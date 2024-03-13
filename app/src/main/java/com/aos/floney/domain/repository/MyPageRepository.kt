package com.aos.floney.domain.repository

import android.provider.ContactsContract.CommonDataKinds.Nickname
import com.aos.floney.data.dto.request.RequestPostUsersBookKeyDto
import com.aos.floney.data.dto.request.RequestPutUsersPasswordDto
import com.aos.floney.domain.entity.mypage.UserMypageData
import com.aos.floney.domain.entity.mypage.ReceiveMarketing

interface MyPageRepository {
    suspend fun getusersMypageData(
        authorization : String
    ) : Result<UserMypageData>
    suspend fun getusersReceiveMarketingData(
        authorization : String
    ) : Result<ReceiveMarketing>

    suspend fun putusersReceiveMarketingData(
        authorization : String,
        agree : Boolean
    ) : Result<Unit>

    suspend fun putusersPasswordData(
        authorization : String,
        putUsersPasswordDto: RequestPutUsersPasswordDto
    ) : Result<Unit>

    suspend fun getusersNicknameUpdate(
        authorization: String,
        nickname: String
    ) : Result<Unit>

    suspend fun getusersBookKey(
        authorization: String,
        requestPostUsersBookKeyDto: RequestPostUsersBookKeyDto
    ) : Result<Unit>
}