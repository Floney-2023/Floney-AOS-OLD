package com.aos.floney.data.source

import com.aos.floney.data.service.CalendarService
import javax.inject.Inject

class CalendarDataSource @Inject constructor(
    private val calendarService: CalendarService
){
    suspend fun getbooksMonthData(
        bookKey :String,
        date : String
    ) = calendarService.getbooksMonthData(bookKey, date)

    suspend fun getbooksDaysData(
        bookKey: String,
        date: String
    ) = calendarService.getbooksDaysData(bookKey, date)

    suspend fun getbooksInfoData(
        bookKey: String
    ) = calendarService.getbooksInfoData(bookKey)

    suspend fun getbooksUsersCheck(
    ) = calendarService.getBooksUsersCheck()

}