package tw.edu.pu.csim.ycwang.seniorcitizen_live.knowledge

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import tw.edu.pu.csim.ycwang.seniorcitizen_live.HomeScreen
import tw.edu.pu.csim.ycwang.seniorcitizen_live.R

class Knowledge : AppCompatActivity() {

    private lateinit var recyclerViewKnowledge: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_knowledge)

        supportActionBar?.title = "衛教知識"

        val actionBar = supportActionBar
        // 啟用Action Bar菜單項
        actionBar?.setDisplayHomeAsUpEnabled(true)

        recyclerViewKnowledge = findViewById(R.id.recyclerViewKnowledge)
        recyclerViewKnowledge.layoutManager = LinearLayoutManager(this)

        val itemList = listOf(
            ItemModel("退化性關節炎\n（Osteoarthritis）", "退化性關節炎是一種常見的關節病，尤其在老年人中比較普遍。磨損和破損引起的，通常會影響手、膝、髖和腦癱等關節。退化性關節炎可能導致關節痛、悶硬、悶熱和活動受限，並且在日常生活中造成不便和不適應。這種病的風危險因素包年、遺傳、關節受傷害、肥胖和長期使用關節等。治療方法通常包括保持適當度的運動、控制體重、物理治療、藥物治療和技術等，以減輕疼痛和改善能力。"),
            ItemModel("阿茲海默病\n（Alzheimer's disease）", "阿茲海默病是一種逐漸發展的神經退行性疾患，主要影響老年人的認知能力。它通常以記憶力退、語言障礙、思維能力下降和行為變化為特徵。阿滋海默病的病因尚不完全清楚，但與神經細胞的退化和腦中異常蛋白錯亂有關。這種病會逃避使老人失去日常生活技能，並對其他人和照顧者產生巨大負擔。目前還沒有治療阿茲海默病的方法，但可以通過藥物治療和認識訓練等方式來延緩疾病的進展，提高者的生活活質量。"),
            ItemModel("中風\n（Stroke）", "中風是一種血液供應到大腦的血管突然中斷或破裂，導致大腦組織缺氧和損傷的情況。老人是中風的高風險人群之一。中風可能導致突然出現的表面或身體一側無力、語言障礙、視力喪失、平衡失調和嚴重頭疼等症狀。中風分為兩種主要類型：缺血性中風和出血性中風。缺血性中風是由於腦部血管塞而引起，而出血性中風是由於腦部血管破裂而引起。中風的風險因素包括高血壓、高血脂、糖尿病、心髒病、吸煙、肥胖和缺乏運動等。早期診斷和即時治療對中風的預測至關重要。"),
            ItemModel("心臟病\n（Heart Disease）","心髒病是指滲及心臟的各種疾病，包括冠心病、心臟衰竭、心臟律不整和心臟瓣膜病等。老人因年期、長的心負荷和其他健康問題的累積，更容易那上面心髒病變。這些疾病可能導致心臟供血不足、心臟損傷、心臟功能不全和心臟衰竭等症狀。常見的心臟疾病風險素包例如高血壓、高血脂、糖尿病、肥胖、吸煙、缺乏運動和家族病史等。預防心臟疾病的關鍵是保持健康的生活方式，包括均衡的飲食、適當程度的運動、戒菸、降低壓力，以及定期接受醫學檢驗檢查並按醫生的指導進行治療。"),
            ItemModel("慢性阻塞性肺病\n（Chronic Obstructive Pulmonary Disease，COPD）", "慢性阻塞性肺病是一種排除惡性化的肺部疾病，主要包括慢性阻塞性肺炎和肺空氣。老人因長期吸煙、長期暴露於空氣污染物質或工作環境中的有有害氣體等原因，更容易患慢性阻塞性肺病。這種疾病會導致氣道炎症、氣流受限、呼吸困難和咳嗽等症狀。慢性阻塞性肺病會對日常生活造成重大影響，並增加呼氣道感染和其他器官並發病的風險。治療COPD的目標是減輕症狀、改善腎臟功能、控制疾病進展並提供高生活質量。這包包括戒菸、使用支持氣管托張藥劑、進行關節功能鍛煉和隨從醫生的治療計劃。")
        )

        val adapter = KnowledgeRecyclerViewAdapter(this, itemList)
        recyclerViewKnowledge.adapter = adapter
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

}