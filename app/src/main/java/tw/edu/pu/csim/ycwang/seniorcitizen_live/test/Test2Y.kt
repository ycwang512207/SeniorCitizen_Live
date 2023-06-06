package tw.edu.pu.csim.ycwang.seniorcitizen_live.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import tw.edu.pu.csim.ycwang.seniorcitizen_live.R

class Test2Y : AppCompatActivity() {
    private lateinit var btnYFinish: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2_y)
        btnYFinish = findViewById(R.id.btnYFinish)
        btnYFinish.setOnClickListener {
            val userUid = intent.getStringExtra("userUid")
            val sum = intent.getStringExtra("sum")
            val intent = Intent(this, TestFinish::class.java)
            intent.putExtra("userUid", userUid)
            intent.putExtra("sum", sum)
            startActivity(intent)
            finish()
        }
    }
}