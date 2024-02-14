package com.aos.floney.presentation.home.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aos.floney.R
import com.aos.floney.databinding.ItemCustomCalendarBinding
import com.aos.floney.domain.entity.CalendarItem
import com.aos.floney.domain.entity.CalendarItemType
import com.aos.floney.presentation.home.HomeViewModel
import com.aos.floney.util.view.ItemDiffCallback
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

class CalendarAdapter(
    private val currMonth: Int,
    private val plantHistories: List<CalendarItem>,
    private val viewModel: HomeViewModel
) : ListAdapter<CalendarItem, CalendarAdapter.ViewHolder>(
    ItemDiffCallback<CalendarItem>(
        onItemsTheSame = { old, new -> old == new },
        onContentsTheSame = { old, new -> old == new }
    )
){
    class ViewHolder(
        private val binding : ItemCustomCalendarBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val dayText: TextView = binding.dateTextView
        private val incomeText: TextView = binding.withdrawalTextView
        private val outcomeText: TextView = binding.depositTextView

        fun onBind(item: CalendarItem) {

            val currentDate = LocalDate.now()
            val formattedDate = SimpleDateFormat("yyyy.M.d", Locale.getDefault()).format(Date.from(currentDate.atStartOfDay(
                ZoneId.systemDefault()).toInstant()))


            val parts = item.date.split("-")
            val dayOfMonth = parts.lastOrNull() ?: ""

            dayText.text = dayOfMonth
            incomeText.text = getFormattedMoneyText(item.money, item.assetType == CalendarItemType.INCOME)
            outcomeText.text =  getFormattedMoneyText(item.money, item.assetType == CalendarItemType.OUTCOME)

            // 오늘 날짜 배경 처리
            if (item.date == formattedDate) {
                dayText.setBackgroundResource(R.drawable.ellipse)
                // ContextCompat.getColor를 사용하여 색상 값 가져오기
                dayText.setTextColor(ContextCompat.getColor(dayText.context, R.color.white))
            }

            // 현재 월에 속하는 날짜만 보이도록 처리
            if (item.date=="") {
                binding.root.visibility = View.INVISIBLE
            } else {
                binding.root.visibility = View.VISIBLE
            }

            // Set up click listener
            binding.root.setOnClickListener {
                dailyBottomSheet(binding, item.date)
            }
        }
        fun dailyBottomSheet(binding: ItemCustomCalendarBinding, date : String) {
            val bottomSheetPostFragment = BottomSheetFragment()
            bottomSheetPostFragment.show(
                (binding.root.context as AppCompatActivity).supportFragmentManager,
                bottomSheetPostFragment.tag
            )
        }
        fun getFormattedMoneyText(money: Int, isIncome: Boolean): String {
            val sign = if (isIncome) "+" else "-"
            return "$sign$money"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCustomCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }


}
