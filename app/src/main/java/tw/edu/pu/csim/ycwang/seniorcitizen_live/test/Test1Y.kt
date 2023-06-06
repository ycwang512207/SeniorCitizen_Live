package tw.edu.pu.csim.ycwang.seniorcitizen_live.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import tw.edu.pu.csim.ycwang.seniorcitizen_live.R

class Test1Y : AppCompatActivity() {

    private lateinit var btnYNextTest2: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test1_y)

        btnYNextTest2 = findViewById(R.id.btnYNextTest2)
        btnYNextTest2.setOnClickListener {
            val userUid = intent.getStringExtra("userUid")
            val sum = intent.getStringExtra("sum")
            val intent = Intent(this, Test2::class.java)
            intent.putExtra("userUid", userUid)
            intent.putExtra("sum", sum)
            startActivity(intent)
            finish()
        }
    }
}