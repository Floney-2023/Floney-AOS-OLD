package com.aos.floney.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPutUsersPasswordDto(
    @SerialName("newPassword")
    val newPassword: String,
    @SerialName("oldPassword")
    val oldPassword: String
)