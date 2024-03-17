package com.aos.floney.domain.repository

import com.aos.floney.data.dto.request.PostLoginRequestDto
import com.aos.floney.data.dto.request.RequestPostRegisterUserDto
import com.aos.floney.data.dto.response.users.PostUserLoginResponseDto
import com.aos.floney.domain.entity.login.PostusersLoginData

interface UserRepository {
    suspend fun postRegisterUser(requestPostRegisterUserDto: RequestPostRegisterUserDto): Result<Unit>
    suspend fun postLoginUser(postLoginRequestDto: PostLoginRequestDto): Result<PostusersLoginData>

    suspend fun getLogoutUser(accessToken: String): Result<Unit>

    suspend fun getEmailMailUser(email: String): Result<Unit>

}