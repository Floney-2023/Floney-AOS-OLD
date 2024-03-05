package com.aos.floney.presentation.mypage.settings.alarm

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentMypageSettingAlarmBinding
import com.aos.floney.databinding.FragmentMypageSettingBinding
import com.aos.floney.presentation.mypage.MypageViewModel
import com.aos.floney.presentation.mypage.settings.MypageFragmentSetting
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
@AndroidEntryPoint
class MypageFragmentSettingAlarm  : BindingFragment<FragmentMypageSettingAlarmBinding>(R.layout.fragment_mypage_setting_alarm){
    private val viewModel: MypageViewModel by viewModels()
    private var share : Boolean = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsetting()
    }
    private fun initsetting(){
        switchToggle()
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
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