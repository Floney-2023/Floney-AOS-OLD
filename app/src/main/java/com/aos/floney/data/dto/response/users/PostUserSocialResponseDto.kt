package com.aos.floney.data.dto.response.users

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//임시
@Serializable
data class PostUserSocialResponseDto(
    @SerialName("email")
    val email: String,
    @SerialName("nickname")
    val nickname: String,
    @SerialName("receiveMarketing")
    val receiveMarketing: String
)