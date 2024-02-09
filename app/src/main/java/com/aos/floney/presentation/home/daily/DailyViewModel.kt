package com.aos.floney.presentation.home.daily

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.aos.floney.domain.entity.CarryOverInfo
import com.aos.floney.domain.entity.DailyViewItem
import com.aos.floney.domain.entity.DayLinesResponse
import com.aos.floney.domain.entity.DayLinesResults
import com.aos.floney.domain.entity.DayTotalExpenses
import java.text.SimpleDateFormat
import java.util.*

class DailyViewModel : ViewModel() {
    private val _dailyItems = MutableLiveData<List<DailyViewItem>>()
    val dailyItems: LiveData<List<DailyViewItem>> get() = _dailyItems

    private val _selectedDate = MutableLiveData<Date>()
    val selectedDate: LiveData<Date> get() = _selectedDate

    fun updateSelectedDate(date: Date) {
        _selectedDate.value = date
        updateDailyItems(date)
    }

    private fun updateDailyItems(date: Date) {
        val itemList = mutableListOf<DailyViewItem>()

        // 임의로 설정
        val exampleDayLinesResponse =
            DailyViewItem(
                    id = 1,
                    money = 1000,
                    img = null,
                    category = "식비",
                    assetType = "체크카드",
                    content = "멜론",
                    exceptStatus = false,
                    userNickName = "도비"
                )
        itemList.add(exampleDayLinesResponse)
        Log.d("DailyFragment", "Observer triggered: ")
        _dailyItems.value = itemList
    }
}
