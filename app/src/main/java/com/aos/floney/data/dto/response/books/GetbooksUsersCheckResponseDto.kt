package com.aos.floney.data.dto.response.books

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetbooksUsersCheckResponseDto(
    @SerialName("bookKey")
    val bookKey: String
)