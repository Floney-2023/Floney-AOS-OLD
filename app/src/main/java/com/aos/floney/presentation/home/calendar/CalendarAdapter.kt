package com.aos.floney.presentation.home.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aos.floney.R
import com.aos.floney.databinding.ItemCustomCalendarBinding
import com.aos.floney.domain.entity.CalendarData
import com.aos.floney.domain.entity.GetbooksMonthData
import com.aos.floney.presentation.home.HomeViewModel
import com.aos.floney.util.view.ItemDiffCallback
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Date
import java.util.Locale

class CalendarAdapter(
    private val currMonth: Int,
    private val onDateClick: (Date) -> Unit
) : ListAdapter<CalendarData, CalendarAdapter.ViewHolder>(
    ItemDiffCallback<CalendarData>(
        onItemsTheSame = { old, new -> old == new },
        onContentsTheSame = { old, new -> old == new }
    )
){
    class ViewHolder(
        private val binding : ItemCustomCalendarBinding,
        private val onDateClick: (Date) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        var currMonth: Int = 0
        private val dayText: TextView = binding.dateTextView
        private val incomeText: TextView = binding.withdrawalTextView
        private val outcomeText: TextView = binding.depositTextView

        fun onBind(
            item : CalendarData
        ) {

            val dateFormat = SimpleDateFormat("d", Locale.getDefault())

            // 현재 월에 속하는 날짜만 보이도록 처리
            if (item.income.date=="0") {
                binding.root.visibility = View.INVISIBLE
            } else {
                val date = convertStringToDate(item.income.date)
                // 오늘 날짜 배경 처리
                if (isToday(date)) {
                    dayText.setBackgroundResource(R.drawable.ellipse)
                    // ContextCompat.getColor를 사용하여 색상 값 가져오기
                    dayText.setTextColor(ContextCompat.getColor(dayText.context, R.color.white))
                }
                val day = dateFormat.format(date)
                val index = day.toInt()-1
                dayText.text = day
                incomeText.text = String.format("+${item.income.money.toInt()}")
                outcomeText.text = String.format("-${item.outcome.money.toInt()}")




                if (incomeText.text.toString().toDouble()==0.0)
                    incomeText.visibility = View.GONE
                if (outcomeText.text.toString().toDouble()==0.0)
                    outcomeText.visibility = View.GONE

                binding.root.visibility = View.VISIBLE
                // Set up click listener
                binding.root.setOnClickListener {
                    if (date != null) {
                        onDateClick(date)
                    }
                }
            }
        }
        fun isToday(date: Date?): Boolean {
            val currentDate = LocalDate.now()
            val formattedDate = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
                Date.from(currentDate.atStartOfDay(ZoneId.systemDefault()).toInstant())
            )
            return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(date) == formattedDate
        }
        fun convertStringToDate(dateString: String, pattern: String = "yyyy-MM-dd"): Date? {
            val dateFormatter = SimpleDateFormat(pattern, Locale.getDefault())
            return try {
                dateFormatter.parse(dateString)
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemCustomCalendarBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onDateClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.currMonth = currMonth
        val calendarItem = getItem(position)
        holder.onBind(calendarItem)
    }

}
