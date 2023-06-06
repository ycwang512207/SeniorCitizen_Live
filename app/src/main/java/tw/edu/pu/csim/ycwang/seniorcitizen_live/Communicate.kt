package tw.edu.pu.csim.ycwang.seniorcitizen_live

import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.GestureDetector
import android.view.GestureDetector.OnGestureListener
import android.view.MenuItem
import android.view.MotionEvent
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView

class Communicate : AppCompatActivity(), OnGestureListener {

    private lateinit var img: ImageView
    private lateinit var txv: TextView
    private lateinit var btnI: Button
    private lateinit var btnC: Button
    private lateinit var gDetector: GestureDetector
    private lateinit var mediaPlayer: MediaPlayer
    private var count: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_communicate)

        supportActionBar?.title = "溝通工具"

        val actionBar = supportActionBar
        // 啟用Action Bar菜單項
        actionBar?.setDisplayHomeAsUpEnabled(true)

        img = findViewById(R.id.img)
        txv = findViewById(R.id.txv)
        btnI = findViewById(R.id.btnI)
        btnC = findViewById(R.id.btnC)
        mediaPlayer = MediaPlayer()
        gDetector = GestureDetector(this, this)
        btnI.setOnClickListener {
            if (count == 1){
                mediaPlayer.reset()
                mediaPlayer = MediaPlayer.create(this, R.raw.makanlah)
                mediaPlayer.start()
            }
            else if (count == 2){
                mediaPlayer.reset()
                mediaPlayer = MediaPlayer.create(this, R.raw.kencing)
                mediaPlayer.start()
            }
            else{
                mediaPlayer.reset()
                mediaPlayer = MediaPlayer.create(this, R.raw.tinja)
                mediaPlayer.start()
            }
        }

        btnC.setOnClickListener {
            if (count == 1){
                mediaPlayer.reset()
                mediaPlayer = MediaPlayer.create(this, R.raw.eat)
                mediaPlayer.start()
            }
            else if (count == 2){
                mediaPlayer.reset()
                mediaPlayer = MediaPlayer.create(this, R.raw.pee)
                mediaPlayer.start()
            }
            else{
                mediaPlayer.reset()
                mediaPlayer = MediaPlayer.create(this, R.raw.t)
                mediaPlayer.start()
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // 在此执行返回操作，例如关闭当前 Activity
                val userUid = intent.getStringExtra("userUid")
                val intent = Intent(this, HomeScreen::class.java)
                intent.putExtra("userUid", userUid)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gDetector.onTouchEvent(event)
        return true
    }

    override fun onDown(p0: MotionEvent): Boolean {
        return true
    }

    override fun onShowPress(p0: MotionEvent) {
    }

    override fun onSingleTapUp(p0: MotionEvent): Boolean {
        return true
    }

    override fun onScroll(p0: MotionEvent, p1: MotionEvent, p2: Float, p3: Float): Boolean {
        return true
    }

    override fun onLongPress(p0: MotionEvent) {

    }

    override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
        if (e1.x <= e2.x){
            count++
            if (count > 3) {
                count = 1
            }
        }
        else{
            count --
            if (count < 1){
                count = 3
            }
        }
        var res:Int = getResources().getIdentifier("c" + count.toString(),
            "drawable", getPackageName())
        img.setImageDrawable(getResources().getDrawable(res))
        if (count == 1){
            txv.text = "吃飯"
        }
        else if (count == 2){
            txv.text = "尿尿"
        }
        else{
            txv.text = "大便"
        }
        return true
    }
}