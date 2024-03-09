package com.aos.floney.presentation.mypage.settings.alarm

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.activity.addCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.aos.floney.R
import com.aos.floney.databinding.FragmentMypageSettingAlarmBinding
import com.aos.floney.databinding.FragmentMypageSettingBinding
import com.aos.floney.presentation.mypage.MypageViewModel
import com.aos.floney.presentation.mypage.settings.MypageFragmentSetting
import com.aos.floney.util.fragment.viewLifeCycle
import com.aos.floney.util.fragment.viewLifeCycleScope
import com.aos.floney.util.view.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import timber.log.Timber

@AndroidEntryPoint
class MypageFragmentSettingAlarm  : BindingFragment<FragmentMypageSettingAlarmBinding>(R.layout.fragment_mypage_setting_alarm){
    private val viewModel: MypageViewModel by viewModels()
    private var share : Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsetting()

    }
    private fun initsetting(){
        getMarketing()
        callbackSetting()
        backbuttonSetting()
        putusersReceiveMarketingStateObserver()
    }
    private fun backbuttonSetting(){
        binding.backButton.setOnClickListener {
            viewModel.putusersReceiveMarketing(share)
            parentFragmentManager.popBackStack()
        }
    }
    private fun callbackSetting(){
        // 뒤로가기 이벤트 감지
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner) {
            Log.d("BackButton", "Back button is pressed")
            viewModel.putusersReceiveMarketing(share)

        }

    }
    private fun getMarketing(){
        viewModel.getusersReceiveMarketingState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    share=state.data.receiveMarketing
                    switchToggle()
                }
                is UiState.Failure -> Timber.e("Failure : ${state.msg}")
                is UiState.Empty -> Unit
                is UiState.Loading -> {
                    //activateLoadingProgressBar()
                }
            }
        }.launchIn(viewLifeCycleScope)
    }
    private fun putusersReceiveMarketingStateObserver(){
        viewModel.putusersReceiveMarketingState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    parentFragmentManager.popBackStack() // 뒤로가기를 처리
                }
                is UiState.Failure -> Timber.e("Failure : ${state.msg}")
                is UiState.Empty -> Unit
                is UiState.Loading -> Unit
            }
        }.launchIn(viewLifeCycleScope)
    }
    private fun switchToggle() {
        val toggle = binding.categoryToggleIv
        // 첫 진입 시 토글 이미지 세팅
        toggle.isChecked = share
        // 토글 클릭 시 이미지 세팅
        toggle.setOnClickListener {
            toggle.isChecked = !share
            share = !share
        }
    }
}