package com.aos.floney.data.service

import com.aos.floney.data.dto.request.RequestPostUsersBookKeyDto
import com.aos.floney.data.dto.request.RequestPutUsersPasswordDto
import com.aos.floney.data.dto.response.GetUserReceiveMarketingResponseDto
import com.aos.floney.data.dto.response.GetusersMypageResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Query

interface MypageService {
    @GET("/users/mypage")
    suspend fun getusersMypageData(
    ): GetusersMypageResponseDto

    @GET("/users/receive-marketing")
    suspend fun getusersReceiveMarketingData(
    ): GetUserReceiveMarketingResponseDto

    @PUT("/users/receive-marketing")
    suspend fun putusersReceiveMarketingData(
        @Query("agree") agree : Boolean
    )

    @PUT("/users/password")
    suspend fun putusersPasswordData(
        @Body requestPutUsersPasswordDto : RequestPutUsersPasswordDto
    )

    @GET("/users/nickname/update")
    suspend fun getusersNicknameUpdate(
        @Query("nickname") nickname : String
    )
    @POST("/users/bookKey")
    suspend fun getusersBookKey(
        @Body requestPostUsersBookKeyDto : RequestPostUsersBookKeyDto
    )
}