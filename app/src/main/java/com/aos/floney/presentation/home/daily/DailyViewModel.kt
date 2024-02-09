package com.aos.floney.presentation.home.daily

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aos.floney.domain.entity.CarryOverInfo
import com.aos.floney.domain.entity.DayLinesResponse
import com.aos.floney.domain.entity.DayLinesResults
import com.aos.floney.domain.entity.DayTotalExpenses
import java.text.SimpleDateFormat
import java.util.*

class DailyViewModel : ViewModel() {
    private val _dailyItems = MutableLiveData<List<DayLinesResults>>()
    val dailyItems: LiveData<List<DayLinesResults>> get() = _dailyItems

    private val _selectedDate = MutableLiveData<Date>()
    val selectedDate: LiveData<Date> get() = _selectedDate

    fun updateSelectedDate(date: Date) {
        _selectedDate.value = date
        updateDailyItems(date)
    }

    private fun updateDailyItems(date: Date) {
        // Perform logic to fetch daily items for the specified date
        val itemList = mutableListOf<DayLinesResults>()

        // Example: Fetch daily items from a repository or perform some other logic
        // val dailyItems = repository.getDailyItemsForDate(date)

        // For demonstration purposes, let's assume you have a method getDailyItemsForDate
        // that returns a DayLinesResponse for a given date.
        // Replace this with your actual logic.

        // Replace the following line with actual data retrieval
        val exampleDayLinesResponse = DayLinesResponse(
            dayLinesResponse = listOf(
                DayLinesResults(
                    id = 1,
                    money = 100.0,
                    img = "@drawable/ex.png",
                    category = listOf("Food", "Groceries"),
                    assetType = "Cash",
                    content = "Example expense",
                    exceptStatus = false,
                    userNickName = "John"
                ),
                // Add more example DayLinesResults as needed
            ),
            totalExpense = listOf(
                DayTotalExpenses(
                    money = 150.0,
                    assetType = "Credit Card"
                ),
                // Add more example DayTotalExpenses as needed
            ),
            carryOverInfo = CarryOverInfo(
                carryOverStatus = true,
                carryOverMoney = 50.0
            ),
            seeProfileImg = true
        )

        itemList.addAll(exampleDayLinesResponse.dayLinesResponse.filterNotNull())

        _dailyItems.value = itemList
    }
}
