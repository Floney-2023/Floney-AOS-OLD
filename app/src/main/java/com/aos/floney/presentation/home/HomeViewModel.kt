package com.aos.floney.presentation.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aos.floney.domain.entity.CalendarItem
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import java.util.Random

class HomeViewModel : ViewModel() {
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

        val firstDayOfMonth = calendar.clone() as Calendar
        firstDayOfMonth.set(Calendar.DAY_OF_MONTH, 1)
        val lastDayOfMonth = calendar.clone() as Calendar
        lastDayOfMonth.set(Calendar.DAY_OF_MONTH, lastDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH))

        val dateFormat = SimpleDateFormat("dd", Locale.getDefault())
        var currentDate = firstDayOfMonth.time

        // 받아오는 부분(우선 랜덤값으로 설정)
        while (!currentDate.after(lastDayOfMonth.time)) {
            val date = dateFormat.format(currentDate)
            val depositAmount = "+${Random().nextInt(10000)}"
            val withdrawalAmount = "-${Random().nextInt(5000)}"
            itemList.add(CalendarItem(date, depositAmount, withdrawalAmount))

            val nextDate = Calendar.getInstance()
            nextDate.time = currentDate
            nextDate.add(Calendar.DATE, 1)
            currentDate = nextDate.time
        }

        _calendarItems.value = itemList
    }
    fun postButtonClick(){

    }
}