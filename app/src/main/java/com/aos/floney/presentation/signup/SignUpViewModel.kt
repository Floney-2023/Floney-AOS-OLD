package com.aos.floney.presentation.signup

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aos.floney.data.dto.request.PostLoginRequestDto
import com.aos.floney.data.dto.request.PostUserEmailMailRequestDto
import com.aos.floney.data.dto.request.PostUserSignupRequestDto
import com.aos.floney.data.dto.request.RequestPostRegisterUserDto
import com.aos.floney.domain.entity.GetbooksDaysData
import com.aos.floney.domain.entity.GetbooksInfoData
import com.aos.floney.domain.entity.GetbooksMonthData
import com.aos.floney.domain.entity.books.GetbooksUsersCheckData
import com.aos.floney.domain.entity.login.PostusersLoginData
import com.aos.floney.domain.entity.signup.PostusersSignupData
import com.aos.floney.domain.repository.CalendarRepository
import com.aos.floney.domain.repository.DataStoreRepository
import com.aos.floney.domain.repository.UserRepository
import com.aos.floney.util.view.UiState
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
class SignUpViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _getUserEmailMailState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val getUserEmailMailState: StateFlow<UiState<Unit>> = _getUserEmailMailState.asStateFlow()

    private val _postUserEmailMailState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val postUserEmailMailState: StateFlow<UiState<Unit>> = _postUserEmailMailState.asStateFlow()

    private val _postUserSignupState = MutableStateFlow<UiState<PostusersSignupData>>(UiState.Loading)
    val postUserSignupState: StateFlow<UiState<PostusersSignupData>> = _postUserSignupState.asStateFlow()

    suspend fun getDeviceToken(): String? {
        return dataStoreRepository.getDeviceToken()?.first()
    }

    fun getEmailMailUser(email:String){
        viewModelScope.launch {
            userRepository.getEmailMailUser(email)
                .onSuccess { response ->
                    if (response == null){
                    } else {
                        Timber.d("메일 인증 전송 성공")
                        _getUserEmailMailState.value = UiState.Success(response)
                    }
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("HTTP 실패 ${t.code()}, ${t.message()}")
                        val errorResponse = t.response()?.errorBody()?.string()
                        val json = JSONObject(errorResponse)
                        val code = json.getString("code")
                        Timber.e("액세스 ${t.message}")
                        _getUserEmailMailState.value = UiState.Failure("${code}")
                    }
                }
        }

    }
    fun postEmailMailUser(email:String,code:String){
        viewModelScope.launch {
            userRepository.postEmailMailUser(PostUserEmailMailRequestDto(email = email, code = code))
                .onSuccess { response ->
                    if (response == null){
                    } else {
                        Timber.d("메일 인증 검사 성공")
                        _postUserEmailMailState.value = UiState.Success(response)
                    }
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("HTTP 실패 ${t.code()}, ${t.message()}")
                        val errorResponse = t.response()?.errorBody()?.string()
                        val json = JSONObject(errorResponse)
                        val code = json.getString("code")
                        Timber.e("액세스 ${t.message}")
                        _postUserEmailMailState.value = UiState.Failure("${code}")
                    }
                }
        }

    }
    fun postSignupUser(signupData : PostUserSignupRequestDto){
        viewModelScope.launch {
            userRepository.postSignupUser(signupData)
                .onSuccess { response ->
                    if (response == null){
                    } else {
                        Timber.d("회원가입 성공")
                        saveAccessToken(response.accessToken, response.refreshToken)
                        _postUserSignupState.value = UiState.Success(response)
                    }
                }
                .onFailure { t ->
                    if (t is HttpException) {
                        Timber.e("HTTP 실패 ${t.code()}, ${t.message()}")
                        val errorResponse = t.response()?.errorBody()?.string()
                        val json = JSONObject(errorResponse)
                        val code = json.getString("code")
                        Timber.e("액세스 ${t.message}")
                        _postUserSignupState.value = UiState.Failure("${code}")
                    }
                }
        }

    }
    private fun saveAccessToken(accessToken: String, refreshToken: String) =
        viewModelScope.launch(Dispatchers.IO) {
            dataStoreRepository.saveAccessToken(accessToken, refreshToken)
        }
    companion object {
        private const val FIRST_DAY = 1
    }
}