package tw.edu.pu.csim.ycwang.seniorcitizen_live.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import tw.edu.pu.csim.ycwang.seniorcitizen_live.HomeScreen
import tw.edu.pu.csim.ycwang.seniorcitizen_live.R

class Test : AppCompatActivity() {
    private lateinit var btnStart: Button
    private lateinit var btnBackHome: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        supportActionBar?.title = "照護測驗"

        btnStart = findViewById(R.id.btnStart)
        btnStart.setOnClickListener {
            val userUid = intent.getStringExtra("userUid")
            val intent = Intent(this, Test1::class.java)
            intent.putExtra("userUid", userUid)
            startActivity(intent)
            finish()
        }

        btnBackHome = findViewById(R.id.btnBackHome)
        btnBackHome.setOnClickListener {
            val userUid = intent.getStringExtra("userUid")
            val intent = Intent(this, HomeScreen::class.java)
            intent.putExtra("userUid", userUid)
            startActivity(intent)
            finish()
        }
    }
}