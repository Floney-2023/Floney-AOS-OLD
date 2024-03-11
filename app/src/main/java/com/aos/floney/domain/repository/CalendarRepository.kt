package com.aos.floney.domain.repository

import com.aos.floney.domain.entity.GetbooksDaysData
import com.aos.floney.domain.entity.GetbooksInfoData
import com.aos.floney.domain.entity.GetbooksMonthData
import com.aos.floney.domain.entity.books.GetbooksUsersCheckData

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
    ): Result<GetbooksDaysData>
    suspend fun getbooksInfoData(
        authorization: String,
        bookKey: String
    ): Result<GetbooksInfoData>
    suspend fun getbooksUsersCheckData(
        authorization: String
    ): Result<GetbooksUsersCheckData>

}