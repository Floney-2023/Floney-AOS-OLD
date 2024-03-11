package com.aos.floney.data.service

import com.aos.floney.data.dto.request.RequestPutUsersPasswordDto
import com.aos.floney.data.dto.response.GetUserReceiveMarketingResponseDto
import com.aos.floney.data.dto.response.GetusersMypageResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PUT
import retrofit2.http.Query

interface MypageService {
    @GET("/users/mypage")
    suspend fun getusersMypageData(
        @Header("Authorization") authorization : String
    ): GetusersMypageResponseDto

    @GET("/users/receive-marketing")
    suspend fun getusersReceiveMarketingData(
        @Header("Authorization") authorization : String
    ): GetUserReceiveMarketingResponseDto

    @PUT("/users/receive-marketing")
    suspend fun putusersReceiveMarketingData(
        @Header("Authorization") authorization : String,
        @Query("agree") agree : Boolean
    )

    @PUT("/users/password")
    suspend fun putusersPasswordData(
        @Header("Authorization") authorization : String,
        @Body requestPutUsersPasswordDto : RequestPutUsersPasswordDto
    )

    @GET("/users/nickname/update")
    suspend fun getusersNicknameUpdate(
        @Header("Authorization") authorization : String,
        @Query("nickname") nickname : String
    )


}