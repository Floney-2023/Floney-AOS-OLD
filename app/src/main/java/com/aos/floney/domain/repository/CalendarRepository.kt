package com.aos.floney.domain.repository

import com.aos.floney.data.dto.response.GetbooksMonthResponseDto
import com.aos.floney.domain.entity.DailyItem
import com.aos.floney.domain.entity.GetbooksMonthData
import com.aos.floney.domain.entity.TotalExpense

interface CalendarRepository {
    suspend fun getbooksMonthData(
        authorization : String,
        bookKey : String,
        date : String
    ) : Result<GetbooksMonthData>
    suspend fun getbooksDaysData(
        authorization: String,
        bookKey: String,
        date: String
    ): Result<Pair<List<DailyItem>?, List<TotalExpense>?>>
}