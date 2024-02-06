package com.aos.floney.presentation.home.calendar

import android.util.Log
import androidx.lifecycle.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.aos.floney.domain.entity.CalendarItem
import java.text.SimpleDateFormat
import java.util.*

class CalendarViewModel : ViewModel() {
    private val _calendarItems = MutableLiveData<List<CalendarItem>>()
    val calendarItems: LiveData<List<CalendarItem>> get() = _calendarItems

    val calendar = Calendar.getInstance()

    init {
        updateCalendarItems()
    }

    fun moveToPreviousMonth() {
        calendar.add(Calendar.MONTH, -1)
        updateCalendarItems()
    }

    fun moveToNextMonth() {
        calendar.add(Calendar.MONTH, 1)
        updateCalendarItems()
    }

    private fun updateCalendarItems() {
        val itemList = mutableListOf<CalendarItem>()
        calendar.get(Calendar.MONTH) + 1

        val firstDayOfMonth = calendar.clone() as Calendar
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)
        val first = firstDayOfMonth.time
        adjustToStartOfWeek(firstDayOfMonth) // 주의 시작을 맞추기 위한 조정

        val lastDayOfMonth = calendar.clone() as Calendar
        lastDayOfMonth.set(Calendar.DAY_OF_MONTH, lastDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH))
        val last = lastDayOfMonth.time
        adjustToEndOfWeek(lastDayOfMonth) // 주의 끝을 맞추기 위한 조정

        val dateFormat = SimpleDateFormat("d", Locale.getDefault())
        var currentDate = firstDayOfMonth.time

        while (!currentDate.after(lastDayOfMonth.time)) {
            Log.d("CalendarFragment", "Calendar items updated: $currentDate")
            val date = dateFormat.format(currentDate)
            val isCurrentMonth = (currentDate >= first && currentDate <= last)
            itemList.add(CalendarItem(date, "", "", isCurrentMonth))

            //val depositAmount = "+${Random().nextInt(10000)}"
            //val withdrawalAmount = "${Random().nextInt(5000)}"

            val nextDate = Calendar.getInstance()
            nextDate.time = currentDate
            nextDate.add(Calendar.DATE, 1)
            currentDate = nextDate.time
        }

        _calendarItems.value = itemList

    }

    // 첫째 날이 포함된 주의 첫째 날로 조정
    private fun adjustToStartOfWeek(calendar: Calendar) {
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -1)
        }
    }

    // 마지막 날이 포함된 주의 마지막 날로 조정
    private fun adjustToEndOfWeek(calendar: Calendar) {
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 1)
        }
    }
}