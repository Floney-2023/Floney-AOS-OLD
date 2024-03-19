package com.aos.floney.data.repository

import com.aos.floney.data.dto.request.PostLoginRequestDto
import com.aos.floney.data.dto.request.PostUserEmailMailRequestDto
import com.aos.floney.data.dto.request.PostUserSignupRequestDto
import com.aos.floney.data.dto.response.users.PostUserSignupResponseDto
import com.aos.floney.data.dto.request.RequestPostRegisterUserDto
import com.aos.floney.data.dto.response.users.PostUserSocialResponseDto
import com.aos.floney.data.source.UserDataSource
import com.aos.floney.domain.entity.login.PostusersLoginData
import com.aos.floney.domain.entity.signup.PostusersSignupData
import com.aos.floney.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val userDataSource: UserDataSource
) : UserRepository {
    override suspend fun postSignupUser(postUserSignupRequestDto: PostUserSignupRequestDto): Result<PostusersSignupData> =
        runCatching {
            val response = userDataSource.postSignupUser(postUserSignupRequestDto)
            PostusersSignupData(
                accessToken = response.accessToken,
                refreshToken = response.refreshToken
            )
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

    override suspend fun postSocialLogin(provider : String, socialAccessToken: String): Result<Unit> =
        runCatching {
            userDataSource.postSocialLogin(provider,socialAccessToken)
        }
    override suspend fun postSocialSignup(provider : String, socialAccessToken: String): Result<PostUserSocialResponseDto> =
        runCatching {
            userDataSource.postSocialSignup(provider,socialAccessToken)
        }

}