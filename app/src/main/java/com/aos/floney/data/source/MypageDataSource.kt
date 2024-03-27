package com.aos.floney.data.source

import com.aos.floney.data.dto.request.RequestPostUsersBookKeyDto
import com.aos.floney.data.dto.request.RequestPutUsersPasswordDto
import com.aos.floney.data.service.CalendarService
import com.aos.floney.data.service.MypageService
import com.aos.floney.presentation.mypage.inform.profileImg.MypageFragmentInformProfileImg
import javax.inject.Inject

class MypageDataSource @Inject constructor(
    private val mypageService: MypageService
){

    suspend fun getusersMypageData(
    ) = mypageService.getusersMypageData()

    suspend fun getusersReceiveMarketingData(
    ) = mypageService.getusersReceiveMarketingData()

    suspend fun putusersReceiveMarketingData(
        agree : Boolean
    ) = mypageService.putusersReceiveMarketingData(agree)

    suspend fun putusersPasswordData(
        requestPutUsersPasswordDto: RequestPutUsersPasswordDto
    ) = mypageService.putusersPasswordData(requestPutUsersPasswordDto)

    suspend fun getusersNicknameUpdate(
        nickname : String
    ) = mypageService.getusersNicknameUpdate(nickname)

    suspend fun getusersBookKey(
        requestPostUsersBookKeyDto: RequestPostUsersBookKeyDto
    ) = mypageService.getusersBookKey(requestPostUsersBookKeyDto)

    suspend fun getProfileimgUpdate(
        profileImg: String
    ) = mypageService.getProfileimgUpdate(profileImg)
}