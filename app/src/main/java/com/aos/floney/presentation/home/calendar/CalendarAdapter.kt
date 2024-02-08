package com.aos.floney.presentation.home.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aos.floney.R
import com.aos.floney.domain.entity.CalendarItem

class CalendarAdapter(private val viewModel: CalendarViewModel) :
    RecyclerView.Adapter<CalendarAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val dateTextView: TextView = itemView.findViewById(R.id.dateTextView)
        val depositTextView: TextView = itemView.findViewById(R.id.depositTextView)
        val withdrawalTextView: TextView = itemView.findViewById(R.id.withdrawalTextView)
        init {
            itemView.setOnClickListener {
                // 클릭 이벤트 처리
                showToast("Clicked on ${dateTextView}")
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_custom_calendar, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.dateTextView.text = item.date
        holder.depositTextView.text = item.depositAmount
        holder.withdrawalTextView.text = item.withdrawalAmount

        // 현재 월에 속하는 날짜만 보이도록 처리
        if (item.date!="") {
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.setOnClickListener {
                showToast("Clicked on ${item.date}")
            }
        } else {
            // 현재 월에 속하지 않는 날짜는 감춤
            holder.itemView.visibility = View.INVISIBLE
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
}
