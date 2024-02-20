package com.aos.floney.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPostRegisterUserDto(
    @SerialName("deviceToken")
    val deviceToken: String
)