package com.aos.floney.data.source

import com.aos.floney.data.service.CalendarService
import javax.inject.Inject

class CalendarDataSource @Inject constructor(
    private val calendarService: CalendarService
){
    suspend fun getbooksMonthData(
        authorization : String,
        bookKey :String,
        date : String
    ) = calendarService.getbooksMonthData(authorization, bookKey, date)

    suspend fun getbooksDaysData(
        authorization : String,
        bookKey: String,
        date: String
    ) = calendarService.getbooksDaysData(authorization, bookKey, date)

    suspend fun getbooksInfoData(
        authorization : String,
        bookKey: String
    ) = calendarService.getbooksInfoData(authorization, bookKey)

    suspend fun getbooksUsersCheck(
        authorization : String
    ) = calendarService.getBooksUsersCheck(authorization)

}