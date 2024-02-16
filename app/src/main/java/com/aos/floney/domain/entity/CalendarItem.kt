package com.aos.floney.domain.entity

data class CalendarItem(
    val date: String = "",
    val money: Double,
    val assetType : CalendarItemType
)

enum class CalendarItemType {
    INCOME, OUTCOME
}