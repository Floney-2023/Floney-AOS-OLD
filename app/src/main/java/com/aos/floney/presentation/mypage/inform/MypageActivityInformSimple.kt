package com.aos.floney.presentation.mypage.inform

import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.aos.floney.R
import com.aos.floney.databinding.ActivityMypageInformSimpleloginBinding
import com.aos.floney.presentation.mypage.MypageViewModel
import com.aos.floney.util.binding.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MypageActivityInformSimple  : BindingActivity<ActivityMypageInformSimpleloginBinding>(R.layout.activity_mypage_inform_simplelogin){
    private val viewModel: MypageViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initsetting()
    }
    private fun initsetting(){


    }
    private inline fun <reified T : Fragment> navigateTo() {
        supportFragmentManager.commit {
            replace<T>(R.id.mypageInformEmail, T::class.simpleName)
            addToBackStack(ROOT_FRAGMENT_HOME_SETTING)
        }
    }
    companion object {
        private const val ROOT_FRAGMENT_HOME_SETTING = "MypageInformSimpleLogin"
    }
}