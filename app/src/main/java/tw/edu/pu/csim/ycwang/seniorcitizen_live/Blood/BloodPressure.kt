package tw.edu.pu.csim.ycwang.seniorcitizen_live.Blood

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
import tw.edu.pu.csim.ycwang.seniorcitizen_live.HomeScreen
import tw.edu.pu.csim.ycwang.seniorcitizen_live.R
import java.util.*

class BloodPressure : AppCompatActivity() {

    lateinit var txvDate: TextView
    lateinit var btnDate: Button
    lateinit var spinnerTime: Spinner
    lateinit var editTextSystolic: EditText
    lateinit var editTextDiastolic: EditText
    lateinit var editTextHeartBeat: EditText
    lateinit var btnBack: Button
    lateinit var btnSave: Button
    lateinit var btnHistory: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blood_pressure)

        // 設置 Action Bar 的標題為「血壓紀錄」
        supportActionBar?.title = "血壓紀錄"

        txvDate = findViewById(R.id.txvDate)
        btnDate = findViewById(R.id.btnDate)
        spinnerTime = findViewById(R.id.spinnerTime)
        editTextSystolic = findViewById(R.id.editTextSystolic)
        editTextDiastolic = findViewById(R.id.editTextDiastolic)
        editTextHeartBeat = findViewById(R.id.editTextHeartBeat)
        btnBack = findViewById(R.id.btnBack)
        btnSave = findViewById(R.id.btnSave)
        btnHistory = findViewById(R.id.btnHistory)

        btnDate.setOnClickListener {
            showDatePickerDialog()
        }

        btnBack.setOnClickListener {
            val userUid = intent.getStringExtra("userUid")
            val intent = Intent(this, HomeScreen::class.java)
            intent.putExtra("userUid", userUid)
            startActivity(intent)
            finish()
        }

        btnHistory.setOnClickListener {
            val userUid = intent.getStringExtra("userUid").toString()
            val intent = Intent(this, BloodPressureHistoy::class.java)
            if (userUid != null) {
                intent.putExtra("userUid", userUid)
            }
            startActivity(intent)
            finish()
        }

        btnSave.setOnClickListener {
            submitBloodPressureRecord()
        }

        // 設置時段選擇的選項
        val periodOptions = arrayOf("早上", "晚上")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, periodOptions)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerTime.adapter = adapter


    }

    private fun showDatePickerDialog() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(this, {_, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = "$selectedYear-${selectedMonth + 1}-$selectedDay"
            txvDate.text = selectedDate
        }, year, month, day)

        datePickerDialog.show()
    }

    private fun submitBloodPressureRecord() {
        val userUid = intent.getStringExtra("userUid").toString()
        val selectedDate = txvDate.text.toString()
        val selectedTime = spinnerTime.selectedItem.toString()
        val systolic = editTextSystolic.text.toString()
        val diastolic = editTextDiastolic.text.toString()
        val heartBeat = editTextHeartBeat.text.toString()

        if (selectedDate == "日期") {
            Toast.makeText(this, "請選擇日期", Toast.LENGTH_SHORT).show()
            return
        }

        if (systolic.isEmpty() || diastolic.isEmpty() || heartBeat.isEmpty()){
            Toast.makeText(this, "請輸入完整血壓數據", Toast.LENGTH_SHORT).show()
        }

        else if (systolic.length < 2 || systolic.length > 3 ||
            diastolic.length < 2 || diastolic.length > 3 ||
            heartBeat.length < 2 || heartBeat.length > 3
        ) {
            Toast.makeText(this, "血壓數據的輸入格式不正確", Toast.LENGTH_SHORT).show()
            return
        }

        try {
            val systolicValue = systolic.toInt()
            val diastolicValue = diastolic.toInt()
            val heartBeatValue = heartBeat.toInt()

            val bloodPressure = BloodPressureData(
                userUid,  // 使用者 UID
                selectedDate,  // 選取的日期
                selectedTime,  // 選取的時段
                systolicValue,  // 收縮壓數值
                diastolicValue,  // 舒張壓數值
                heartBeatValue  // 心跳數值
            )

            // 執行保存血壓紀錄的操作
            val db = FirebaseFirestore.getInstance()
            db.collection("BloodPressure").add(bloodPressure).addOnSuccessListener {
                    documentReference ->
                // 保存成功
                if (systolic.toInt() < 90 && diastolic.toInt() < 60) {
                    Toast.makeText(this, "血壓紀錄已儲存成功！血壓過低囉！休息10分鐘後再量一次吧", Toast.LENGTH_LONG).show()
                }
                else if (systolic.toInt() > 130 && diastolic.toInt() > 80) {
                    Toast.makeText(this, "血壓紀錄已儲存成功！血壓過高囉！休息10分鐘後再量一次吧", Toast.LENGTH_LONG).show()
                }
                else {
                    Toast.makeText(this, "血壓紀錄已儲存成功！血壓正常，繼續保持！", Toast.LENGTH_LONG).show()
                }

                clearInputFields()
            }.addOnFailureListener{
                    e ->
                // 處理保存失敗的情況
                Toast.makeText(this, "血壓紀錄儲存失敗", Toast.LENGTH_SHORT).show()
            }
        } catch (e: NumberFormatException) { }
    }

    private fun clearInputFields() {
        txvDate.text = "日期"
        spinnerTime.setSelection(0)
        editTextSystolic.text.clear()
        editTextDiastolic.text.clear()
        editTextHeartBeat.text.clear()
    }

}