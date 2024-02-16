package com.aos.floney.data.dto.response

import com.aos.floney.domain.entity.CalendarItem
import com.aos.floney.domain.entity.CalendarItemType
import com.aos.floney.domain.entity.DailyItem
import com.aos.floney.domain.entity.TotalExpense
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetbooksDaysResponseDto(
    @SerialName("dayLinesResponse")
    val dayLinesResponse: List<DayLineItemDto>?,
    @SerialName("totalExpense")
    val totalExpense: List<TotalExpenseDto>?,
    @SerialName("seeProfileImg")
    val seeProfileImg: Boolean,
    @SerialName("carryOverInfo")
    val carryOverInfo: CarryOverInfoDto
) {
    @Serializable
    data class DayLineItemDto(
        @SerialName("id")
        val id: Int,
        @SerialName("money")
        val money: Double,
        @SerialName("img")
        val img: String,
        @SerialName("category")
        val category: List<String>,
        @SerialName("assetType")
        val assetType: String,
        @SerialName("content")
        val content: String,
        @SerialName("userEmail")
        val userEmail: String?,
        @SerialName("exceptStatus")
        val exceptStatus: Boolean,
        @SerialName("userNickName")
        val userNickName: String
    )

    @Serializable
    data class TotalExpenseDto(
        @SerialName("money")
        val money: Double,
        @SerialName("assetType")
        val assetType: String
    )

    @Serializable
    data class CarryOverInfoDto(
        @SerialName("carryOverStatus")
        val carryOverStatus: Boolean,
        @SerialName("carryOverMoney")
        val carryOverMoney: Double
    )

    fun convertToDailyItems(): List<DailyItem>? {
        return dayLinesResponse?.map { dayLineItem ->
            val categories = dayLineItem.category
            val assetType = CalendarItemType.valueOf(dayLineItem.assetType)
            val mainCategory = if (categories.isNotEmpty()) categories[0] else ""

            DailyItem(
                id = dayLineItem.id,
                money = dayLineItem.money,
                assetType = assetType,
                img = dayLineItem.img,
                category = categories,
                content = dayLineItem.content,
                userEmail = dayLineItem.userEmail ?: "",
                exceptStatus = dayLineItem.exceptStatus,
                userNickName = dayLineItem.userNickName
            )
        }
    }
    fun convertToTotalExpense(): List<TotalExpense>? {
        return totalExpense?.map { totalExpense ->
            TotalExpense(
                money = totalExpense.money,
                assetType = CalendarItemType.valueOf(totalExpense.assetType),
            )
        }
    }
}