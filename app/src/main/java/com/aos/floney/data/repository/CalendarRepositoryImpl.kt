package com.aos.floney.data.repository

import com.aos.floney.data.source.CalendarDataSource
import com.aos.floney.domain.entity.GetbooksDaysData
import com.aos.floney.domain.entity.GetbooksInfoData
import com.aos.floney.domain.entity.GetbooksMonthData
import com.aos.floney.domain.entity.books.GetbooksUsersCheckData
import com.aos.floney.domain.repository.CalendarRepository
import javax.inject.Inject

class CalendarRepositoryImpl @Inject constructor(
    private val calendarDataSource: CalendarDataSource
) : CalendarRepository{
    override suspend fun getbooksMonthData(
        bookKey: String,
        date: String
    ): Result<GetbooksMonthData> =
        runCatching {
            val response = calendarDataSource.getbooksMonthData(bookKey, date)
            GetbooksMonthData(
                calendarItems = response.converToBooksMonth(),
                carryOverInfo = response.convertToCarryOverInfo(),
                totalIncome = response.totalIncome,
                totalOutcome = response.totalOutCome
            )
        }

    override suspend fun getbooksDaysData(
        bookKey: String,
        date: String
    ): Result<GetbooksDaysData> =
        runCatching {
            val response = calendarDataSource.getbooksDaysData(bookKey, date)
            GetbooksDaysData(
                dayLinesResponse = response.convertToDailyItems(),
                totalExpense = response.convertToTotalExpense(),
                carryOverInfo = response.convertToCarryInfo(),
                seeProfileImg = response.seeProfileImg
            )
        }
    override suspend fun getbooksInfoData(
        bookKey: String
    ): Result<GetbooksInfoData> =
        runCatching {
            val response = calendarDataSource.getbooksInfoData(bookKey)
            GetbooksInfoData(
                bookImg = response.bookImg,
                bookName = response.bookName,
                startDay = response.convertStringToDate(),
                seeProfileStatus = response.seeProfileStatus,
                carryOver = response.carryOver,
                ourBookUsers = response.convertToOutBookUsers()
            )
        }
    override suspend fun getbooksUsersCheckData(
    ): Result<GetbooksUsersCheckData> =
        runCatching {
            val response = calendarDataSource.getbooksUsersCheck()
            GetbooksUsersCheckData(
                bookKey = response.bookKey
            )
        }


}