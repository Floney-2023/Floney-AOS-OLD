package com.aos.floney.data.source

import com.aos.floney.data.dto.request.PostLoginRequestDto
import com.aos.floney.data.dto.request.RequestPostRegisterUserDto
import com.aos.floney.data.dto.response.users.PostUserLoginResponseDto
import com.aos.floney.data.service.UserService
import com.aos.floney.domain.entity.login.PostusersLoginData
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val userService: UserService
) {
    suspend fun postRegisterUser(requestPostRegisterUserDto: RequestPostRegisterUserDto) =
        userService.postRegisterUser(requestPostRegisterUserDto)
    suspend fun postLoginUser(postLoginRequestDto: PostLoginRequestDto) =
        userService.postLoginUser(postLoginRequestDto)
}