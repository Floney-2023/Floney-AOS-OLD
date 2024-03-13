package com.aos.floney.data.dto.response.books

import com.aos.floney.domain.entity.DailyItemType
import com.aos.floney.domain.entity.GetbooksDaysData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetbooksDaysResponseDto(
    @SerialName("dayLinesResponse")
    val dayLinesResponse: List<DayLineItemDto>?,
    @SerialName("totalExpense")
    val totalExpense: List<TotalExpenseDto>,
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
        @SerialName("description")
        val description: String,
        @SerialName("exceptStatus")
        val exceptStatus: Boolean,
        @SerialName("lineCategory")
        val lineCategory: String,
        @SerialName("lineSubCategory")
        val lineSubCategory: String,
        @SerialName("assetSubCategory")
        val assetSubCategory: String,
        @SerialName("writerEmail")
        val writerEmail: String,
        @SerialName("writerNickname")
        val writerNickname: String,
        @SerialName("writerProfileImg")
        val writerProfileImg: String,
        @SerialName("repeatDuration")
        val repeatDuration: String,
    )

    @Serializable
    data class TotalExpenseDto(
        @SerialName("money")
        val money: Double,
        @SerialName("categoryType")
        val assetType: String
    )

    @Serializable
    data class CarryOverInfoDto(
        @SerialName("carryOverStatus")
        val carryOverStatus: Boolean,
        @SerialName("carryOverMoney")
        val carryOverMoney: Double
    )

    fun convertToDailyItems(): List<GetbooksDaysData.DailyItem>? {
        return dayLinesResponse?.map { dayLineItem ->
            val categories = dayLineItem.lineSubCategory
            val assetType = DailyItemType.valueOf(dayLineItem.lineCategory)

            GetbooksDaysData.DailyItem(
                id = dayLineItem.id,
                money = dayLineItem.money,
                description = dayLineItem.description,
                exceptStatus = dayLineItem.exceptStatus,
                lineCategory = assetType,
                lineSubCategory = dayLineItem.lineSubCategory,
                assetSubCategory = dayLineItem.assetSubCategory,
                writerEmail = dayLineItem.writerEmail,
                writerNickname = dayLineItem.writerNickname,
                writerProfileImg = dayLineItem.writerProfileImg,
                repeatDuration = dayLineItem.repeatDuration
            )
        }?.let { dailyItems ->
            // Check if carryOverInfo is true
            if (carryOverInfo.carryOverStatus == true && carryOverInfo.carryOverMoney != 0.0) {
                val emptyDailyItem = GetbooksDaysData.DailyItem(
                    id = -1, // Use a unique identifier
                    money = carryOverInfo.carryOverMoney,
                    description = "이월",
                    exceptStatus = false,
                    lineCategory = DailyItemType.TRANSFER,
                    lineSubCategory = "",
                    assetSubCategory = "",
                    writerEmail = "",
                    writerNickname = "",
                    writerProfileImg = "",
                    repeatDuration = ""
                )
                listOf(emptyDailyItem) + dailyItems
            } else {
                dailyItems
            }
        }
    }

    fun convertToTotalExpense(): List<GetbooksDaysData.TotalExpense>? {
        return totalExpense?.map { it ->
            GetbooksDaysData.TotalExpense(
                money = it.money,
                categoryType = DailyItemType.valueOf(it.assetType),
            )
        }
    }
    fun convertToCarryInfo(): GetbooksDaysData.CarryOverInfo =
        GetbooksDaysData.CarryOverInfo (
            carryOverStatus = carryOverInfo.carryOverStatus,
            carryOverMoney = carryOverInfo.carryOverMoney,
        )
}