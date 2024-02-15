package com.aos.floney.domain.repository

import com.aos.floney.domain.entity.CalendarItem

interface CalendarRepository {
    suspend fun getbooksMonthData(
        authorization : String,
        bookKey : String,
        date : String
    ) : Result<List<CalendarItem>?>
}