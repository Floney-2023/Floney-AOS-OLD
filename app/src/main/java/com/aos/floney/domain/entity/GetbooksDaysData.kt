package com.aos.floney.domain.entity

data class GetbooksDaysData(
    val dayLinesResponse: List<DailyItem>?,
    val totalExpense: List<TotalExpense>?,
    val carryOverInfo: CarryOverInfo,
    val seeProfileImg: Boolean
) {
    data class DailyItem(
        val id: Int,
        val money: Double,
        val description: String,
        val exceptStatus: Boolean,
        val lineCategory: DailyItemType,
        val lineSubCategory: String,
        val assetSubCategory: String,
        val writerEmail: String,
        val writerNickname: String,
        val writerProfileImg: String,
        val repeatDuration: String
    )

    data class TotalExpense(
        val categoryType: DailyItemType,
        val money: Double
    )

    data class CarryOverInfo(
        val carryOverStatus: Boolean,
        val carryOverMoney: Double
    )
}

enum class DailyItemType {
    INCOME, OUTCOME, TRANSFER
}