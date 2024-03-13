package com.aos.floney.data.dto.response.books

import com.aos.floney.domain.entity.GetbooksInfoData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Serializable
data class GetbooksInfoResponseDto(
    @SerialName("bookImg")
    val bookImg: String?,
    @SerialName("bookName")
    val bookName: String,
    @SerialName("startDay")
    val startDay: String,
    @SerialName("seeProfileStatus")
    val seeProfileStatus: Boolean,
    @SerialName("carryOver")
    val carryOver: Boolean,
    @SerialName("ourBookUsers")
    val ourBookUsers: List<OurBookUsers>,
) {
    @Serializable
    data class OurBookUsers(
        @SerialName("name")
        val name: String,
        @SerialName("profileImg")
        val profileImg: String,
        @SerialName("email")
        val email: String,
        @SerialName("role")
        val role: String,
        @SerialName("me")
        val me: Boolean
    )
    fun convertToOutBookUsers(): List<GetbooksInfoData.BookUser>? {
        return ourBookUsers.map { it ->
            GetbooksInfoData.BookUser(
                name = it.name,
                profileImg =  it.profileImg,
                email = it.email,
                role = it.role,
                me = it.me
            )
        }
    }

    fun convertStringToDate(pattern: String = "yyyy-MM-dd"): Date? {
        val dateFormatter = SimpleDateFormat(pattern, Locale.getDefault())
        return try {
            dateFormatter.parse(startDay)
        } catch (e: Exception) {
            null
        }
    }
}