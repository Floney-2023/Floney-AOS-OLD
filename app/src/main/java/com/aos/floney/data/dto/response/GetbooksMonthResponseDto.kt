package com.aos.floney.data.dto.response

import com.aos.floney.domain.entity.CalendarItemType
import com.aos.floney.domain.entity.GetbooksMonthData
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class GetbooksMonthResponseDto (
    @SerialName("expenses")
    val expenses : List<booksMonthDto>?,
    @SerialName("carryOverInfo")
    val carryOverInfo : CarryOverInfo,
    @SerialName("totalIncome")
    val totalIncome : Double,
    @SerialName("totalOutcome")
    val totalOutCome : Double,
){
    @Serializable
    data class booksMonthDto(
        @SerialName("date")
        val date : String,
        @SerialName("money")
        val money : Double,
        @SerialName("assetType")
        val assetType : String
        )
    @Serializable
    data class CarryOverInfo(
        @SerialName("carryOverStatus")
        val carryOverStatus : Boolean,
        @SerialName("carryOverMoney")
        val carryOverMoney : Double,
    )
    fun converToBooksMonth(): List<GetbooksMonthData.CalendarItem>? = expenses?.map { data ->
        GetbooksMonthData.CalendarItem(
            date = data.date,
            money = data.money,
            assetType = CalendarItemType.valueOf(data.assetType)
        )
    }
    fun convertToCarryOverInfo(): GetbooksMonthData.CarryOverInfo =
        GetbooksMonthData.CarryOverInfo(
            carryOverStatus = carryOverInfo.carryOverStatus,
            carryOverMoney = carryOverInfo.carryOverMoney
        )
}