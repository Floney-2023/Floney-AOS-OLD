package com.aos.floney.presentation.mypage.inform.exit

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentMypageInformExitFirstBinding
import com.aos.floney.databinding.FragmentMypageInformExitSecondBinding
import com.aos.floney.databinding.FragmentMypageInformExitThirdBinding
import com.aos.floney.databinding.FragmentMypageInformProfilechangeBinding
import com.aos.floney.databinding.FragmentMypageSettingBinding
import com.aos.floney.presentation.login.LoginActivity
import com.aos.floney.presentation.mypage.MypageViewModel
import com.aos.floney.presentation.mypage.settings.alarm.MypageFragmentSettingAlarm
import com.aos.floney.presentation.mypage.settings.language.MypageFragmentSettingLanguage
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
@AndroidEntryPoint
class MypageFragmentInformExitThird  : BindingFragment<FragmentMypageInformExitThirdBinding>(R.layout.fragment_mypage_inform_exit_third){
    private val viewModel: MypageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsetting()
    }
    private fun initsetting(){
        binding.nextButton.setOnClickListener{
            navigateActivityTo<LoginActivity>()
        }
    }
    private inline fun <reified T : Fragment> navigateTo() {
        parentFragmentManager.commit {
            replace<T>(R.id.mypageFragmentSetting, T::class.simpleName)
            addToBackStack(ROOT_FRAGMENT_HOME_SETTING)
        }
    }
    private inline fun <reified T : Activity> navigateActivityTo() {
        val intent = Intent(getActivity(), T::class.java)
        startActivity(intent)
    }
    companion object {
        private const val ROOT_FRAGMENT_HOME_SETTING = "MypageFragmentInformExitSecond"
    }
}