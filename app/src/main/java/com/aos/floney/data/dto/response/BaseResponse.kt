package com.aos.floney.data.dto.response

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BaseResponse<T>(
    @SerialName("code")
    val code: Int? = null,
    @SerialName("message")
    val message: String? = null,
    @SerialName("data")
    val data: T? = null
)