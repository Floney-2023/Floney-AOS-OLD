package com.aos.floney.presentation.mypage

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aos.floney.data.dto.request.RequestPostUsersBookKeyDto
import com.aos.floney.data.dto.request.RequestPutUsersPasswordDto
import com.aos.floney.domain.entity.mypage.UserMypageData
import com.aos.floney.domain.entity.mypage.ReceiveMarketing
import com.aos.floney.domain.repository.MyPageRepository
import com.aos.floney.util.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.util.Calendar
import javax.inject.Inject
@HiltViewModel
class MypageViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository
): ViewModel() {

    private val _getusersMypageState =
        MutableStateFlow<UiState<UserMypageData>>(UiState.Loading)
    val getusersMypageState: StateFlow<UiState<UserMypageData>> =
        _getusersMypageState.asStateFlow()

    private val _getusersReceiveMarketingState =
        MutableStateFlow<UiState<ReceiveMarketing>>(UiState.Loading)
    val getusersReceiveMarketingState: StateFlow<UiState<ReceiveMarketing>> =
        _getusersReceiveMarketingState.asStateFlow()
    init {
        updatemypageItems()
        updateusersReceiveMarketing()
    }

    fun updatemypageItems()
    {
        viewModelScope.launch {
            myPageRepository.getusersMypageData()
                .onSuccess { response ->
                    _getusersMypageState.value =
                        UiState.Success(response)
                    Log.d("myPage", "onsuccess: $response")
                }.onFailure { t ->
                    Log.d("myPage", "onfailure: ${t}")
                    _getusersMypageState.value = UiState.Failure("${t.message}")
                }
        }
    }
    fun updateusersReceiveMarketing()
    {
        viewModelScope.launch {
            myPageRepository.getusersReceiveMarketingData()
                .onSuccess { response ->
                    _getusersReceiveMarketingState.value =
                        UiState.Success(response)
                    Log.d("myPage", "onsuccess: $response")
                }.onFailure { t ->
                    Log.d("myPage", "onfailure: ${t}")
                    _getusersReceiveMarketingState.value = UiState.Failure("${t.message}")
                }
        }
    }
    private val _putusersReceiveMarketingState =
        MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val putusersReceiveMarketingState: StateFlow<UiState<Unit>> =
        _putusersReceiveMarketingState.asStateFlow()
    fun putusersReceiveMarketing(agree : Boolean)
    {
        viewModelScope.launch {
            _putusersReceiveMarketingState.value = UiState.Loading
            myPageRepository.putusersReceiveMarketingData(
                agree = agree
            )
                .onSuccess { response ->
                    _putusersReceiveMarketingState.value = UiState.Success(response)
                    Timber.e("성공 ${UiState.Success(response)}")
                }.onFailure { t ->
                    if (t is HttpException) {
                        val errorResponse = t.response()?.errorBody()?.string()
                        Timber.e("HTTP 실패: $errorResponse")
                    }
                    _putusersReceiveMarketingState.value = UiState.Failure("${t.message}")
                }
        }
    }

    /*비밀번호 변경*/
    private val _putusersPasswordState =
        MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val putusersPasswordState: StateFlow<UiState<Unit>> =
        _putusersPasswordState.asStateFlow()
    fun putusersPassword(newPassword: String, oldPassword :String)
    {
        viewModelScope.launch {
            _putusersPasswordState.value = UiState.Loading
            myPageRepository.putusersPasswordData(
                RequestPutUsersPasswordDto(
                    newPassword, oldPassword
                )
            )
                .onSuccess { response ->
                    _putusersPasswordState.value = UiState.Success(response)
                    Timber.e("성공 ${UiState.Success(response)}")
                }.onFailure { t ->
                    if (t is HttpException) {
                        val errorResponse = t.response()?.errorBody()?.string()
                        val json = JSONObject(errorResponse)
                        val code = json.getString("code")
                        Timber.e("HTTP 실패: $errorResponse")
                        _putusersPasswordState.value = UiState.Failure("${code}")
                    }

                }
        }
    }
    /*닉네임 변경*/
    private val _getusersNicknameUpdateState =
        MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val getusersNicknameUpdateState: StateFlow<UiState<Unit>> =
        _getusersNicknameUpdateState.asStateFlow()
    fun getusersNicknameUpdate(nickname :String)
    {
        viewModelScope.launch {
            _getusersNicknameUpdateState.value = UiState.Loading
            myPageRepository.getusersNicknameUpdate(
                nickname = nickname
            )
                .onSuccess { response ->
                    _getusersNicknameUpdateState.value = UiState.Success(response)
                    Timber.e("성공 ${UiState.Success(response)}")
                }.onFailure { t ->
                    if (t is HttpException) {
                        val errorResponse = t.response()?.errorBody()?.string()
                        val json = JSONObject(errorResponse)
                        val code = json.getString("code")
                        Timber.e("HTTP 실패: $errorResponse")
                        _getusersNicknameUpdateState.value = UiState.Failure("${code}")
                    }

                }
        }
    }
    /*최신 접근 가계부 불러오기*/
    /*닉네임 변경*/
    private val _getusersBookKeyState =
        MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val getusersBookKeyState: StateFlow<UiState<Unit>> =
        _getusersBookKeyState.asStateFlow()
    fun getusersBookKey(bookKey :String)
    {
        viewModelScope.launch {
            _getusersBookKeyState.value = UiState.Loading
            myPageRepository.getusersBookKey(
                requestPostUsersBookKeyDto = RequestPostUsersBookKeyDto(bookKey)
            )
                .onSuccess { response ->
                    _getusersBookKeyState.value = UiState.Success(response)
                    Timber.e("성공 ${UiState.Success(response)}")
                }.onFailure { t ->
                    if (t is HttpException) {
                        val errorResponse = t.response()?.errorBody()?.string()
                        val json = JSONObject(errorResponse)
                        val code = json.getString("code")
                        Timber.e("HTTP 실패: $errorResponse")
                        _getusersBookKeyState.value = UiState.Failure("${code}")
                    }

                }
        }
    }
}