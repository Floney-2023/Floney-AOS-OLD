package com.aos.floney.presentation.mypage

import android.util.Log
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aos.floney.domain.entity.GetbooksInfoData
import com.aos.floney.domain.entity.UserMypageData
import com.aos.floney.domain.repository.CalendarRepository
import com.aos.floney.domain.repository.MyPageRepository
import com.aos.floney.util.view.UiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
@HiltViewModel
class MypageViewModel @Inject constructor(
    private val myPageRepository: MyPageRepository
): ViewModel() {

    private val _getusersMypageState =
        MutableStateFlow<UiState<UserMypageData>>(UiState.Loading)
    val getusersMypageState: StateFlow<UiState<UserMypageData>> =
        _getusersMypageState.asStateFlow()

    init {
        updatemypageItems()
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
}