package com.aos.floney.presentation.login

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aos.floney.data.dto.request.PostLoginRequestDto
import com.aos.floney.data.dto.request.RequestPostRegisterUserDto
import com.aos.floney.domain.entity.GetbooksDaysData
import com.aos.floney.domain.entity.GetbooksInfoData
import com.aos.floney.domain.entity.GetbooksMonthData
import com.aos.floney.domain.entity.books.GetbooksUsersCheckData
import com.aos.floney.domain.entity.login.PostusersLoginData
import com.aos.floney.domain.repository.CalendarRepository
import com.aos.floney.domain.repository.DataStoreRepository
import com.aos.floney.domain.repository.KakaoLoginRepository
import com.aos.floney.domain.repository.UserRepository
import com.aos.floney.util.view.UiState
import com.kakao.sdk.auth.model.OAuthToken
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val userRepository: UserRepository,
    private val kakaoLoginRepository: KakaoLoginRepository
) : ViewModel() {

    private val _postRegisterUserState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val postRegisterUserState: StateFlow<UiState<Unit>> = _postRegisterUserState.asStateFlow()

    private val _loginState = MutableStateFlow<UiState<PostusersLoginData?>>(UiState.Empty)
    val loginState: StateFlow<UiState<PostusersLoginData?>> = _loginState.asStateFlow()

    private val _socialloginState = MutableStateFlow<UiState<Unit?>>(UiState.Empty)
    val socialloginState: StateFlow<UiState<Unit?>> = _socialloginState.asStateFlow()

    suspend fun getDeviceToken(): String? {
        return dataStoreRepository.getDeviceToken()?.first()
    }

    private val kakaoLoginCallback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        KakaoLoginCallback {
            Timber.d("kakao 액세스토큰 ${token?.accessToken}")
            Timber.d("kakao 리프레시토큰 ${token?.refreshToken}")
            if (token != null) {
                saveSocialToken(token.accessToken, token.refreshToken)
                postLogin(token.accessToken, KAKAO)
            } else {
                Timber.e("token is null")
            }
        }.handleResult(token, error)
    }
    private fun postLogin(socialToken: String, socialType: String) {
        viewModelScope.launch {
            _socialloginState.value = UiState.Loading

            userRepository.postSocialLogin(socialType, socialToken)
                .onSuccess { response ->
                    if (response != null) {
                        Timber.d("로그인 성공")
                        //Timber.d("액세스 : ${response.accessToken} \n 리프레시 : ${response.refreshToken}")

                        //saveAccessToken(response.accessToken, response.refreshToken)
                        //saveUserId(response.userId)

                        _socialloginState.value = UiState.Success(response)
                    } else {
                        Timber.e("response is null")
                    }
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("HTTP 실패 ${t.code()}, ${t.message()}")
                    }
                    Timber.e("${t.message}")
                    _loginState.value = UiState.Failure("${t.message}")
                }
        }
    }

    private fun saveSocialToken(socialAccessToken: String, socialRefreshToken: String) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveSocialToken(socialAccessToken, socialRefreshToken)
        }

    fun postuserLogin(email:String, password : String){
        viewModelScope.launch {
            Timber.d("액세스 : ${email} \n 리프레시 : ${password}")
            userRepository.postLoginUser(PostLoginRequestDto(email,password))
                .onSuccess { response ->
                    if (response != null){
                        Timber.d("로그인 성공")
                        Timber.d("액세스 : ${response.accessToken} \n 리프레시 : ${response.refreshToken}")
                        saveAccessToken(response.accessToken, response.refreshToken)
                        _loginState.value = UiState.Success(response)

                    } else {
                        Timber.e("response is null")
                    }
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("HTTP 실패 ${t.code()}, ${t.message()}")
                        val errorResponse = t.response()?.errorBody()?.string()
                        val json = JSONObject(errorResponse)
                        val code = json.getString("code")
                        Timber.e("액세스 ${t.message}")
                        _loginState.value = UiState.Failure("${code}")
                    }
                }
        }

    }
    private fun saveAccessToken(accessToken: String, refreshToken: String) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveAccessToken(accessToken, refreshToken)
        }


    fun kakaoLogin(context: Context) {
        kakaoLoginRepository.loginKakao(kakaoLoginCallback, context)
    }

    companion object {
        private const val KAKAO = "KAKAO"
    }
}