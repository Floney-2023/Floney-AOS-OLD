package com.aos.floney.domain.repository

import com.aos.floney.data.dto.request.PostLoginRequestDto
import com.aos.floney.data.dto.request.RequestPostRegisterUserDto

interface UserRepository {
    suspend fun postRegisterUser(requestPostRegisterUserDto: RequestPostRegisterUserDto): Result<Unit>
    suspend fun postLoginUser(postLoginRequestDto: PostLoginRequestDto): Result<Unit>
}