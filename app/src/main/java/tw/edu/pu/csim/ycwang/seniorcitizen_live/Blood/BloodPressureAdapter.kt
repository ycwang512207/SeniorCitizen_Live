package tw.edu.pu.csim.ycwang.seniorcitizen_live
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import tw.edu.pu.csim.ycwang.seniorcitizen_live.Blood.BloodPressureData

class BloodPressureAdapter(
    private val bloodPressureList: List<BloodPressureData>,
    private val context: Context
) :
    RecyclerView.Adapter<BloodPressureAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.blood_pressure_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bloodPressure = bloodPressureList[position]

        // 將血壓數據绑定到item上
        holder.txvDataDate.text = bloodPressure.date
        holder.txvDataTime.text = bloodPressure.time
        holder.txvSystolic.text = bloodPressure.systolic.toString()
        holder.txvDiastolic.text = bloodPressure.diastolic.toString()
        holder.txvHeartBeat.text = bloodPressure.heartBeat.toString()

        // 根據收缩壓和舒张壓判断血壓狀態並設置相應的文字和顏色
        val bloodPressureStatus = checkBloodPressureStatus(
            bloodPressure.systolic,
            bloodPressure.diastolic
        )
        holder.txvPressureStatus.text = bloodPressureStatus.text
        holder.txvPressureStatus.setTextColor(bloodPressureStatus.textColor)
    }

    // 輔助方法：檢查收缩壓和舒张壓的狀態
    private fun checkBloodPressureStatus(systolic: Int, diastolic: Int): BloodPressureStatus {
        return when {
            systolic < 90 && diastolic < 60 -> BloodPressureStatus("血壓過低", ContextCompat.getColor(context, R.color.black))
            systolic > 130 && diastolic > 80 -> BloodPressureStatus("血壓過高", ContextCompat.getColor(context, R.color.purple_500))
            else -> BloodPressureStatus("正常", ContextCompat.getColor(context, R.color.purple_200))
        }
    }

    override fun getItemCount(): Int {
        return bloodPressureList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txvDataDate: TextView = itemView.findViewById(R.id.txvDataDate)
        val txvDataTime: TextView = itemView.findViewById(R.id.txvDataTime)
        val txvSystolic: TextView = itemView.findViewById(R.id.txvSystolic)
        val txvDiastolic: TextView = itemView.findViewById(R.id.txvDiastolic)
        val txvHeartBeat: TextView = itemView.findViewById(R.id.txvHeartBeat)
        val txvPressureStatus: TextView = itemView.findViewById(R.id.txvPressureStatus)
    }
}
// 數據類：血壓狀態
data class BloodPressureStatus(val text: String, val textColor: Int)
