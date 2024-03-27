package com.aos.floney.data.repository

import com.aos.floney.data.dto.request.RequestPostUsersBookKeyDto
import com.aos.floney.data.dto.request.RequestPutUsersPasswordDto
import com.aos.floney.data.source.MypageDataSource
import com.aos.floney.domain.entity.mypage.UserMypageData
import com.aos.floney.domain.entity.mypage.ReceiveMarketing
import com.aos.floney.domain.repository.MyPageRepository
import javax.inject.Inject

class MypageRepositoryImpl @Inject constructor(
    private val mypageDataSource: MypageDataSource
) : MyPageRepository {
    override suspend fun getusersMypageData(): Result<UserMypageData> =
        runCatching {
            val response = mypageDataSource.getusersMypageData()
            UserMypageData(
                nickname = response.nickname,
                email = response.email,
                profileImg = response.profileImg,
                provider = response.provider,
                lastAdTime = response.lastAdTime,
                myBooks = response.convertToBookDto()
            )
        }
    override suspend fun getusersReceiveMarketingData(): Result<ReceiveMarketing> =
        runCatching {
            val response = mypageDataSource.getusersReceiveMarketingData()
            ReceiveMarketing(
                receiveMarketing = response.receiveMarketing
            )
        }
    override suspend fun putusersReceiveMarketingData(
        agree : Boolean
    ): Result<Unit> =
        runCatching {
            mypageDataSource.putusersReceiveMarketingData(agree)
        }
    override suspend fun putusersPasswordData(
        requestPutUsersPasswordDto: RequestPutUsersPasswordDto
    ): Result<Unit> =
        runCatching {
            mypageDataSource.putusersPasswordData(requestPutUsersPasswordDto)
        }

    override suspend fun getusersNicknameUpdate(
        nickname: String
    ): Result<Unit> =
        runCatching {
        mypageDataSource.getusersNicknameUpdate(nickname)
    }

    override suspend fun getusersBookKey(
        requestPostUsersBookKeyDto: RequestPostUsersBookKeyDto
    ): Result<Unit> =
        runCatching {
            mypageDataSource.getusersBookKey(requestPostUsersBookKeyDto)
        }

    override suspend fun getProfileimgUpdate(profileimg : String) : Result<Unit> =
        runCatching {
            mypageDataSource.getProfileimgUpdate(profileimg)
        }

}