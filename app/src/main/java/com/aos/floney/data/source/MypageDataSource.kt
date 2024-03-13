package com.aos.floney.data.source

import com.aos.floney.data.dto.request.RequestPostUsersBookKeyDto
import com.aos.floney.data.dto.request.RequestPutUsersPasswordDto
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

    suspend fun putusersReceiveMarketingData(
        authorization : String,
        agree : Boolean
    ) = mypageService.putusersReceiveMarketingData(authorization, agree)

    suspend fun putusersPasswordData(
        authorization : String,
        requestPutUsersPasswordDto: RequestPutUsersPasswordDto
    ) = mypageService.putusersPasswordData(authorization, requestPutUsersPasswordDto)

    suspend fun getusersNicknameUpdate(
        authorization : String,
        nickname : String
    ) = mypageService.getusersNicknameUpdate(authorization, nickname)

    suspend fun getusersBookKey(
        authorization : String,
        requestPostUsersBookKeyDto: RequestPostUsersBookKeyDto
    ) = mypageService.getusersBookKey(authorization, requestPostUsersBookKeyDto)

}