package com.aos.floney.presentation.login

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
class LoginViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _postRegisterUserState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val postRegisterUserState: StateFlow<UiState<Unit>> = _postRegisterUserState.asStateFlow()

    private val _loginState = MutableStateFlow<UiState<PostusersLoginData?>>(UiState.Empty)
    val loginState: StateFlow<UiState<PostusersLoginData?>> = _loginState.asStateFlow()

    suspend fun getDeviceToken(): String? {
        return dataStoreRepository.getDeviceToken()?.first()
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
    companion object {
        private const val FIRST_DAY = 1
    }
}