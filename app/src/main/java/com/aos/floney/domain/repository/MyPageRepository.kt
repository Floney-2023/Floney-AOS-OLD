package com.aos.floney.domain.repository

import android.provider.ContactsContract.CommonDataKinds.Nickname
import com.aos.floney.data.dto.request.RequestPostUsersBookKeyDto
import com.aos.floney.data.dto.request.RequestPutUsersPasswordDto
import com.aos.floney.domain.entity.mypage.UserMypageData
import com.aos.floney.domain.entity.mypage.ReceiveMarketing

interface MyPageRepository {
    suspend fun getusersMypageData(
    ) : Result<UserMypageData>
    suspend fun getusersReceiveMarketingData(
    ) : Result<ReceiveMarketing>

    suspend fun putusersReceiveMarketingData(
        agree : Boolean
    ) : Result<Unit>

    suspend fun putusersPasswordData(
        putUsersPasswordDto: RequestPutUsersPasswordDto
    ) : Result<Unit>

    suspend fun getusersNicknameUpdate(
        nickname: String
    ) : Result<Unit>

    suspend fun getusersBookKey(
        requestPostUsersBookKeyDto: RequestPostUsersBookKeyDto
    ) : Result<Unit>

    suspend fun getProfileimgUpdate(
        profileImg : String
    ) : Result<Unit>
}