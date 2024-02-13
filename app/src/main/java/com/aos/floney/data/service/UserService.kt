package com.aos.floney.data.service

import com.aos.floney.data.dto.request.GetUserRequestDto
import com.aos.floney.data.dto.response.GetUserResponseDto
import retrofit2.http.Body
import retrofit2.http.GET

interface UserService {
    @GET("")
    suspend fun getUserData(
        @Body requestGetUser: GetUserRequestDto
    ): GetUserResponseDto


}