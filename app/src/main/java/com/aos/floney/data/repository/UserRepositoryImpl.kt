package com.aos.floney.data.repository

import com.aos.floney.data.dto.request.PostLoginRequestDto
import com.aos.floney.data.dto.request.PostUserEmailMailRequestDto
import com.aos.floney.data.dto.request.RequestPostRegisterUserDto
import com.aos.floney.data.dto.response.PostUserResponseDto
import com.aos.floney.data.dto.response.users.PostUserLoginResponseDto
import com.aos.floney.data.source.UserDataSource
import com.aos.floney.domain.entity.GetbooksMonthData
import com.aos.floney.domain.entity.books.GetbooksUsersCheckData
import com.aos.floney.domain.entity.login.PostusersLoginData
import com.aos.floney.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun postRegisterUser(requestPostRegisterUserDto: RequestPostRegisterUserDto): Result<Unit> =
        runCatching {
            userDataSource.postRegisterUser(requestPostRegisterUserDto)
        }
    override suspend fun postLoginUser(postLoginRequestDto: PostLoginRequestDto): Result<PostusersLoginData> =
        runCatching {
            val response = userDataSource.postLoginUser(postLoginRequestDto)
            PostusersLoginData(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )
        }
    override suspend fun getLogoutUser(accessToken : String): Result<Unit> =
        runCatching {
            userDataSource.getLogoutUser(accessToken)
        }
    override suspend fun getEmailMailUser(email : String): Result<Unit> =
        runCatching {
            userDataSource.getEmailMailUser(email)
        }
    override suspend fun postEmailMailUser(postUserEmailMailRequestDto : PostUserEmailMailRequestDto): Result<Unit> =
        runCatching {
            userDataSource.postEmailMailUser(postUserEmailMailRequestDto)
        }
}