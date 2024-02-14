package com.aos.floney.data.repository

import com.aos.floney.data.source.CalendarDataSource
import com.aos.floney.domain.entity.CalendarItem
import com.aos.floney.domain.repository.CalendarRepository
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val calendarDataSource: CalendarDataSource
) : CalendarRepository{
    override suspend fun getbooksMonthData(
        bookKey: String,
        date: String
    ): Result<List<CalendarItem>?> =
        runCatching {
            calendarDataSource.getbooksMonthData(bookKey, date).converToBooksMonth()
        }

}