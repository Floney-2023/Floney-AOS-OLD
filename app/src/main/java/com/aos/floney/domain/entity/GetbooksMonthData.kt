package com.aos.floney.domain.entity

data class GetbooksMonthData(
    val calendarItems: List<CalendarItem>?,
    val carryOverInfo: CarryOverInfo,
    val totalIncome: Double,
    val totalOutcome: Double
) {
    data class CalendarItem(
        val date: String,
        val money: Double,
        val assetType: CalendarItemType
    )

    data class CarryOverInfo(
        val carryOverStatus: Boolean,
        val carryOverMoney: Double
    )
}

enum class CalendarItemType {
    INCOME, OUTCOME
}