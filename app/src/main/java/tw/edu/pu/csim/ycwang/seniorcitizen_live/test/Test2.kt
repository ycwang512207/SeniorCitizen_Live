package tw.edu.pu.csim.ycwang.seniorcitizen_live.test

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import tw.edu.pu.csim.ycwang.seniorcitizen_live.R

class Test2 : AppCompatActivity() {
    private lateinit var btnTest2Y: Button
    private lateinit var btnTest2N: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test2)

        btnTest2Y = findViewById(R.id.btnTest2Y)
        btnTest2Y.setOnClickListener {
            val userUid = intent.getStringExtra("userUid")
            var sum = intent.getStringExtra("sum")?.toInt() ?: 0
            sum++
            val intent = Intent(this, Test2Y::class.java)
            intent.putExtra("userUid", userUid)
            intent.putExtra("sum", sum.toString())
            startActivity(intent)
            finish()
        }

        btnTest2N = findViewById(R.id.btnTest2N)
        btnTest2N.setOnClickListener {
            val userUid = intent.getStringExtra("userUid")
            val sum = intent.getStringExtra("sum")
            val intent = Intent(this, Test2N::class.java)
            intent.putExtra("userUid", userUid)
            intent.putExtra("sum", sum)
            startActivity(intent)
            finish()
        }
    }
}