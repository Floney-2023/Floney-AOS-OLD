package com.aos.floney.presentation.home

import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentHomeBinding
import com.aos.floney.databinding.FragmentMypageBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

class MypageFragment  : BindingFragment<FragmentMypageBinding>(R.layout.fragment_mypage){
    private val viewModel: MypageViewModel by viewModels()

}