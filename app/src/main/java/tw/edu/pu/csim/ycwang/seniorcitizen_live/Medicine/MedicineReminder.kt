package tw.edu.pu.csim.ycwang.seniorcitizen_live.Medicine

import android.os.Parcelable

data class MedicineReminder(
    val UserUid: String = "",
    val medicationPeriod: String = "", // 用药时段，例如 "晚餐飯後"
    val medicationTime: String = "", // 用药时间，例如 "08:00 PM"
    val medicationDayOfWeek: String = "", // 用药星期，例如 "星期一"
    var alarmEnabled: Boolean = false // 闹钟开关状态
)

