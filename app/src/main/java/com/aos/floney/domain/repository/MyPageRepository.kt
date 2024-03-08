package com.aos.floney.domain.repository

import com.aos.floney.domain.entity.UserMypageData

interface MyPageRepository {
    suspend fun getusersMypageData(
        authorization : String
    ) : Result<UserMypageData>
}