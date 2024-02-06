package com.aos.floney.domain.entity

data class CalendarItem(
    val date: String,
    val depositAmount: String,
    val withdrawalAmount: String,
    val isCurrentMonth: Boolean
)