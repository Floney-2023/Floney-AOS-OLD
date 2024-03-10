package com.aos.floney.data.dto.response

import com.aos.floney.domain.entity.mypage.UserMypageData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetusersMypageResponseDto(
    @SerialName("nickname")
    val nickname: String,
    @SerialName("email")
    val email: String,
    @SerialName("profileImg")
    val profileImg: String,
    @SerialName("provider")
    val provider: String,
    @SerialName("lastAdTime")
    val lastAdTime: String,
    @SerialName("myBooks")
    val myBooks: List<BookDto>
) {
    @Serializable
    data class BookDto(
        @SerialName("bookImg")
        val bookImg: String?, // Assuming bookImg can be nullable
        @SerialName("bookKey")
        val bookKey: String,
        @SerialName("name")
        val name: String,
        @SerialName("memberCount")
        val memberCount: Int
    )

    fun convertToBookDto(): List<UserMypageData.Book> = myBooks.map { book ->
        UserMypageData.Book(
            bookImg = book.bookImg,
            bookKey = book.bookKey,
            name = book.name,
            memberCount = book.memberCount
        )
    }
}