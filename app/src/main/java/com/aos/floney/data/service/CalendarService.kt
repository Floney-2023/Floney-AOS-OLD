package com.aos.floney.data.service

import com.aos.floney.data.dto.response.GetbooksMonthResponseDto
import retrofit2.http.GET
import retrofit2.http.Query

interface CalendarService {
    @GET("/books/month")
    suspend fun getbooksMonthData(
        @Query("bookKey") bookKey : String,
        @Query("date") date : String,
    ): GetbooksMonthResponseDto


}