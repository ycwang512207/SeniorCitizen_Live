package tw.edu.pu.csim.ycwang.seniorcitizen_live.Medicine

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.google.firebase.firestore.FirebaseFirestore
import tw.edu.pu.csim.ycwang.seniorcitizen_live.R
import java.text.SimpleDateFormat
import java.util.*

class MedicineSetTime : AppCompatActivity() {
    private lateinit var spinnerSetTimePeriod: Spinner
    private lateinit var txvSetTime: TextView
    private lateinit var btnSetTime: Button
    private lateinit var cbSetMonday: CheckBox
    private lateinit var cbSetTuesday: CheckBox
    private lateinit var cbSetWednesday: CheckBox
    private lateinit var cbSetThursday: CheckBox
    private lateinit var cbSetFriday: CheckBox
    private lateinit var cbSetSaturday: CheckBox
    private lateinit var cbSetSunday: CheckBox
    private lateinit var btnSetOk: Button
    private lateinit var btnDelete: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_medicine_set_time)

        // 設置 Action Bar 的標題為「用藥提醒」
        supportActionBar?.title = "用藥提醒設定"

        // 接收userUid和documentId的值
        val userUid = intent.getStringExtra("userUid") ?: ""

        spinnerSetTimePeriod = findViewById<Spinner>(R.id.spinnerSetTimePeriod)
        txvSetTime = findViewById(R.id.txvSetTime)
        btnSetTime = findViewById(R.id.btnSetTime)
        cbSetMonday = findViewById(R.id.cbSetMonday)
        cbSetTuesday = findViewById(R.id.cbSetTuesday)
        cbSetWednesday = findViewById(R.id.cbSetWednesday)
        cbSetThursday = findViewById(R.id.cbSetThursday)
        cbSetFriday = findViewById(R.id.cbSetFriday)
        cbSetSaturday = findViewById(R.id.cbSetSaturday)
        cbSetSunday = findViewById(R.id.cbSetSunday)
        btnSetOk = findViewById(R.id.btnSetOK)
        btnDelete = findViewById(R.id.btnDelete)

        // 假設時段選項儲存在一個字符串數組中
        val medicationPeriods = arrayOf("早餐飯前", "早餐飯後", "午餐飯前", "午餐飯後", "晚餐飯前", "晚餐飯後", "睡前")

        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, medicationPeriods)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerSetTimePeriod.adapter = adapter


        // 設置時間選擇按钮的點擊事件
        btnSetTime.setOnClickListener {
            showTimePickerDialog()
        }

        val fromItemClick = intent.getBooleanExtra("fromItemClick", false)
        if (fromItemClick) {
            // 從列表項點擊打開
            // 執行相應的操作
            val documentId = intent.getStringExtra("documentId")
            // 從Firestore中檢索文檔數據
            val db = FirebaseFirestore.getInstance()
            val documentRef = db.collection("MedicineTime").document(documentId.toString())
            documentRef.get()
                .addOnSuccessListener { documentSnapshot ->
                    if (documentSnapshot.exists()) {
                        val medicineReminder = documentSnapshot.toObject(MedicineReminder::class.java)

                        // 將數據應用於相應的item组件
                        if (medicineReminder != null) {
                            txvSetTime.text = medicineReminder.medicationTime
                        }
                        if (medicineReminder != null) {
                            // 將適配器聲明为類级别的變量
                            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                            spinnerSetTimePeriod.adapter = adapter

                            spinnerSetTimePeriod.setSelection(adapter.getPosition(medicineReminder.medicationPeriod))
                        }
                        // 根據星期數據設置對應的 CheckBox
                        if (medicineReminder != null) {
                            setWeekdayCheckBoxes(medicineReminder.medicationDayOfWeek)

                            // 判斷是否選擇了 "每天" 選項
                            if (medicineReminder.medicationDayOfWeek == "每天") {
                                // 如果選擇了 "每天"，將所有複選框都勾選上
                                cbSetMonday.isChecked = true
                                cbSetTuesday.isChecked = true
                                cbSetWednesday.isChecked = true
                                cbSetThursday.isChecked = true
                                cbSetFriday.isChecked = true
                                cbSetSaturday.isChecked = true
                                cbSetSunday.isChecked = true
                            }
                        }

                        // 從Firestore中檢索到了文檔的數據，調用upData方法進行更新
                        // 設置完成按钮的點擊事件
                        btnSetOk.setOnClickListener {
                            UpData(userUid, documentId.toString())
                        }
                        btnDelete.setOnClickListener {
                            val db = FirebaseFirestore.getInstance()
                            val documentRef = db.collection("MedicineTime").document(documentId.toString())

                            documentRef.delete() // 删除文檔
                                .addOnSuccessListener {
                                    Toast.makeText(this, "用藥提醒已刪除", Toast.LENGTH_SHORT).show()

                                    // 設置返回结果，以便在 MedicineTime 頁面重新加載數據
                                    val intent = Intent()
                                    intent.putExtra("dataChanged", true)
                                    setResult(Activity.RESULT_OK, intent)

                                    finish() // 關閉當前 Activity
                                }
                                .addOnFailureListener { e ->
                                    Toast.makeText(this, "刪除失敗：${e.message}", Toast.LENGTH_SHORT).show()
                                }
                        }

                    }
                }
                .addOnFailureListener { e ->
                    // 處理檢索文檔數據失敗的情況
                }
        } else {
            // 設置完成按紐的點擊事件
            btnSetOk.text = "新增提醒"
            btnSetOk.setOnClickListener {
                addData(userUid)
            }
            btnDelete.text = "取消返回"
            btnDelete.setOnClickListener {
                val intent = Intent(this, MedicineTime::class.java)
                intent.putExtra("userUid", userUid)
                startActivity(intent)
            }
        }

    }

    private fun showTimePickerDialog() {
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(this, TimePickerDialog.OnTimeSetListener { _, selectedHour, selectedMinute ->
            // 在TextView中顯示選擇的時間
            txvSetTime.text = String.format("%02d:%02d", selectedHour, selectedMinute)
        }, hour, minute, true)

        timePickerDialog.show()
    }

    private fun addData(userUid: String) {
        val selectedPeriod = spinnerSetTimePeriod.selectedItem.toString()
        val selectedTime = txvSetTime.text.toString()
        val selectedWeekdays = getSelectedWeekdays()

        // 判斷是否選擇了星期
        if (selectedWeekdays.isEmpty()) {
            Toast.makeText(this, "請選擇星期", Toast.LENGTH_SHORT).show()
            return
        }

        // 驗證時段選擇和時間是否符合
        if (!isPeriodTimeMatch(selectedPeriod, selectedTime)) {
            Toast.makeText(this, "時間和時段不相符", Toast.LENGTH_SHORT).show()
            return
        }

        val medicineReminder = MedicineReminder(
            UserUid = userUid,
            medicationPeriod = selectedPeriod,
            medicationTime = selectedTime,
            medicationDayOfWeek = selectedWeekdays.joinToString(", "), // 將星期列表轉換為逗號分隔的字符串
            alarmEnabled = false // 默認設置為 false
        )

        val db = FirebaseFirestore.getInstance()
        db.collection("MedicineTime")
            .add(medicineReminder)
            .addOnSuccessListener { documentReference ->
                // 成功添加文檔后的操作，例如顯示成功消息
                Toast.makeText(this, "用藥提醒已設定", Toast.LENGTH_SHORT).show()

                val intent = Intent(this, MedicineTime::class.java)
                intent.putExtra("userUid", userUid)
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                // 添加文檔失敗時的操作，例如顯示錯誤消息
                Toast.makeText(this, "設定失敗：${e.message}", Toast.LENGTH_SHORT).show()
            }

    }

    private fun UpData(userUid: String, documentId: String){
        val selectedPeriod = spinnerSetTimePeriod.selectedItem.toString()
        val selectedTime = txvSetTime.text.toString()
        val selectedWeekdays = getSelectedWeekdays()

        // 判断是否選擇了星期
        if (selectedWeekdays.isEmpty()) {
            Toast.makeText(this, "請選擇星期", Toast.LENGTH_SHORT).show()
            return
        }

        // 驗證時段選擇和時間是否符合
        if (!isPeriodTimeMatch(selectedPeriod, selectedTime)) {
            Toast.makeText(this, "時間和時段不相符", Toast.LENGTH_SHORT).show()
            return
        }

        val db = FirebaseFirestore.getInstance()
        val documentRef = db.collection("MedicineTime").document(documentId)
        documentRef.update("medicationPeriod", selectedPeriod,
            "medicationTime", selectedTime,
            "medicationDayOfWeek", selectedWeekdays.joinToString(", ")) // 使用 set() 方法更新文檔數據
            .addOnSuccessListener {
                // 成功更新文檔后的操作，例如顯示成功消息
                Toast.makeText(this, "用藥提醒已更新", Toast.LENGTH_SHORT).show()

                // 設置返回结果，以便在 MedicineTime 頁面重新加載數據
                val intent = Intent()
                intent.putExtra("dataChanged", true)
                setResult(Activity.RESULT_OK, intent)

                finish() // 關閉當前 Activity
            }
            .addOnFailureListener { e ->
                // 更新文檔失敗時的操作，例如顯示錯誤消息
                Toast.makeText(this, "更新失敗：${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun getSelectedWeekdays(): List<String> {
        val weekdays = mutableListOf<String>()

        if (cbSetMonday.isChecked && cbSetTuesday.isChecked && cbSetWednesday.isChecked &&
            cbSetThursday.isChecked && cbSetFriday.isChecked && cbSetSaturday.isChecked &&
            cbSetSunday.isChecked) {
            weekdays.add("每天")
        }
        else {
            if (cbSetMonday.isChecked) {
                weekdays.add("星期一")
            }
            if (cbSetTuesday.isChecked) {
                weekdays.add("星期二")
            }
            if (cbSetWednesday.isChecked) {
                weekdays.add("星期三")
            }
            if (cbSetThursday.isChecked) {
                weekdays.add("星期四")
            }
            if (cbSetFriday.isChecked) {
                weekdays.add("星期五")
            }
            if (cbSetSaturday.isChecked) {
                weekdays.add("星期六")
            }
            if (cbSetSunday.isChecked) {
                weekdays.add("星期日")
            }
        }



        return weekdays
    }

    private fun isPeriodTimeMatch(selectedPeriod: String, selectedTime: String): Boolean {
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = format.parse(selectedTime)

        return when (selectedPeriod) {
            "早餐飯前", "早餐飯後" -> {
                time.before(format.parse("12:00"))
            }
            "午餐飯前", "午餐飯後" -> {
                time.before(format.parse("17:00"))
            }
            "晚餐飯前", "晚餐飯後" -> {
                time.before(format.parse("21:00"))
            }
            "睡前" -> {
                time.after(format.parse("00:00"))
            }
            else -> false
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()

        val userUid = intent.getStringExtra("userUid")
        val intent = Intent(this, MedicineTime::class.java)
        intent.putExtra("userUid", userUid)
        startActivity(intent)
        finish()
    }

    private fun setWeekdayCheckBoxes(weekdayData: String) {
        val weekdays = weekdayData.split(", ")

        cbSetMonday.isChecked = weekdays.contains("星期一")
        cbSetTuesday.isChecked = weekdays.contains("星期二")
        cbSetWednesday.isChecked = weekdays.contains("星期三")
        cbSetThursday.isChecked = weekdays.contains("星期四")
        cbSetFriday.isChecked = weekdays.contains("星期五")
        cbSetSaturday.isChecked = weekdays.contains("星期六")
        cbSetSunday.isChecked = weekdays.contains("星期日")
    }


}