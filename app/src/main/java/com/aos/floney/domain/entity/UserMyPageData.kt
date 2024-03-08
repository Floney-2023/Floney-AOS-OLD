package com.aos.floney.domain.entity

data class UserMypageData(
    val nickname: String,
    val email: String,
    val profileImg: String,
    val provider: String,
    val lastAdTime: String,
    val myBooks: List<Book>
) {
    data class Book(
        val bookImg: String?,
        val bookKey: String,
        val name: String,
        val memberCount: Int
    )
}