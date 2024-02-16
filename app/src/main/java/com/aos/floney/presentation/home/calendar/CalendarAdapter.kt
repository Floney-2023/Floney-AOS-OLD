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
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarAdapter(
    private val currMonth: Int,
    private val calendarItems: List<CalendarItem>,
    private val viewModel: HomeViewModel
) : ListAdapter<Date, CalendarAdapter.ViewHolder>(
    ItemDiffCallback<Date>(
        onItemsTheSame = { old, new -> old == new },
        onContentsTheSame = { old, new -> old == new }
    )
){
    class ViewHolder(
        private val binding : ItemCustomCalendarBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        var currMonth: Int = 0
        private val dayText: TextView = binding.dateTextView
        private val incomeText: TextView = binding.withdrawalTextView
        private val outcomeText: TextView = binding.depositTextView

        fun onBind(date: Date, calendarItems: List<CalendarItem>) {

            val dateFormat = SimpleDateFormat("d", Locale.getDefault())
            val fullDateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

            val currentDate = LocalDate.now()
            val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date.from(currentDate.atStartOfDay(
                ZoneId.systemDefault()).toInstant()))





            // 오늘 날짜 배경 처리
            if (fullDateFormat.format(date) == formattedDate) {
                dayText.setBackgroundResource(R.drawable.ellipse)
                // ContextCompat.getColor를 사용하여 색상 값 가져오기
                dayText.setTextColor(ContextCompat.getColor(dayText.context, R.color.white))
            }


            // 현재 월에 속하는 날짜만 보이도록 처리
            if (date.month != currMonth) {
                binding.root.visibility = View.INVISIBLE
            } else {
                val day = dateFormat.format(date)
                val index = (day.toInt()-1)*2

                val recordItems = calendarItems.groupBy { it.date.takeLast(2) }

                dayText.text = day
                incomeText.text = getFormattedMoneyText(calendarItems[index].money, calendarItems[index].assetType == CalendarItemType.INCOME)
                outcomeText.text =  getFormattedMoneyText(calendarItems[index+1].money, calendarItems[index+1].assetType == CalendarItemType.INCOME)

                if (incomeText.text.toString().toDouble()==0.0)
                    incomeText.visibility = View.GONE
                if (outcomeText.text.toString().toDouble()==0.0)
                    outcomeText.visibility = View.GONE

                binding.root.visibility = View.VISIBLE
            }

            // Set up click listener
            binding.root.setOnClickListener {
                dailyBottomSheet(binding, date)
            }
        }
        fun dailyBottomSheet(binding: ItemCustomCalendarBinding, dailyDate : Date) {
            val bottomSheetPostFragment = BottomSheetFragment()
            bottomSheetPostFragment.show(
                (binding.root.context as AppCompatActivity).supportFragmentManager,
                bottomSheetPostFragment.tag
            )
        }
        fun getFormattedMoneyText(money: Double, isIncome: Boolean): String {
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
        holder.currMonth = currMonth
        val date = getItem(position)
        holder.onBind(date, calendarItems)
    }


}
