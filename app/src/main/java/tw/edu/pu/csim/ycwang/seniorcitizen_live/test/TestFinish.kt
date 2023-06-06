package tw.edu.pu.csim.ycwang.seniorcitizen_live.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import tw.edu.pu.csim.ycwang.seniorcitizen_live.HomeScreen
import tw.edu.pu.csim.ycwang.seniorcitizen_live.R

class TestFinish : AppCompatActivity() {
    private lateinit var txvSum: TextView
    private lateinit var btnTestBack: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_finish)
        val sum = intent.getStringExtra("sum")

        txvSum = findViewById(R.id.txvSum)
        txvSum.text = "恭喜答對了${sum}題！"

        btnTestBack = findViewById(R.id.btnTestBack)
        btnTestBack.setOnClickListener {
            val userUid = intent.getStringExtra("userUid")
            val intent = Intent(this, HomeScreen::class.java)
            intent.putExtra("userUid", userUid)
            startActivity(intent)
            finish()
        }
    }
}