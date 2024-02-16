package com.aos.floney.domain.entity

data class DayLinesResponse(
    val dayLinesResponse: List<DailyItem?>,
    val totalExpense: List<TotalExpense?>,
    val carryOverInfo: CarryOverInfo,
    val seeProfileImg: Boolean
)

data class DailyItem(
    val id: Int,
    val money: Double,
    val img: String,
    val category: List<String>,
    val assetType: CalendarItemType,
    val content: String,
    val userEmail: String,
    val exceptStatus: Boolean,
    val userNickName: String
)
data class TotalExpense(
    val money: Double,
    val assetType: CalendarItemType
)

data class CarryOverInfo(
    val carryOverStatus: Boolean,
    val carryOverMoney: Double
)