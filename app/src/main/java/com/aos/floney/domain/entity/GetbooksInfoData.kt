package com.aos.floney.domain.entity

import java.util.Date

data class GetbooksInfoData(
    val bookImg: String?,
    val bookName: String,
    val startDay: Date?,
    val seeProfileStatus: Boolean,
    val carryOver: Boolean,
    val ourBookUsers: List<BookUser>?
){
    data class BookUser(
        val name: String,
        val profileImg: String,
        val email: String,
        val role: String,
        val me: Boolean
    )
}