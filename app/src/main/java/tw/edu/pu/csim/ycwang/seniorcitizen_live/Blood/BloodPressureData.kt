package tw.edu.pu.csim.ycwang.seniorcitizen_live.Blood

data class BloodPressureData(
    val userId: String = "",
    val date: String = "",
    val time: String = "",
    val systolic: Int = 0,
    val diastolic: Int = 0,
    val heartBeat: Int = 0
)