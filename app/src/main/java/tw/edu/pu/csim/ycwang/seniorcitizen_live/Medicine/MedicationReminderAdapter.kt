package tw.edu.pu.csim.ycwang.seniorcitizen_live.Medicine

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Switch
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import tw.edu.pu.csim.ycwang.seniorcitizen_live.R
import java.util.*

class MedicationReminderAdapter(
    private val context: Context,
    private val medicineReminderList: List<MedicineReminder>,
    private val snapshots: List<DocumentSnapshot> // 添加快照列表参数
    ) :
    RecyclerView.Adapter<MedicationReminderAdapter.ViewHolder>(){

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtMedicationPeriod: TextView = itemView.findViewById(R.id.txtMedicationPeriod)
        val txtMedicationTime: TextView = itemView.findViewById(R.id.txtMedicationTime)
        val txtMedicationDayOfWeek: TextView = itemView.findViewById(R.id.txtMedicationDayOfWeek)
        val switchAlarm: Switch = itemView.findViewById(R.id.switchAlarm)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.medicine_time_item, parent, false)
        return  ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val medicineReminder = medicineReminderList[position]
        val documentId = snapshots[position].id // 获取 Firestore 文档的 ID

        holder.txtMedicationPeriod.text = medicineReminder.medicationPeriod
        holder.txtMedicationTime.text = medicineReminder.medicationTime
        holder.txtMedicationDayOfWeek.text = medicineReminder.medicationDayOfWeek
        holder.switchAlarm.isChecked = medicineReminder.alarmEnabled

        // 設置鬧鐘開關狀態變化的監聽器
        holder.switchAlarm.setOnCheckedChangeListener { _, isChecked ->
            medicineReminder.alarmEnabled = isChecked
            updateSwitchStateInFirestore(documentId, isChecked) // 将更新的状态同步到 Firestore
            checkAlarmStatusAndUpdateReminderList() // 檢查鬧鐘狀態並更新後台服務的提醒列表
        }
        holder.itemView.setOnClickListener {
            val context = holder.itemView.context
            val intent = Intent(context, MedicineSetTime::class.java)
            intent.putExtra("documentId", documentId) // 将文档 ID 传递给下一个活动
            intent.putExtra("fromItemClick", true) // 设置来源为点击列表项
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return medicineReminderList.size
    }

    private fun updateSwitchStateInFirestore(documentId: String, isChecked: Boolean) {
        val db = FirebaseFirestore.getInstance()
        val documentRef = db.collection("MedicineTime").document(documentId)

        documentRef.update("alarmEnabled", isChecked) // 更新文档的 isAlarmEnabled 字段
            .addOnSuccessListener {
                // 更新成功后的操作，例如显示成功消息
            }
            .addOnFailureListener { e ->
                // 更新失败时的操作，例如显示错误消息
            }
    }

    private fun checkAlarmStatusAndUpdateReminderList() {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        for (reminder in medicineReminderList) {
            if (reminder.alarmEnabled) {
                // 检查是否已经设置闹钟
                val intent = Intent(context, MedicationAlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
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

                    val intent = Intent(context, MedicationAlarmReceiver::class.java)
                    val pendingIntent = PendingIntent.getBroadcast(
                        context,
                        reminder.hashCode(),
                        intent,
                        PendingIntent.FLAG_UPDATE_CURRENT
                    )

                    alarmManager.setRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.timeInMillis,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                    )
                }
            } else {
                // 取消闹钟
                val intent = Intent(context, MedicationAlarmReceiver::class.java)
                val pendingIntent = PendingIntent.getBroadcast(
                    context,
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



}