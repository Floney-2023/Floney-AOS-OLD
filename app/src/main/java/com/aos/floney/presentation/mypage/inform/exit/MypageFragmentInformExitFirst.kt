package com.aos.floney.presentation.mypage.inform.exit

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentMypageInformExitFirstBinding
import com.aos.floney.databinding.FragmentMypageInformProfilechangeBinding
import com.aos.floney.databinding.FragmentMypageSettingBinding
import com.aos.floney.presentation.mypage.MypageViewModel
import com.aos.floney.presentation.mypage.settings.alarm.MypageFragmentSettingAlarm
import com.aos.floney.presentation.mypage.settings.language.MypageFragmentSettingLanguage
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
@AndroidEntryPoint
class MypageFragmentInformExitFirst  : BindingFragment<FragmentMypageInformExitFirstBinding>(R.layout.fragment_mypage_inform_exit_first){
    private val viewModel: MypageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsetting()
    }
    private fun initsetting(){

        binding.back.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
        binding.nextButton.setOnClickListener{
            navigateTo<MypageFragmentInformExitSecond>()
        }
    }
    private inline fun <reified T : Fragment> navigateTo() {
        parentFragmentManager.commit {
            replace<T>(R.id.MypageFragmentInformExitFirst, T::class.simpleName)
            addToBackStack(ROOT_FRAGMENT_HOME_SETTING)
        }
    }
    companion object {
        private const val ROOT_FRAGMENT_HOME_SETTING = "MypageFragmentInformExitFirst"
    }
}