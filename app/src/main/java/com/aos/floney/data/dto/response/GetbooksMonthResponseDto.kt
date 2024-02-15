package com.aos.floney.data.dto.response

import com.aos.floney.domain.entity.CalendarItem
import com.aos.floney.domain.entity.CalendarItemType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetbooksMonthResponseDto (
    @SerialName("expenses")
    val expenses : List<booksMonthDto>?,
    @SerialName("carryOverInfo")
    val carryOverInfo : CarryOverInfo,
    @SerialName("totalIncome")
    val totalIncome : Int,
    @SerialName("totalOutCome")
    val totalOutCome : Int,
){
    @Serializable
    data class booksMonthDto(
        @SerialName("date")
        val date : String,
        @SerialName("money")
        val money : Int,
        @SerialName("assetType")
        val assetType : String
        )
    @Serializable
    data class CarryOverInfo(
        @SerialName("carryOverStatus")
        val carryOverStatus : Boolean,
        @SerialName("carryOverMoney")
        val carryOverMoney : Int,
    )
    fun converToBooksMonth(): List<CalendarItem>? = expenses?.map { data ->
        CalendarItem(
            date = data.date,
            money = data.money,
            assetType = CalendarItemType.valueOf(data.assetType)
        )

    }
}