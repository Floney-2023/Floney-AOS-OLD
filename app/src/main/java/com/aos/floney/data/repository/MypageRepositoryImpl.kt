package com.aos.floney.data.repository

import com.aos.floney.data.source.MypageDataSource
import com.aos.floney.domain.entity.UserMypageData
import com.aos.floney.domain.repository.MyPageRepository
import javax.inject.Inject

class MypageRepositoryImpl @Inject constructor(
    private val mypageDataSource: MypageDataSource
) : MyPageRepository {
    override suspend fun getusersMypageData(authorization: String): Result<UserMypageData> =
        runCatching {
            val response = mypageDataSource.getusersMypageData(authorization)
            UserMypageData(
                nickname = response.nickname,
                email = response.email,
                profileImg = response.profileImg,
                provider = response.provider,
                lastAdTime = response.lastAdTime,
                myBooks = response.convertToBookDto()
            )
        }
    
}