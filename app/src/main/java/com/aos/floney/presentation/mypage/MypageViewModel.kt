package com.aos.floney.presentation.mypage

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aos.floney.data.dto.request.RequestPutUsersPasswordDto
import com.aos.floney.domain.entity.GetbooksInfoData
import com.aos.floney.domain.entity.UserMypageData
import com.aos.floney.domain.entity.mypage.ReceiveMarketing
import com.aos.floney.domain.repository.CalendarRepository
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
            myPageRepository.getusersMypageData(Authorization)
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
            myPageRepository.getusersReceiveMarketingData(Authorization)
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
                authorization = Authorization,
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
                authorization = Authorization,
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
}