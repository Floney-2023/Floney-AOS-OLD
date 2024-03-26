package com.aos.floney.data.interceptor

import android.content.Context
import com.aos.floney.BuildConfig.BASE_URL
import com.aos.floney.data.dto.response.BaseResponse
import com.aos.floney.data.dto.response.ResponseReIssueTokenDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import com.aos.floney.domain.repository.DataStoreRepository
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import timber.log.Timber
import javax.inject.Inject

class AuthInterceptor @Inject constructor(
    @ApplicationContext context: Context,
    private val json: Json,
    private val dataStoreRepository: DataStoreRepository
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        runBlocking { Timber.e("액세스토큰 : ${getAccessToken()}, 리프레시토큰 : ${getRefreshToken()}") }
        val originalRequest = chain.request()

        if (originalRequest.url.encodedPath.equals("/users/login",true)||
            originalRequest.url.encodedPath.equals("/users/logout",true)||
            originalRequest.url.encodedPath.equals("/users/email/mail",true)||
            originalRequest.url.encodedPath.equals("/users",true)
        ){
            val headerRequest = originalRequest.newAuthBuilder()
                .build()
            val response = chain.proceed(headerRequest)
            return response
        }
        else {
            val headerRequest = originalRequest.newAuthBuilder()
                .header("Authorization", "Bearer ${runBlocking(Dispatchers.IO) { getAccessToken() }}")
                .build()

            val response = chain.proceed(headerRequest)

            Timber.e("액세스 토큰이 이거다. ${response}${runBlocking(Dispatchers.IO) { getAccessToken() }}")
            runBlocking { Timber.e("액세스토큰 : ${getAccessToken()}, 리프레시토큰 : ${getRefreshToken()}") }

            when (response.code) {
                CODE_TOKEN_EXPIRED -> {
                    try {
                        Timber.e("액세스 토큰 만료, 토큰 재발급 합니다.")
                        response.close()
                        return handleTokenExpired(chain, originalRequest, headerRequest)
                    } catch (t: Throwable) {
                        Timber.e("예외발생 ${t.message}")
                        saveAccessToken("", "")
                    }
                }

                CODE_INVALID_USER -> {
                    Timber.e("유효하지 않은 사용자")
                    saveAccessToken("", "")
                }
            }
            return response
        }

    }

    private fun Request.newAuthBuilder() =
        this.newBuilder().addHeader(HEADER_TOKEN, runBlocking(Dispatchers.IO) { getAccessToken() })

    private suspend fun getAccessToken(): String {
        return dataStoreRepository.getAccessToken().first() ?: ""
    }

    private suspend fun getRefreshToken(): String {
        return dataStoreRepository.getRefreshToken().first() ?: ""
    }

    private fun saveAccessToken(accessToken: String, refreshToken: String) =
        runBlocking {
            dataStoreRepository.saveAccessToken(accessToken, refreshToken)
        }

    private fun handleTokenExpired(
        chain: Interceptor.Chain,
        originalRequest: Request,
        headerRequest: Request
    ): Response {

        Timber.e("리프레시 토큰?? ")
        val refreshTokenRequest = originalRequest.newBuilder()
            .url("${BASE_URL}users/reissue")
            .post(createTokenReissueRequestBody())
            .build()
        OkHttpClient().newCall(refreshTokenRequest).execute().use { refreshTokenResponse ->
            if (refreshTokenResponse.code == 201) {
                val responseBody = refreshTokenResponse.body?.string()
                responseBody?.let { body ->
                    val responseToken = json.decodeFromString<ResponseReIssueTokenDto>(body)
                    Timber.e("리프레시 토큰?! ${responseToken}")
                    saveAccessToken(responseToken.accessToken, responseToken.refreshToken)
                }
                refreshTokenResponse.close()
                val newRequest = originalRequest.newAuthBuilder().build()
                return chain.proceed(newRequest)
            } else {
                refreshTokenResponse.close()
                Timber.e("리프레시 토큰 실패 : ${refreshTokenResponse.code}")
                saveAccessToken("", "")
                return chain.proceed(headerRequest)
            }
        }
    }
    private fun createTokenReissueRequestBody(): RequestBody {
        val accessToken = runBlocking(Dispatchers.IO) { getAccessToken() }
        val refreshToken = runBlocking(Dispatchers.IO) { getRefreshToken() }

        val requestBodyString = """
        {
            "accessToken": "$accessToken",
            "refreshToken": "$refreshToken"
        }
    """.trimIndent()

        return requestBodyString.toRequestBody("application/json".toMediaTypeOrNull())
    }

    companion object {
        private const val HEADER_TOKEN = "Authorization"
        private const val CODE_TOKEN_EXPIRED = 401
        private const val CODE_INVALID_USER = 1000
        private const val REFRESH_TOKEN = "refreshToken"
    }
}