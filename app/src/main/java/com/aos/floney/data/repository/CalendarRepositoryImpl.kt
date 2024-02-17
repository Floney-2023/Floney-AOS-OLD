package com.aos.floney.data.repository

import com.aos.floney.data.source.CalendarDataSource
import com.aos.floney.domain.entity.CalendarItem
import com.aos.floney.domain.entity.DailyItem
import com.aos.floney.domain.entity.TotalExpense
import com.aos.floney.domain.repository.CalendarRepository
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val calendarDataSource: CalendarDataSource
) : CalendarRepository{
    override suspend fun getbooksMonthData(
        authorization : String,
        bookKey: String,
        date: String
    ): Result<List<CalendarItem>?> =
        runCatching {
            calendarDataSource.getbooksMonthData(authorization, bookKey, date).converToBooksMonth()
        }
    override suspend fun getbooksDaysData(
        authorization : String,
        bookKey: String,
        date: String
    ): Result<Pair<List<DailyItem>?, List<TotalExpense>?>> =
        runCatching {
            val response = calendarDataSource.getbooksDaysData(authorization, bookKey, date)
            val dailyItems = response.convertToDailyItems()
            val totalExpense = response.convertToTotalExpense()

            Pair(dailyItems, totalExpense)
        }

}