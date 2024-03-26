package com.aos.floney.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class PostUserEmailMailRequestDto(
    @SerialName("email")
    val email: String,
    @SerialName("code")
    val code: String
)