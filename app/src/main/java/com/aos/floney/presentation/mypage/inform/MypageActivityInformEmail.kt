package com.aos.floney.presentation.mypage.inform

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.aos.floney.R
import com.aos.floney.databinding.ActivityMypageInformEmailloginBinding
import com.aos.floney.databinding.ActivityMypageInformSimpleloginBinding
import com.aos.floney.presentation.mypage.MypageViewModel
import com.aos.floney.presentation.mypage.inform.exit.MypageFragmentInformExitFirst
import com.aos.floney.presentation.mypage.inform.profileImg.MypageFragmentInformProfileImg
import com.aos.floney.presentation.mypage.inform.pwChange.MypageFragmentInformpwChange
import com.aos.floney.util.binding.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageActivityInformEmail  : BindingActivity<ActivityMypageInformEmailloginBinding>(R.layout.activity_mypage_inform_emaillogin){
    private val viewModel: MypageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initsetting()

    }
    private fun initsetting(){
        backButtonClick()
        profileChangeClick()
        passWordChangeClick()
        logoutClick()
        exitClick()
    }
    private fun backButtonClick(){
        binding.backButton.setOnClickListener {
            finish()
        }
    }
    private fun profileChangeClick(){
        binding.profileImageChange.setOnClickListener {
            navigateTo<MypageFragmentInformProfileImg>()
        }
    }
    private fun passWordChangeClick(){
        binding.pwChange.setOnClickListener {
            navigateTo<MypageFragmentInformpwChange>()
        }
    }
    private fun logoutClick(){
        binding.logout.setOnClickListener {

        }
    }
    private fun exitClick(){
        binding.exit.setOnClickListener {
            navigateTo<MypageFragmentInformExitFirst>()
        }
    }

    private inline fun <reified T : Fragment> navigateTo() {
        supportFragmentManager.commit {
            replace<T>(R.id.mypageInformEmail, T::class.simpleName)
            addToBackStack(ROOT_FRAGMENT_HOME_SETTING)
        }
    }
    companion object {
        private const val ROOT_FRAGMENT_HOME_SETTING = "MypageInfromEmailLogin"
    }
}