package com.aos.floney.data.source

import com.aos.floney.data.dto.request.RequestPostRegisterUserDto
import com.aos.floney.data.service.UserService
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val userService: UserService
) {
    suspend fun postRegisterUser(requestPostRegisterUserDto: RequestPostRegisterUserDto) =
        userService.postRegisterUser(requestPostRegisterUserDto)
}