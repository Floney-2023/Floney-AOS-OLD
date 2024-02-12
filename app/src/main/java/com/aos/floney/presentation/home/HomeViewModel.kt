package com.aos.floney.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aos.floney.domain.entity.CalendarItem
import com.aos.floney.domain.entity.DailyViewItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random

class HomeViewModel : ViewModel() {

    private val _calendarItems = MutableLiveData<List<CalendarItem>>()
    val calendarItems: LiveData<List<CalendarItem>> get() = _calendarItems

    private val _dailyItems = MutableStateFlow<List<DailyViewItem>>(mutableListOf<DailyViewItem>())
    val dailyItems: StateFlow<List<DailyViewItem>> get() = _dailyItems

    // calendar를 MutableStateFlow로 변경
    private val _calendar = MutableStateFlow<Calendar>(Calendar.getInstance())
    val calendar: StateFlow<Calendar> get() = _calendar

    init {
        _calendar.value = Calendar.getInstance()
        updateCalendarItems()
    }

    // ... (이후 코드는 동일)

    fun moveToPreviousMonth() {
        _calendar.value?.add(Calendar.MONTH, -1)
        updateCalendarItems()
    }

    fun moveToNextMonth() {
        _calendar.value?.add(Calendar.MONTH, 1)
        updateCalendarItems()
    }

    fun moveToPreviousDay() {
        _calendar.value?.add(Calendar.DATE, -1)
        updateDailyItems(_calendar.value?.time)
    }

    fun moveToNextDay() {
        _calendar.value?.add(Calendar.DATE, 1)
        updateDailyItems(_calendar.value?.time)
    }

    fun clickSelectDate(selectDay: Int) {

        viewModelScope.launch{
            _calendar.value?.set(Calendar.DATE, selectDay)
            _calendar.emit(_calendar.value)
            Log.d("selectDay", "Calendar items updated: ${_calendar.value?.time}")
            updateDailyItems(_calendar.value?.time)
        }

    }
    fun clickSelectYearMonth(selectYear : Int, selectMonth: Int) {

        viewModelScope.launch{
            _calendar.value?.set(Calendar.YEAR, selectYear)
            _calendar.value?.set(Calendar.MONTH, selectMonth)
            _calendar.emit(_calendar.value)
            Log.d("selectMonthYear", "Calendar items updated: ${_calendar.value?.time}")
            updateCalendarItems()
        }

    }

    private fun updateCalendarItems() {
        val itemList = mutableListOf<CalendarItem>()
        _calendar.value?.get(Calendar.MONTH) // 월에 대한 작업이 불필요한 경우 제거

        val firstDayOfMonth = _calendar.value?.clone() as Calendar
        firstDayOfMonth?.set(Calendar.DAY_OF_MONTH, 1)
        val first = firstDayOfMonth?.time
        adjustToStartOfWeek(firstDayOfMonth) // 주의 시작을 맞추기 위한 조정

        val lastDayOfMonth = _calendar.value?.clone() as Calendar
        lastDayOfMonth?.set(Calendar.DAY_OF_MONTH, lastDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH))
        val last = lastDayOfMonth?.time
        adjustToEndOfWeek(lastDayOfMonth) // 주의 끝을 맞추기 위한 조정

        val dateFormat = SimpleDateFormat("yyyy.M.d", Locale.getDefault())
        var currentDate = firstDayOfMonth?.time

        while (!currentDate?.after(lastDayOfMonth?.time)!!) {
            Log.d("CalendarFragment", "Calendar items updated: $currentDate")
            var date = dateFormat.format(currentDate)
            val isCurrentMonth = (currentDate >= first && currentDate <= last)

            if (!isCurrentMonth)
                date=""
            // 날짜에 따른 deposit, withdrawalAmount 받아오기
            itemList.add(CalendarItem(date, "", ""))

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

    // 날짜에 맞는 item 들고와야함, 임의로 설정
    private fun updateDailyItems(date: Date?) {

        val dailyItemList = mutableListOf<DailyViewItem>()

        for (i in 1..7){
            val randomDailyItem = DailyViewItem(
                id = 1,
                money = Random().nextInt(10000),
                img = null,
                category = "식비",
                assetType = "체크카드",
                content = "멜론",
                exceptStatus = false,
                userNickName = "도비"
            )

            dailyItemList.add(randomDailyItem)
        }


        Log.d("selectDay", "Calendar items updated: $dailyItemList")

        _dailyItems.value = dailyItemList
    }
    // 내역 추가 버튼
    fun postButtonClick(){

    }
}