package com.aos.floney.data.service

import com.aos.floney.data.dto.request.GetUserRequestDto
import com.aos.floney.data.dto.response.GetUserResponseDto
import com.aos.floney.data.dto.response.GetbooksMonthrResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Query

interface CalendarService {
    @GET("/books/month")
    suspend fun getUserData(
        @Query("bookKey") bookKey : String,
        @Query("date") date : String,
    ): GetbooksMonthrResponseDto


}