package com.aos.floney.presentation.home.daily

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aos.floney.databinding.ItemCustomDailyBinding
import com.aos.floney.domain.entity.DailyItemType
import com.aos.floney.domain.entity.GetbooksDaysData
import com.aos.floney.presentation.home.HomeViewModel
import com.aos.floney.util.view.ItemDiffCallback

class DailyAdapter(
    private val viewModel: HomeViewModel
) :
    ListAdapter<GetbooksDaysData.DailyItem, DailyAdapter.ViewHolder>(
        ItemDiffCallback<GetbooksDaysData.DailyItem>(
            onItemsTheSame = { old, new -> old == new },
            onContentsTheSame = { old, new -> old == new }
        )
    ){
    class ViewHolder(
        private val binding : ItemCustomDailyBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        val dailyProfile: ImageView = binding.dailyProfile
        val dailyContent: TextView = binding.dailyContent
        val dailyCategory: TextView = binding.dailyCategory
        val dailyMoney: TextView = binding.dailyMoney
        fun onBind(dailyItem: GetbooksDaysData.DailyItem) {

            /*if (dailyItem.img != "user_default")
                dailyProfile.setImageResource(dailyItem.img)
            */

            dailyContent.text = dailyItem.content

            if (dailyItem.category.isEmpty()) {
                dailyCategory.text = String.format("-")
                dailyMoney.text=dailyItem.money.toInt().toString()
            }
            else
            {
                dailyCategory.text = String.format(dailyItem.category[0]+" ‧ "+dailyItem.category[1])
                dailyMoney.text=getFormattedMoneyText(dailyItem.money, dailyItem.assetType == DailyItemType.INCOME)
                binding.root.setOnClickListener {
                }
            }
        }
        fun getFormattedMoneyText(money: Double, isIncome: Boolean): String {
            val sign = if (isIncome) "+" else "-"
            return "$sign${money.toInt()}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCustomDailyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dateItems = getItem(position)
        holder.onBind(dateItems)
    }
}
