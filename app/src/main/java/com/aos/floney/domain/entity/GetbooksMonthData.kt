package com.aos.floney.domain.entity

data class GetbooksMonthData(
    val calendarItems: List<CalendarItem>?,
    val carryOverInfo: CarryOverInfo,
    var totalIncome: Double,
    var totalOutcome: Double
) {
    data class CalendarItem(
        val date: String,
        var money: Double,
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