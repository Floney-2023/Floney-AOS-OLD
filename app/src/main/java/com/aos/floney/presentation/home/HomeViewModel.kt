package com.aos.floney.presentation.home

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aos.floney.domain.entity.CalendarItem
import com.aos.floney.domain.entity.DailyItem
import com.aos.floney.domain.entity.DailyViewItem
import com.aos.floney.domain.repository.CalendarRepository
import com.aos.floney.util.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val calendarRepository: CalendarRepository
) : ViewModel() {

    val Authorization = "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ3bnNnbWw1MTdAZ21haWwuY29tIiwiaWF0IjoxNzA4MTgzMDA5LCJleHAiOjE3MDgxODY2MDl9.7jkR32iDUZCHA9vF5Vrthj5FW3wPoJEWhYLns8Q007IBrmCmsngf2KfOGk4MSBSO"
    val bookKey = "4B1F8081"

    private val _calendarItems = MutableLiveData<List<CalendarItem>>()
    val calendarItems: LiveData<List<CalendarItem>> get() = _calendarItems

    private val _dailyItems = MutableStateFlow<List<DailyViewItem>>(mutableListOf<DailyViewItem>())
    val dailyItems: StateFlow<List<DailyViewItem>> get() = _dailyItems

    // calendar를 MutableStateFlow로 변경
    private val _calendar = MutableStateFlow<Calendar>(Calendar.getInstance())
    val calendar: StateFlow<Calendar> get() = _calendar

    private val _getCalendarInformationState =
        MutableStateFlow<UiState<List<CalendarItem>>>(UiState.Loading)
    val getCalendarInformationState: StateFlow<UiState<List<CalendarItem>>> =
        _getCalendarInformationState.asStateFlow()

    private val _getDailyInformationState =
        MutableStateFlow<UiState<List<DailyItem>>>(UiState.Loading)
    val getDailyInformationState: StateFlow<UiState<List<DailyItem>>> =
        _getDailyInformationState.asStateFlow()


    private val _postRegisterCalendarState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val postRegisterCalendarState: StateFlow<UiState<Unit>> = _postRegisterCalendarState.asStateFlow()


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


        val firstDayOfMonth = _calendar.value?.clone() as Calendar
        firstDayOfMonth?.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // 날짜에 따른 deposit, withdrawalAmount 받아오기(bookKey 예시)
        viewModelScope.launch {
            calendarRepository.getbooksMonthData(Authorization, bookKey, firstDayFormat.format(firstDayOfMonth.time))
                .onSuccess { response ->
                    if (response != null) {
                        _getCalendarInformationState.value =
                            UiState.Success(response)
                        Log.d("selectDay", "onsuccess: $response")
                    } else {
                        _getCalendarInformationState.value = UiState.Success(emptyList())
                    }
                }.onFailure { t ->
                    Log.d("selectDay", "onfailure: ${t}")
                    _getCalendarInformationState.value = UiState.Failure("${t.message}")
                }
        }
    }
    fun updateCalendarDayList(currYear: Int, currMonth: Int): MutableList<Date> {
        /*calendar.value.set(Calendar.YEAR, currYear)
        calendar.value.set(Calendar.MONTH, currMonth)
        calendar.value.set(Calendar.DAY_OF_MONTH, FIRST_DAY)

        val dayList: MutableList<Date> = mutableListOf()

        for (i in 0..Calendar.WEEK_OF_MONTH) {
            for (k in 0..Calendar.DAY_OF_YEAR) {
                calendar.value.add(
                    Calendar.DAY_OF_MONTH,
                    (FIRST_DAY - calendar.value.get(Calendar.DAY_OF_WEEK)) + k
                )
                dayList.add(calendar.value.time.clone() as Date)
            }
            calendar.value.add(Calendar.WEEK_OF_MONTH, FIRST_DAY)
        }
        Log.d("CalendarFragment", "Calendar items updated: $dayList")*/
        val dayList = mutableListOf<Date>()
        _calendar.value?.get(Calendar.MONTH) // 월에 대한 작업이 불필요한 경우 제거

        val firstDayOfMonth = _calendar.value?.clone() as Calendar
        firstDayOfMonth?.set(Calendar.DAY_OF_MONTH, 1)
        val first = firstDayOfMonth?.time
        adjustToStartOfWeek(firstDayOfMonth) // 주의 시작을 맞추기 위한 조정

        val lastDayOfMonth = _calendar.value?.clone() as Calendar
        lastDayOfMonth?.set(Calendar.DAY_OF_MONTH, lastDayOfMonth.getActualMaximum(Calendar.DAY_OF_MONTH))
        val last = lastDayOfMonth?.time
        adjustToEndOfWeek(lastDayOfMonth) // 주의 끝을 맞추기 위한 조정

        val dateFormat = SimpleDateFormat("yyyy-M-d", Locale.getDefault())
        var currentDate = firstDayOfMonth?.time

        Log.d("selectDay", "updateCalendarDayList?:")
        while (!currentDate?.after(lastDayOfMonth?.time)!!) {
            Log.d("CalendarFragment", "Calendar items updated: $currentDate")
            var date = dateFormat.format(currentDate)
            val isCurrentMonth = (currentDate >= first && currentDate <= last)

            dayList.add(currentDate)
            val nextDate = Calendar.getInstance()
            nextDate.time = currentDate
            nextDate.add(Calendar.DATE, 1)
            currentDate = nextDate.time
        }
        return dayList
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
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // 날짜에 따른 deposit, withdrawalAmount 받아오기(bookKey 예시)
        viewModelScope.launch {
            calendarRepository.getbooksDaysData(Authorization, bookKey, dateFormat.format(date))
                .onSuccess { response ->
                    if (response != null) {
                        _getDailyInformationState.value =
                            UiState.Success(response)
                        Log.d("selectDaily", "onsuccess: $response")
                    } else {
                        _getDailyInformationState.value = UiState.Success(emptyList())
                    }
                }.onFailure { t ->
                    Log.d("selectDaily", "onfailure: ${t}")
                    _getDailyInformationState.value = UiState.Failure("${t.message}")
                }
        }
        /*for (i in 1..7){
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

        _dailyItems.value = dailyItemList*/
    }
    // 내역 추가 버튼
    fun postButtonClick(){

    }

    companion object {
        private const val FIRST_DAY = 1
    }
}