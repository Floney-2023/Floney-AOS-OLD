package com.aos.floney.domain.repository

import com.aos.floney.domain.entity.UserMypageData
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
}