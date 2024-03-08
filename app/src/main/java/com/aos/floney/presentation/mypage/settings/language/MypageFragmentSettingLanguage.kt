package com.aos.floney.presentation.mypage.settings.language

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentMypageSettingLanguageBinding
import com.aos.floney.presentation.mypage.MypageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
@AndroidEntryPoint
class MypageFragmentSettingLanguage  : BindingFragment<FragmentMypageSettingLanguageBinding>(R.layout.fragment_mypage_setting_language){
    private val viewModel: MypageViewModel by viewModels()
    private lateinit var callback: OnBackPressedCallback
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsetting()
    }
    private fun initsetting(){
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}