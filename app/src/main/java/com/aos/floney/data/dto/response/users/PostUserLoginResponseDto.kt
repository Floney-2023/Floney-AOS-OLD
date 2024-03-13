package com.aos.floney.data.dto.response.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//임시
@Serializable
data class PostUserLoginResponseDto(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String
)