package com.aos.floney.data.service

import com.aos.floney.data.dto.response.books.GetbooksDaysResponseDto
import com.aos.floney.data.dto.response.books.GetbooksInfoResponseDto
import com.aos.floney.data.dto.response.books.GetbooksMonthResponseDto
import com.aos.floney.data.dto.response.books.GetbooksUsersCheckResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
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

    @GET("/books/info")
    suspend fun getbooksInfoData(
        @Header("Authorization") authorization : String,
        @Query("bookKey") bookKey : String
    ): GetbooksInfoResponseDto


    @GET("/books/users/check")
    suspend fun getBooksUsersCheck(
        @Header("Authorization") authorization : String
    ) : GetbooksUsersCheckResponseDto
}