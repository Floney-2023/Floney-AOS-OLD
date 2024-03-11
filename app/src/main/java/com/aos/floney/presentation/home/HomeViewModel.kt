package com.aos.floney.presentation.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aos.floney.domain.entity.GetbooksDaysData
import com.aos.floney.domain.entity.GetbooksInfoData
import com.aos.floney.domain.entity.GetbooksMonthData
import com.aos.floney.domain.entity.books.GetbooksUsersCheckData
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


    val _bookKey = MutableStateFlow<String>("")
    val bookKey: MutableStateFlow<String> get() = _bookKey

    // calendar를 MutableStateFlow로 변경.
    private val _calendar = MutableStateFlow<Calendar>(Calendar.getInstance())
    val calendar: MutableStateFlow<Calendar> get() = _calendar

    private val _getUsersCheckState =
        MutableStateFlow<UiState<GetbooksUsersCheckData>>(UiState.Loading)
    val getUsersCheckState: StateFlow<UiState<GetbooksUsersCheckData>> =
        _getUsersCheckState.asStateFlow()

    private val _getCalendarInformationState =
        MutableStateFlow<UiState<GetbooksMonthData>>(UiState.Loading)
    val getCalendarInformationState: StateFlow<UiState<GetbooksMonthData>> =
        _getCalendarInformationState.asStateFlow()

    private val _getDailyInformationState =
        MutableStateFlow<UiState<GetbooksDaysData>>(UiState.Loading)
    val getDailyInformationState: StateFlow<UiState<GetbooksDaysData>> =
        _getDailyInformationState.asStateFlow()

    private val _getbooksInformationState =
        MutableStateFlow<UiState<GetbooksInfoData>>(UiState.Loading)
    val getbooksInformationState: StateFlow<UiState<GetbooksInfoData>> =
        _getbooksInformationState.asStateFlow()


    private val _postRegisterCalendarState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val postRegisterCalendarState: StateFlow<UiState<Unit>> = _postRegisterCalendarState.asStateFlow()


    init {
        _calendar.value = Calendar.getInstance()
        updateBookKeyItems()
        //updateCalendarItems()
        updatebooksInfoItems()
        //updateDailyItems(_calendar.value?.time)
    }

    fun moveToPreviousMonth() {
        viewModelScope.launch{
            val calendarInstance = _calendar.value
            calendarInstance.add(Calendar.MONTH, -1)
            updateCalendarItems()
            _calendar.emit(calendarInstance)
        }
    }

    fun moveToNextMonth() {
        viewModelScope.launch{
            val calendarInstance = _calendar.value
            calendarInstance.add(Calendar.MONTH, 1)
            updateCalendarItems()
            _calendar.emit(calendarInstance)
        }
    }

    fun moveToPreviousDay() {
        viewModelScope.launch{
            val calendarInstance = _calendar.value
            calendarInstance.add(Calendar.DATE, -1)
            _calendar.emit(calendarInstance)
            updateDailyItems(_calendar.value?.time)
        }
    }

    fun moveToNextDay() {

        viewModelScope.launch{
            val calendarInstance = _calendar.value
            calendarInstance.add(Calendar.DATE, 1)
            _calendar.emit(calendarInstance)
            updateDailyItems(_calendar.value?.time)
        }

    }

    fun clickSelectDate(selectDay: Date) {

        viewModelScope.launch{
            val calendarInstance = _calendar.value ?: Calendar.getInstance()
            calendarInstance.time = selectDay
            _calendar.emit(calendarInstance)
            Log.d("selectDay", "Calendar items updated: ${_calendar.value?.time}")
            updateDailyItems(_calendar.value?.time)
        }

    }
    fun clickSelectYearMonth(selectYear : Int, selectMonth: Int) {

        viewModelScope.launch{
            val calendarInstance = _calendar.value
            calendarInstance?.set(Calendar.YEAR, selectYear)
            calendarInstance?.set(Calendar.MONTH, selectMonth)
            _calendar.emit(calendarInstance)
            Log.d("selectMonthYear", "Calendar items updated: ${_calendar.value?.time}")
            updateCalendarItems()
        }

    }

    fun updateCalendarItems() {
        val firstDayOfMonth = _calendar.value?.clone() as Calendar
        firstDayOfMonth?.set(Calendar.DAY_OF_MONTH, 1)
        val firstDayFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // 날짜에 따른 deposit, withdrawalAmount 받아오기(bookKey 예시)
        viewModelScope.launch {
            calendarRepository.getbooksMonthData(Authorization, bookKey.value, firstDayFormat.format(firstDayOfMonth.time))
                .onSuccess { response ->
                    _getCalendarInformationState.value =
                            UiState.Success(response)
                        Log.d("selectDay", "onsuccess: $response")
                }.onFailure { t ->
                    Log.d("selectDay", "onfailure: ${t}")
                    _getCalendarInformationState.value = UiState.Failure("${t.message}")
                }
        }
    }
    fun updateCalendarDayList(currYear: Int, currMonth: Int): Int {
        _calendar.value?.get(Calendar.MONTH) // 월에 대한 작업이 불필요한 경우 제거

        val firstDayOfMonth = _calendar.value?.clone() as Calendar
        firstDayOfMonth?.set(Calendar.DAY_OF_MONTH, 1)
        return adjustToStartOfWeek(firstDayOfMonth) // 오는 날로 주의 시작까지 count
    }

    fun updateBookKeyItems(){
        viewModelScope.launch {
            calendarRepository.getbooksUsersCheckData(Authorization)
                .onSuccess { response ->
                    _getUsersCheckState.value =
                        UiState.Success(response)
                    Log.d("bookKey", "onsuccess: $response")
                }.onFailure { t ->
                    Log.d("bookKey", "onfailure: ${t}")
                    _getUsersCheckState.value = UiState.Failure("${t.message}")
                }
        }
    }

    // 첫째 날이 포함된 주의 첫째 날로 조정
    private fun adjustToStartOfWeek(calendar: Calendar):Int {
        var count = 0
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY) {
            calendar.add(Calendar.DATE, -1)
            count+=1
        }
        return count
    }

    // 마지막 날이 포함된 주의 마지막 날로 조정
    private fun adjustToEndOfWeek(calendar: Calendar) {
        while (calendar.get(Calendar.DAY_OF_WEEK) != Calendar.SATURDAY) {
            calendar.add(Calendar.DATE, 1)
        }
    }

    // 날짜에 맞는 item 들고와야함, 임의로 설정
    fun updateDailyItems(date: Date?) {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())

        // 날짜에 따른 deposit, withdrawalAmount 받아오기(bookKey 예시)
        viewModelScope.launch {
            calendarRepository.getbooksDaysData(Authorization, bookKey.value, dateFormat.format(date))
                .onSuccess { response ->
                    if (response != null) {
                        _getDailyInformationState.value = UiState.Success(response)
                    }
                }.onFailure { t ->
                    Log.d("selectDaily", "onfailure: ${t}")
                    _getDailyInformationState.value = UiState.Failure("${t.message}")
                }
        }

    }
    fun updatebooksInfoItems() {
        Log.d("selectDay", "updatebooksInfo")

        viewModelScope.launch {
            calendarRepository.getbooksInfoData(Authorization, bookKey.value)
                .onSuccess { response ->
                    if (response != null) {
                        _getbooksInformationState.value = UiState.Success(response)
                    }
                }.onFailure { t ->
                    Log.d("selectDay", "onfailure: ${t}")
                    _getbooksInformationState.value = UiState.Failure("${t.message}")
                }
        }

    }

    fun updateBookKey(bookKey : String){
        _bookKey.value=bookKey
    }

    // 내역 추가 버튼
    fun postButtonClick(){

    }

    companion object {
        private const val FIRST_DAY = 1
    }
}