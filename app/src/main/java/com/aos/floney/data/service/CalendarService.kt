package com.aos.floney.data.service

import com.aos.floney.data.dto.response.GetbooksDaysResponseDto
import com.aos.floney.data.dto.response.GetbooksMonthResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface CalendarService {
    @GET("/books/month")
    suspend fun getbooksMonthData(
        @Header("Authorization") authorization : String,
        @Query("bookKey") bookKey : String,
        @Query("date") date : String,
    ): GetbooksMonthResponseDto

    @GET("/books/days")
    suspend fun getbooksDaysData(
        @Header("Authorization") authorization : String,
        @Query("bookKey") bookKey : String,
        @Query("date") date : String,
    ): GetbooksDaysResponseDto

}