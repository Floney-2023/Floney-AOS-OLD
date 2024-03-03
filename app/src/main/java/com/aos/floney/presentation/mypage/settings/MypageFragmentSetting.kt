package com.aos.floney.presentation.mypage.settings

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentMypageSettingBinding
import com.aos.floney.presentation.mypage.MypageViewModel
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

class MypageFragmentSetting  : BindingFragment<FragmentMypageSettingBinding>(R.layout.fragment_mypage_setting){
    private val viewModel: MypageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsetting()
    }
    private fun initsetting(){
        binding.alarmView.setOnClickListener{

        }
        binding.languageView.setOnClickListener {

        }
    }
    private inline fun <reified T : Fragment> navigateTo() {
        childFragmentManager.commit {
            replace<T>(R.id.mypageFragmentSetting, T::class.simpleName)
        }
    }
}