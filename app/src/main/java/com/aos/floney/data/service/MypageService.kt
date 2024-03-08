package com.aos.floney.data.service

import com.aos.floney.data.dto.response.GetbooksDaysResponseDto
import com.aos.floney.data.dto.response.GetbooksInfoResponseDto
import com.aos.floney.data.dto.response.GetbooksMonthResponseDto
import com.aos.floney.data.dto.response.GetusersMypageResponseDto
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface MypageService {
    @GET("/users/mypage")
    suspend fun getusersMypageData(
        @Header("Authorization") authorization : String
    ): GetusersMypageResponseDto
}