package com.aos.floney.presentation.home.calendar

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.aos.floney.R
import com.aos.floney.domain.entity.CalendarItem
import com.aos.floney.presentation.home.HomeViewModel
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.ZoneId
import java.util.Calendar
import java.util.Date
import java.util.Locale

class CalendarAdapter(private val viewModel: HomeViewModel) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val depositTextView: TextView = itemView.findViewById(R.id.depositTextView)
        val withdrawalTextView: TextView = itemView.findViewById(R.id.withdrawalTextView)
        init {
            itemView.setOnClickListener {
                // 클릭 이벤트 처리
                viewModel.clickSelectDate(dateTextView.text.toString().toInt())
                // 모달창 올라오는 이벤트
                dailyBottomSheet(itemView, dateTextView.text.toString().toInt())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_custom_calendar, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val currentDate = LocalDate.now()
        val formattedDate = SimpleDateFormat("yyyy.M.d", Locale.getDefault()).format(Date.from(currentDate.atStartOfDay(
            ZoneId.systemDefault()).toInstant()))



        val item = getItem(position)

        val parts = item.date.split(".")
        val dayOfMonth = parts.lastOrNull() ?: ""
        
        holder.dateTextView.text = dayOfMonth
        holder.depositTextView.text = item.depositAmount
        holder.withdrawalTextView.text = item.withdrawalAmount

        // 오늘 날짜 배경 처리
        if (item.date == formattedDate) {
            holder.dateTextView.setBackgroundResource(R.drawable.ellipse)
            // ContextCompat.getColor를 사용하여 색상 값 가져오기
            holder.dateTextView.setTextColor(ContextCompat.getColor(holder.dateTextView.context, R.color.white))
        }

        // 현재 월에 속하는 날짜만 보이도록 처리
        if (item.date=="") {
            holder.itemView.visibility = View.INVISIBLE
        } else {
            holder.itemView.visibility = View.VISIBLE
        }
    }

    override fun getItemCount(): Int {
        return viewModel.calendarItems.value?.size ?: 0
    }

    private fun getItem(position: Int): CalendarItem {
        return viewModel.calendarItems.value?.get(position) ?: CalendarItem("", "", "")
    }

    private fun showToast(message: String) {

    }
    private fun dailyBottomSheet(itemView: View, date : Int) {
        val bottomSheetPostFragment = BottomSheetFragment()
        bottomSheetPostFragment.show(
            (itemView.context as AppCompatActivity).supportFragmentManager,
            bottomSheetPostFragment.tag
        )
    }
}
