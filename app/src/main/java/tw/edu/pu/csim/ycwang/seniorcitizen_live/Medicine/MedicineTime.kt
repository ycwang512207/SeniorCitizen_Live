package tw.edu.pu.csim.ycwang.seniorcitizen_live.Medicine

import android.app.AlarmManager
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import tw.edu.pu.csim.ycwang.seniorcitizen_live.Blood.BloodPressure
import tw.edu.pu.csim.ycwang.seniorcitizen_live.HomeScreen
import tw.edu.pu.csim.ycwang.seniorcitizen_live.Medicine.MedicationAlarmReceiver.Companion.CHANNEL_ID
import tw.edu.pu.csim.ycwang.seniorcitizen_live.R
import java.util.*

class MedicineTime : AppCompatActivity() {

    private lateinit var recyclerViewMedicine: RecyclerView
    private lateinit var adapter: MedicationReminderAdapter
    private val medicineReminderList: MutableList<MedicineReminder> = mutableListOf()
    private lateinit var medicationReminderServiceIntent: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_time)

        // 設置 Action Bar 的標題為「用藥提醒」
        supportActionBar?.title = "用藥提醒"

        val actionBar = supportActionBar
        // 啟用Action Bar菜單項
        actionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerViewMedicine = findViewById(R.id.recyclerViewMedicine)
        recyclerViewMedicine.layoutManager = LinearLayoutManager(this)

        // 初始化 adapter
        adapter = MedicationReminderAdapter(this, medicineReminderList, listOf())
        recyclerViewMedicine.adapter = adapter

        // 从 Firestore 加载数据并排序
        loadDataFromFirestore()

        medicationReminderServiceIntent = Intent(this, MedicationAlarmReceiver::class.java)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Medication Reminder",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "Medication reminder channel"
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }


    }

    override fun onDestroy() {
        super.onDestroy()

        // 保存用户的闹钟开关状态到 SharedPreferences
        val sharedPreferences = getSharedPreferences("MedicationReminder", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        for (reminder in medicineReminderList) {
            editor.putBoolean(reminder.medicationTime, reminder.alarmEnabled)
        }
        editor.apply()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_medication_reminder, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // 在此执行返回操作，例如关闭当前 Activity
                val userUid = intent.getStringExtra("userUid")
                val intent = Intent(this, HomeScreen::class.java)
                intent.putExtra("userUid", userUid)
                startActivity(intent)
                finish()
                true
            }
            R.id.action_add -> {
                val userUid = intent.getStringExtra("userUid")
                val intent = Intent(this, MedicineSetTime::class.java)
                intent.putExtra("userUid", userUid)
                startActivity(intent)

                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun loadDataFromFirestore() {
        val userUid = intent.getStringExtra("userUid") ?: ""

        val db = FirebaseFirestore.getInstance()
        db.collection("MedicineTime")
            .whereEqualTo("userUid", userUid)
            .orderBy("medicationPeriod")
            .orderBy("medicationTime")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    // 处理异常
                    return@addSnapshotListener
                }

                // 清空旧数据
                medicineReminderList.clear()

                val snapshots = snapshot?.documents ?: listOf() // 获取快照的文档列表

                if (snapshot != null) {
                    for (document in snapshot.documents) {
                        val medicineReminder = document.toObject(MedicineReminder::class.java)
                        if (medicineReminder != null) {
                            medicineReminderList.add(medicineReminder) // 将新数据添加到列表中
                        }
                    }
                }

                // 更新适配器
                adapter = MedicationReminderAdapter(this, medicineReminderList, snapshots)
                recyclerViewMedicine.adapter = adapter
                // 更新适配器
                adapter.notifyDataSetChanged()

            }
    }

    override fun onResume() {
        super.onResume()

        val dataChanged = intent.getBooleanExtra("dataChanged", false)
        if (dataChanged) {
            // 从 Firestore 重新加载数据并排序
            loadDataFromFirestore()
        }
        // 从 SharedPreferences 加载用户的闹钟开关状态
        val sharedPreferences = getSharedPreferences("MedicationReminder", Context.MODE_PRIVATE)
        for (reminder in medicineReminderList) {
            reminder.alarmEnabled = sharedPreferences.getBoolean(reminder.medicationTime, false)
        }

        // 检查 adapter 是否已经被初始化，如果没有则进行初始化
        if (!::adapter.isInitialized) {
            adapter = MedicationReminderAdapter(this, medicineReminderList, listOf())
            recyclerViewMedicine.adapter = adapter
        }

        // 更新适配器
        adapter.notifyDataSetChanged()

        // 检查闹钟状态并更新后台服务的提醒列表
        checkAlarmStatusAndUpdateReminderList()
    }

    private fun checkAlarmStatusAndUpdateReminderList() {
        val alarmManager = this.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (reminder in medicineReminderList) {
            if (reminder.alarmEnabled) {
                // 检查是否已经设置闹钟
                val intent = Intent(this, MedicationAlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    reminder.hashCode(),
                    intent,
                    PendingIntent.FLAG_NO_CREATE // 使用 FLAG_NO_CREATE 标志以检查是否已经存在相同的闹钟
                )

                if (pendingIntent == null) {
                    // 闹钟不存在，需要设置新的闹钟
                    val calendar = Calendar.getInstance()
                    val timeParts = reminder.medicationTime.split(":")
                    val hour = timeParts[0].toInt()
                    val minute = timeParts[1].toInt()

                    calendar.set(Calendar.HOUR_OF_DAY, hour)
                    calendar.set(Calendar.MINUTE, minute)
                    calendar.set(Calendar.SECOND, 0)

                    val intent = Intent(this, MedicationAlarmReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(
                        this,
                        reminder.hashCode(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    // 获取星期信息
                    val dayOfWeek = getDayOfWeek(calendar)

                    // 检查是否匹配星期信息
                    val medicationDayOfWeek = reminder.medicationDayOfWeek
                    if (medicationDayOfWeek == "每天") {
                        setAlarmEveryday(alarmManager, calendar, pendingIntent)
                    } else if (medicationDayOfWeek == dayOfWeek) {
                        alarmManager.setRepeating(
                            AlarmManager.RTC_WAKEUP,
                            calendar.timeInMillis,
                            AlarmManager.INTERVAL_DAY,
                            pendingIntent
                        )
                    }
                }
            } else {
                // 取消闹钟
                val intent = Intent(this, MedicationAlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    this,
                    reminder.hashCode(),
                    intent,
                    PendingIntent.FLAG_NO_CREATE // 使用 FLAG_NO_CREATE 标志以检查是否已经存在相同的闹钟
                )

                if (pendingIntent != null) {
                    // 闹钟存在，需要取消闹钟
                    alarmManager.cancel(pendingIntent)
                    pendingIntent.cancel()
                }
            }
        }
    }

    private fun setAlarmEveryday(alarmManager: AlarmManager, calendar: Calendar, pendingIntent: PendingIntent) {
        // 设置每天的闹钟
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            pendingIntent
        )
    }

    private fun getDayOfWeek(calendar: Calendar): String {
        val dayOfWeek = calendar.get(Calendar.DAY_OF_WEEK)
        return when (dayOfWeek) {
            Calendar.SUNDAY -> "星期日"
            Calendar.MONDAY -> "星期一"
            Calendar.TUESDAY -> "星期二"
            Calendar.WEDNESDAY -> "星期三"
            Calendar.THURSDAY -> "星期四"
            Calendar.FRIDAY -> "星期五"
            Calendar.SATURDAY -> "星期六"
            else -> ""
        }
    }



}