package com.aos.floney.presentation.home.daily

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.aos.floney.R
import com.aos.floney.domain.entity.DailyViewItem

class DailyAdapter(private val viewModel: DailyViewModel) :
    RecyclerView.Adapter<DailyAdapter.ViewHolder>() {
    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val dailyProfile: ImageView = itemView.findViewById(R.id.daily_profile)
        val dailyContent: TextView = itemView.findViewById(R.id.daily_content)
        val dailyCategory: TextView = itemView.findViewById(R.id.daily_category)
        val dailyMoney: TextView = itemView.findViewById(R.id.daily_money)
        init {
            itemView.setOnClickListener {
                // 클릭 이벤트 처리
                showToast("Clicked on ${dailyMoney}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_custom_daily, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)

        if (item.img != null) {
            // holder.dailyProfile.setImageResource(item.dailyProfile)
        }

        holder.dailyContent.text = item.content
        holder.dailyCategory.text = String.format(item.category+" ‧ "+item.assetType)
        holder.dailyMoney.text=item.money.toString()

        holder.itemView.setOnClickListener {

        }
    }

    override fun getItemCount(): Int {
        return viewModel.dailyItems.value?.size ?: 0
    }

    private fun getItem(position: Int): DailyViewItem {
        return viewModel.dailyItems.value?.get(position) ?: DailyViewItem(
            1,
            2000,
            null,
            "체크카드",
            "식비",
            "멜론",
            false,
            "도비")

    }

    private fun showToast(message: String) {

    }
}
