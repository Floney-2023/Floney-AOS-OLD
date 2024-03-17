package com.aos.floney.data.service

import com.aos.floney.data.dto.request.PostLoginRequestDto
import com.aos.floney.data.dto.request.PostUserEmailMailRequestDto
import com.aos.floney.data.dto.request.PostUserSignupRequestDto
import com.aos.floney.data.dto.request.RequestPostRegisterUserDto
import com.aos.floney.data.dto.response.users.PostUserLoginResponseDto
import com.aos.floney.data.dto.response.users.PostUserSignupResponseDto
import com.aos.floney.domain.entity.login.PostusersLoginData
import com.google.android.gms.common.internal.safeparcel.SafeParcelable
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface UserService {
    @POST("/users")
    suspend fun postSignupUser(
        @Body postUserSignupRequestDto: PostUserSignupRequestDto
    ) : PostUserSignupResponseDto
    @POST("/users/login")
    suspend fun postLoginUser(
        @Body postLoginRequestDto: PostLoginRequestDto
    ) : PostUserLoginResponseDto
    @GET("/users/logout")
    suspend fun getLogoutUser(
        @Query("accessToken") accessToken: String
    ) : PostUserLoginResponseDto

    @GET("/users/email/mail")
    suspend fun getEmailMailUser(
        @Query("email") email: String
    )

    @POST("/users/email/mail")
    suspend fun postEmailMailUser(
        @Body postUserEmailMailRequestDto: PostUserEmailMailRequestDto
    )

}