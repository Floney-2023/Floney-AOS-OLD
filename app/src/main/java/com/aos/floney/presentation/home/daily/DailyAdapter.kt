package com.aos.floney.presentation.home.daily

import android.view.LayoutInflater
import android.view.View
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
        val dailyRepeatDuration : TextView = binding.dailyRepeatDuration
        fun onBind(dailyItem: GetbooksDaysData.DailyItem) {

            /*if (dailyItem.img != "user_default")
                dailyProfile.setImageResource(dailyItem.img)
            */

            dailyContent.text = dailyItem.description

            if (dailyItem.writerEmail.isEmpty()) {
                dailyCategory.text = String.format("-")
                dailyMoney.text=dailyItem.money.toInt().toString()
                dailyRepeatDuration.visibility = View.GONE
            }
            else
            {
                dailyCategory.text = String.format(dailyItem.lineSubCategory+" ‧ "+dailyItem.assetSubCategory)
                dailyMoney.text=getFormattedMoneyText(dailyItem.money, dailyItem.lineCategory == DailyItemType.INCOME)

                val repeatText = when (dailyItem.repeatDuration) {
                    "EVERYDAY" -> "매일"
                    "WEEK" -> "매주"
                    "MONTH" -> "매달"
                    "WEEKDAY" -> "주중"
                    "WEEKEND" -> "주말"
                    else -> dailyItem.repeatDuration // 그 외의 경우는 그대로 사용
                }
                dailyRepeatDuration.text = repeatText

                // "NONE"인 경우에만 가시성을 GONE으로 설정
                if (dailyItem.repeatDuration == "NONE") {
                    dailyRepeatDuration.visibility = View.GONE
                } else {
                    dailyRepeatDuration.visibility = View.VISIBLE
                }

                binding.root.setOnClickListener {
                    // 일별 내역 자세하게 보여지기
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
