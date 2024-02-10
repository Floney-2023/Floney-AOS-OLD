package com.aos.floney.domain.entity

data class DayLinesResponse(
    val dayLinesResponse: List<DayLinesResults?>,
    val totalExpense: List<DayTotalExpenses?>,
    val carryOverInfo: CarryOverInfo,
    val seeProfileImg: Boolean
)

data class DayLinesResults(
    val id: Int,
    val money: Double,
    val img: String?, // null default
    val category: List<String>,
    val assetType: String,
    val content: String,
    val exceptStatus: Boolean,
    val userNickName: String
)

data class DayTotalExpenses(
    val money: Double,
    val assetType: String
)

data class CarryOverInfo(
    val carryOverStatus: Boolean,
    val carryOverMoney: Double
)