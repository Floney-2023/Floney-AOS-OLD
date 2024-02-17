package com.aos.floney.presentation.home.daily

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aos.floney.R
import com.aos.floney.databinding.ItemCustomCalendarBinding
import com.aos.floney.databinding.ItemCustomDailyBinding
import com.aos.floney.domain.entity.CalendarItem
import com.aos.floney.domain.entity.CalendarItemType
import com.aos.floney.domain.entity.DailyItem
import com.aos.floney.domain.entity.DailyViewItem
import com.aos.floney.presentation.home.HomeViewModel
import com.aos.floney.presentation.home.calendar.CalendarAdapter
import com.aos.floney.presentation.home.calendar.CalendarViewModel
import com.aos.floney.util.view.ItemDiffCallback
import java.util.Date

class DailyAdapter(
    private val viewModel: HomeViewModel) :
    ListAdapter<DailyItem, DailyAdapter.ViewHolder>(
        ItemDiffCallback<DailyItem>(
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
        fun onBind(dailyItem: DailyItem) {

            /*if (dailyItem.img != "user_default")
                dailyProfile.setImageResource(dailyItem.img)
            */
            dailyContent.text = dailyItem.content
            dailyCategory.text = String.format(dailyItem.category[0]+" ‧ "+dailyItem.category[1])
            dailyMoney.text=getFormattedMoneyText(dailyItem.money, dailyItem.assetType == CalendarItemType.INCOME)

            binding.root.setOnClickListener {

            }
        }
        fun getFormattedMoneyText(money: Double, isIncome: Boolean): String {
            val sign = if (isIncome) "+" else "-"
            return "$sign$money"
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
