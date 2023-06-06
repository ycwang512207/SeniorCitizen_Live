package tw.edu.pu.csim.ycwang.seniorcitizen_live.knowledge

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import tw.edu.pu.csim.ycwang.seniorcitizen_live.R

data class ItemModel(val title: String, val content: String)


class KnowledgeRecyclerViewAdapter(private val context: Context, private val itemList: List<ItemModel>) : RecyclerView.Adapter<KnowledgeRecyclerViewAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.knowledge_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]
        holder.txvTitle.text = item.title

        holder.itemView.setOnClickListener {
            openItemDetails(item)
        }
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txvTitle: TextView = itemView.findViewById(R.id.txvTitle)
    }

    private fun openItemDetails(item: ItemModel) {
        // 創建並顯示另一個 CardView 的對話框，將標題傳递给新的 CardView
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.knowledge_item_text)

        val titleTextView: TextView = dialog.findViewById(R.id.txvTextTitle)
        val contentTextView: TextView = dialog.findViewById(R.id.txvText)

        titleTextView.text = item.title
        contentTextView.text = item.content

        dialog.show()
    }
}