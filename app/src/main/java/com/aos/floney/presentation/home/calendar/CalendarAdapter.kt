package com.aos.floney.presentation.home.calendar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.RecyclerView
import com.aos.floney.R
import com.aos.floney.domain.entity.CalendarItem
import com.aos.floney.presentation.home.HomeViewModel
import com.google.android.material.bottomappbar.BottomAppBarTopEdgeTreatment
import java.util.Calendar

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
        val item = getItem(position)
        holder.dateTextView.text = item.date
        holder.depositTextView.text = item.depositAmount
        holder.withdrawalTextView.text = item.withdrawalAmount

        // 현재 월에 속하는 날짜만 보이도록 처리
        if (item.date!="") {
            holder.itemView.visibility = View.VISIBLE

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
    private fun dailyBottomSheet(itemView: View, date : Int) {
        val bottomSheetPostFragment = BottomSheetFragment()
        bottomSheetPostFragment.show(
            (itemView.context as AppCompatActivity).supportFragmentManager,
            bottomSheetPostFragment.tag
        )
    }
}
