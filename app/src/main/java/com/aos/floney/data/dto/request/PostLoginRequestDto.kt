package com.aos.floney.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostLoginRequestDto(
    @SerialName("email")
    val email: String,
    @SerialName("password")
    val password: String
)