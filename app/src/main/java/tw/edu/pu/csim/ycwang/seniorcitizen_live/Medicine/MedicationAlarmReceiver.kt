package tw.edu.pu.csim.ycwang.seniorcitizen_live.Medicine

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat
import tw.edu.pu.csim.ycwang.seniorcitizen_live.R

class MedicationAlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d("MedicationAlarmReceiver", "Received medication reminder broadcast")
        // 接收到用藥提醒的廣播後，顯示通知
        showMedicationReminderNotification(context?.applicationContext, intent?.getStringExtra("medicationPeriod"))
    }

    private fun showMedicationReminderNotification(context: Context?, medicationPeriod: String?) {
        val notificationTitle = getNotificationTitle(medicationPeriod)
        val notificationText = getNotificationText(medicationPeriod)
        // 建立通知
        val notificationBuilder = NotificationCompat.Builder(context!!, CHANNEL_ID)
            .setContentTitle(notificationTitle)
            .setContentText(notificationText)
            .setSmallIcon(R.drawable.clock)
            .setAutoCancel(true)

        // 設置通知優先级
        notificationBuilder.priority = NotificationCompat.PRIORITY_HIGH

        // 發送通知
        val notificationManager = context?.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }

    private fun getNotificationTitle(medicationPeriod: String?): String {
        return when (medicationPeriod) {
            "早餐飯前" -> "早餐飯前用藥提醒"
            "早餐飯後" -> "早餐飯後用藥提醒"
            "午餐飯前" -> "午餐飯前用藥提醒"
            "午餐飯後" -> "午餐飯後用藥提醒"
            "晚餐飯前" -> "晚餐飯前用藥提醒"
            "晚餐飯後" -> "晚餐飯後用藥提醒"
            "睡前" -> "睡前用藥提醒"
            else -> "用藥提醒"
        }
    }

    private fun getNotificationText(medicationPeriod: String?): String {
        return when (medicationPeriod) {
            "早餐飯前" -> "該服用早餐飯前的藥囉！"
            "早餐飯後" -> "該服用早餐飯後的藥囉！"
            "午餐飯前" -> "該服用午餐飯前的藥囉！"
            "午餐飯後" -> "該服用午餐飯後的藥囉！"
            "晚餐飯前" -> "該服用晚餐飯前的藥囉！"
            "晚餐飯後" -> "該服用晚餐飯後的藥囉！"
            "睡前" -> "該服用睡前的藥囉！"
            else -> "該服藥了！"
        }
    }

    companion object {
        const val CHANNEL_ID = "medication_reminder_channel"
        const val NOTIFICATION_ID = 1
    }
}