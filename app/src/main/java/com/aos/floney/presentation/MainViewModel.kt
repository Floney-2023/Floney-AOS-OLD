package com.aos.floney.presentation

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aos.floney.data.dto.request.RequestPostRegisterUserDto
import com.aos.floney.domain.entity.GetbooksDaysData
import com.aos.floney.domain.entity.GetbooksInfoData
import com.aos.floney.domain.entity.GetbooksMonthData
import com.aos.floney.domain.entity.books.GetbooksUsersCheckData
import com.aos.floney.domain.repository.CalendarRepository
import com.aos.floney.domain.repository.DataStoreRepository
import com.aos.floney.domain.repository.UserRepository
import com.aos.floney.util.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.Random
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dataStoreRepository: DataStoreRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    val Authorization = "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJ3bnNnbWw1MTdAZ21haWwuY29tIiwiaWF0IjoxNzEwMjIzNjI3LCJleHAiOjE3MTAyMjcyMjd9.4sZFeE6njLnOVq-E8_BwEBAm5bpTCmqd1_ze--ZDLsMVkkR6R_AaZcDVvKnYcRrP"

    private val _postRegisterUserState = MutableStateFlow<UiState<Unit>>(UiState.Loading)
    val postRegisterUserState: StateFlow<UiState<Unit>> = _postRegisterUserState.asStateFlow()

    suspend fun getDeviceToken(): String? {
        return dataStoreRepository.getDeviceToken()?.first()
    }

    fun postRegisterUser(deviceToken: String) {
        viewModelScope.launch {
            userRepository.postRegisterUser(
                RequestPostRegisterUserDto(
                    deviceToken = deviceToken
                )
            ).onSuccess { response ->
                _postRegisterUserState.value = UiState.Success(response)
            }.onFailure { t ->
                _postRegisterUserState.value = UiState.Failure("${t.message}")
            }
        }
    }
    companion object {
        private const val FIRST_DAY = 1
    }
}