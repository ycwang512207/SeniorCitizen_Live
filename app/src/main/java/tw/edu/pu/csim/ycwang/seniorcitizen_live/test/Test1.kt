package tw.edu.pu.csim.ycwang.seniorcitizen_live.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import tw.edu.pu.csim.ycwang.seniorcitizen_live.R

class Test1 : AppCompatActivity() {
    private lateinit var btnTest1Y: Button
    private lateinit var btnTest1N: Button
    var sum: Int = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test1)

        btnTest1Y = findViewById(R.id.btnTest1Y)
        btnTest1Y.setOnClickListener {
            val userUid = intent.getStringExtra("userUid")
            val intent = Intent(this, Test1Y::class.java)
            intent.putExtra("userUid", userUid)
            intent.putExtra("sum", sum)
            startActivity(intent)
            finish()
        }

        btnTest1N = findViewById(R.id.btnTest1N)
        btnTest1N.setOnClickListener {
            sum++
            val userUid = intent.getStringExtra("userUid")
            val intent = Intent(this, Test1N::class.java)
            intent.putExtra("userUid", userUid)
            intent.putExtra("sum", sum.toString())
            startActivity(intent)
            finish()
        }

    }
}