package com.aos.floney.data.dto.response

import com.aos.floney.domain.entity.CalendarItemType
import com.aos.floney.domain.entity.DailyItemType
import com.aos.floney.domain.entity.GetbooksDaysData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetbooksDaysResponseDto(
    @SerialName("dayLinesResponse")
    val dayLinesResponse: List<DayLineItemDto>?,
    @SerialName("totalExpense")
    val totalExpense: TotalExpenseDto,
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

    fun convertToDailyItems(): List<GetbooksDaysData.DailyItem>? {
        return dayLinesResponse?.map { dayLineItem ->
            val categories = dayLineItem.category
            val assetType = DailyItemType.valueOf(dayLineItem.assetType)

            GetbooksDaysData.DailyItem(
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
    fun convertToTotalExpense(): GetbooksDaysData.TotalExpense =
        GetbooksDaysData.TotalExpense(
            money = totalExpense.money,
            assetType = DailyItemType.valueOf(totalExpense.assetType),
        )
    fun convertToCarryInfo(): GetbooksDaysData.CarryOverInfo =
        GetbooksDaysData.CarryOverInfo (
            carryOverStatus = carryOverInfo.carryOverStatus,
            carryOverMoney = carryOverInfo.carryOverMoney,
        )
}