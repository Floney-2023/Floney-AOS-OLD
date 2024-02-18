package com.aos.floney.domain.entity

data class GetbooksDaysData(
    val dayLinesResponse: List<DailyItem>?,
    val totalExpense: TotalExpense,
    val carryOverInfo: CarryOverInfo,
    val seeProfileImg: Boolean
){
    data class DailyItem(
        val id: Int,
        val money: Double,
        val img: String,
        val category: List<String>,
        val assetType: DailyItemType,
        val content: String,
        val userEmail: String,
        val exceptStatus: Boolean,
        val userNickName: String
    )
    data class TotalExpense(
        val money: Double,
        val assetType: DailyItemType
    )

    data class CarryOverInfo(
        val carryOverStatus: Boolean,
        val carryOverMoney: Double
    )
}

enum class DailyItemType {
    INCOME, OUTCOME
}