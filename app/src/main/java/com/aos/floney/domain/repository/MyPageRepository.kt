package com.aos.floney.domain.repository

import com.aos.floney.data.dto.response.GetusersMypageResponseDto
import com.aos.floney.domain.entity.GetbooksDaysData
import com.aos.floney.domain.entity.GetbooksInfoData
import com.aos.floney.domain.entity.GetbooksMonthData
import com.aos.floney.domain.entity.UserMypageData

interface MyPageRepository {
    suspend fun getusersMypageData(
        authorization : String
    ) : Result<UserMypageData>
}