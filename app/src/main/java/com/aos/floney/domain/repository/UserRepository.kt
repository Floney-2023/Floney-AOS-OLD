package com.aos.floney.domain.repository

import com.aos.floney.data.dto.request.PostLoginRequestDto
import com.aos.floney.data.dto.request.PostUserEmailMailRequestDto
import com.aos.floney.data.dto.request.PostUserSignupRequestDto
import com.aos.floney.data.dto.request.RequestPostRegisterUserDto
import com.aos.floney.data.dto.response.users.PostUserLoginResponseDto
import com.aos.floney.domain.entity.login.PostusersLoginData
import com.aos.floney.domain.entity.signup.PostusersSignupData

interface UserRepository {
    suspend fun postSignupUser(postUserSignupRequestDto: PostUserSignupRequestDto): Result<PostusersSignupData>
    suspend fun postLoginUser(postLoginRequestDto: PostLoginRequestDto): Result<PostusersLoginData>

    suspend fun getLogoutUser(accessToken: String): Result<Unit>

    suspend fun getEmailMailUser(email: String): Result<Unit>

    suspend fun postEmailMailUser(postUserEmailMailRequestDto: PostUserEmailMailRequestDto): Result<Unit>

}