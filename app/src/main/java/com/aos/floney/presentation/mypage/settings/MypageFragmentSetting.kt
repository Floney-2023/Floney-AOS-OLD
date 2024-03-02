package com.aos.floney.presentation.mypage.settings

import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentMypageBinding
import com.aos.floney.databinding.FragmentMypageSettingBinding
import com.aos.floney.presentation.mypage.MypageViewModel
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

class MypageFragmentSetting  : BindingFragment<FragmentMypageSettingBinding>(R.layout.fragment_mypage_setting){
    private val viewModel: MypageViewModel by viewModels()

}