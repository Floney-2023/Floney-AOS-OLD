package com.aos.floney.data.repository

import com.aos.floney.data.dto.request.RequestPostRegisterUserDto
import com.aos.floney.data.source.UserDataSource
import com.aos.floney.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun postRegisterUser(requestPostRegisterUserDto: RequestPostRegisterUserDto): Result<Unit> =
        runCatching {
            userDataSource.postRegisterUser(requestPostRegisterUserDto)
        }
}