package com.aos.floney.data.source

import com.aos.floney.data.service.CalendarService
import com.aos.floney.data.service.MypageService
import javax.inject.Inject

class MypageDataSource @Inject constructor(
    private val mypageService: MypageService
){

    suspend fun getusersMypageData(
        authorization : String
    ) = mypageService.getusersMypageData(authorization)

    suspend fun getusersReceiveMarketingData(
        authorization : String
    ) = mypageService.getusersReceiveMarketingData(authorization)

}