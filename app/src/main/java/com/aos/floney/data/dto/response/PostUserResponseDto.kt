package com.aos.floney.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

//임시
@Serializable
data class PostUserResponseDto(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)