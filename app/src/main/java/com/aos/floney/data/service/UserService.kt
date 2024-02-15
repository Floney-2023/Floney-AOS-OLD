package com.aos.floney.data.service

import com.aos.floney.data.dto.request.PostLoginRequestDto
import com.aos.floney.data.dto.request.RequestPostRegisterUserDto
import retrofit2.http.Body
import retrofit2.http.POST

interface UserService {
    @POST("/users")
    suspend fun postRegisterUser(
        @Body requestPostRegisterUserDto: RequestPostRegisterUserDto
    )
    @POST("/users/login")
    suspend fun postLoginUser(
        @Body postLoginRequestDto: PostLoginRequestDto
    )
}