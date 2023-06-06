package tw.edu.pu.csim.ycwang.seniorcitizen_live

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.ActionBar
import com.google.firebase.auth.FirebaseAuth
import tw.edu.pu.csim.ycwang.seniorcitizen_live.Blood.BloodPressure
import tw.edu.pu.csim.ycwang.seniorcitizen_live.Medicine.MedicineTime
import tw.edu.pu.csim.ycwang.seniorcitizen_live.knowledge.Knowledge
import tw.edu.pu.csim.ycwang.seniorcitizen_live.test.Test

class HomeScreen : AppCompatActivity() {

    lateinit var btnOut: Button
    lateinit var firebaseAuth: FirebaseAuth
    lateinit var actionBar: ActionBar
    lateinit var btnKnowledge: Button
    lateinit var btnTest: Button
    lateinit var btnBloodPressure: Button
    lateinit var btnMedicineTime: Button
    lateinit var btnCommunicate: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        actionBar = supportActionBar!!
        actionBar.hide()

        val userUid = intent.getStringExtra("userUid")

        firebaseAuth = FirebaseAuth.getInstance()

        btnOut = findViewById(R.id.btnOut)
        btnOut.setOnClickListener {
            logoutUser()
        }

        btnKnowledge = findViewById(R.id.btnKnowledge)
        btnKnowledge.setOnClickListener {
            val intent1 = Intent(this, Knowledge::class.java)
            if (userUid != null) {
                intent1.putExtra("userUid", userUid)
            }
            startActivity(intent1)
            finish()
        }

        btnTest = findViewById(R.id.btnTest)
        btnTest.setOnClickListener {
            val intent2 = Intent(this, Test::class.java)
            if (userUid != null) {
                intent2.putExtra("userUid", userUid)
            }
            startActivity(intent2)
            finish()
        }

        btnBloodPressure = findViewById(R.id.btnBloodPressure)
        btnBloodPressure.setOnClickListener {
            val intent3 = Intent(this, BloodPressure::class.java)
            if (userUid != null) {
                intent3.putExtra("userUid", userUid)
            }
            startActivity(intent3)
            finish()
        }

        btnMedicineTime = findViewById(R.id.btnMedicineTime)
        btnMedicineTime.setOnClickListener {
            val intent4 = Intent(this, MedicineTime::class.java)
            if (userUid != null) {
                intent4.putExtra("userUid", userUid)
            }
            startActivity(intent4)
            finish()
        }

        btnCommunicate = findViewById(R.id.btnCommunicate)
        btnCommunicate.setOnClickListener {
            val intent5 = Intent(this, Communicate::class.java)
            if (userUid != null) {
                intent5.putExtra("userUid", userUid)
            }
            startActivity(intent5)
            finish()
        }

    }

    private fun logoutUser() {
        // 使用 Firebase 身分驗證進行登出
        firebaseAuth.signOut()
        // 清除使用者登入狀態
        clearUserLoggedInState()
        // 轉到 Login
        val intent = Intent(this, Login::class.java)
        startActivity(intent)
        finish()
    }

    private fun clearUserLoggedInState() {
        val userPreferences = UserPreferences(this)
        userPreferences.userLoggedIn = false
        userPreferences.userUid = null
    }
}