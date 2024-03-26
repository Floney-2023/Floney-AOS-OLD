package com.aos.floney.domain.repository

import com.aos.floney.domain.entity.GetbooksDaysData
import com.aos.floney.domain.entity.GetbooksInfoData
import com.aos.floney.domain.entity.GetbooksMonthData
import com.aos.floney.domain.entity.books.GetbooksUsersCheckData

interface CalendarRepository {
    suspend fun getbooksMonthData(
        bookKey : String,
        date : String
    ) : Result<GetbooksMonthData>
    suspend fun getbooksDaysData(
        bookKey: String,
        date: String
    ): Result<GetbooksDaysData>
    suspend fun getbooksInfoData(
        bookKey: String
    ): Result<GetbooksInfoData>
    suspend fun getbooksUsersCheckData(

    ): Result<GetbooksUsersCheckData>

}