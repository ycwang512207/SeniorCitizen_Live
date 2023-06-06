package tw.edu.pu.csim.ycwang.seniorcitizen_live.Blood

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import tw.edu.pu.csim.ycwang.seniorcitizen_live.BloodPressureAdapter
import tw.edu.pu.csim.ycwang.seniorcitizen_live.R

class BloodPressureHistoy : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: BloodPressureAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_blood_pressure_histoy)

        // 設置 Action Bar 的標題為「血壓歷史紀錄」
        supportActionBar?.title = "血壓歷史紀錄"

        val actionBar = supportActionBar
        actionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val userUid = intent.getStringExtra("userUid")

        val db = FirebaseFirestore.getInstance()

        val query = db.collection("BloodPressure")
            .whereEqualTo("userId", userUid)
            .orderBy("date", Query.Direction.DESCENDING)
            .orderBy("time", Query.Direction.DESCENDING)

        query.addSnapshotListener{
                querySnapshot, firebaseFirestoreException ->
            if (firebaseFirestoreException != null) {
                //處理查詢異常
                return@addSnapshotListener
            }

            val bloodPressureList = mutableListOf<BloodPressureData>()

            for (document in querySnapshot!!.documents) {
                val bloodPressure = document.toObject(BloodPressureData::class.java)
                bloodPressure?.let {
                    bloodPressureList.add(it)
                }
            }

            adapter = BloodPressureAdapter(bloodPressureList, this)
            recyclerView.adapter = adapter
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // 在此执行返回操作，例如关闭当前 Activity
                val userUid = intent.getStringExtra("userUid")
                val intent = Intent(this, BloodPressure::class.java)
                intent.putExtra("userUid", userUid)
                startActivity(intent)
                finish()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


}