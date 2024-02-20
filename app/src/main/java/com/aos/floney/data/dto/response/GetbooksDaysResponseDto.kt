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
        }?.let { dailyItems ->
            // Check if carryOverInfo is true
            if (carryOverInfo.carryOverStatus == true && carryOverInfo.carryOverMoney != 0.0) {
                // Create an empty DailyItem
                val emptyDailyItem = GetbooksDaysData.DailyItem(
                    id = -1, // Use a unique identifier
                    money = carryOverInfo.carryOverMoney, // Set the default value for money
                    assetType = DailyItemType.CARRY, // Set default assetType or adjust as needed
                    img = "user_default", // Set default image or adjust as needed
                    category = emptyList(), // Set default categories or adjust as needed
                    content = "이월", // Set default content or adjust as needed
                    userEmail = "", // Set default userEmail or adjust as needed
                    exceptStatus = false, // Set default exceptStatus or adjust as needed
                    userNickName = "" // Set default userNickName or adjust as needed
                )
                // Add the emptyDailyItem as the first item
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
                assetType = DailyItemType.valueOf(it.assetType),
            )
        }
    }
    fun convertToCarryInfo(): GetbooksDaysData.CarryOverInfo =
        GetbooksDaysData.CarryOverInfo (
            carryOverStatus = carryOverInfo.carryOverStatus,
            carryOverMoney = carryOverInfo.carryOverMoney,
        )
}