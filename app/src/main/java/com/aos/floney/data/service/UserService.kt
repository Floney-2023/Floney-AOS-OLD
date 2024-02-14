package com.aos.floney.data.service

import com.aos.floney.data.dto.request.GetUserRequestDto
import com.aos.floney.data.dto.request.RequestPostRegisterUserDto
import com.aos.floney.data.dto.response.GetUserResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface UserService {
    @POST("users")
    suspend fun postRegisterUser(
        @Body requestPostRegisterUserDto: RequestPostRegisterUserDto
    )
}