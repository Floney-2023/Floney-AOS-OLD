package com.aos.floney.data.source

import com.aos.floney.data.dto.request.PostLoginRequestDto
import com.aos.floney.data.dto.request.PostUserEmailMailRequestDto
import com.aos.floney.data.dto.request.PostUserSignupRequestDto
import com.aos.floney.data.dto.request.RequestPostRegisterUserDto
import com.aos.floney.data.dto.response.users.PostUserLoginResponseDto
import com.aos.floney.data.dto.response.users.PostUserSignupResponseDto
import com.aos.floney.data.dto.response.users.PostUserSocialResponseDto
import com.aos.floney.data.service.UserService
import com.aos.floney.domain.entity.login.PostusersLoginData
import com.aos.floney.domain.entity.signup.PostusersSignupData
import javax.inject.Inject

class UserDataSource @Inject constructor(
    private val userService: UserService
) {
    suspend fun postSignupUser(postUserSignupRequestDto: PostUserSignupRequestDto) =
        userService.postSignupUser(postUserSignupRequestDto)
    suspend fun postLoginUser(postLoginRequestDto: PostLoginRequestDto) =
        userService.postLoginUser(postLoginRequestDto)
    suspend fun getLogoutUser(accessToken: String) =
        userService.getLogoutUser(accessToken)
    suspend fun getEmailMailUser(email: String) =
        userService.getEmailMailUser(email)
    suspend fun postEmailMailUser(postUserEmailMailRequestDto: PostUserEmailMailRequestDto) =
        userService.postEmailMailUser(postUserEmailMailRequestDto)

    suspend fun postSocialLogin(provider : String, socialAccessToken: String) =
        userService.postSocialLogin(provider,socialAccessToken)
    suspend fun postSocialSignup(provider : String, socialAccessToken: String):PostUserSocialResponseDto =
        userService.postSocialSignup(provider,socialAccessToken)
}