package com.aos.floney.data.dto.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RequestPostUsersBookKeyDto(
    @SerialName("bookKey")
    val deviceToken: String
)