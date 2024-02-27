package com.aos.floney.presentation.login

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.aos.floney.R
import com.aos.floney.databinding.ActivityLoginBinding
import com.aos.floney.presentation.home.HomeFragment
import com.aos.floney.presentation.home.YearMonthPickerFragment
import com.aos.floney.util.binding.BindingActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.singUp.setOnClickListener {

        }
        binding.startLogin.setOnClickListener{
            // 이미 가입된 계정
            val alreadySignFragment = AlreadySignFragment { year, month ->

            }
            alreadySignFragment.show(supportFragmentManager, "YearMonthPicker")

            // 로그인 성공 시, 홈 페이지
            //navigateTo<HomeFragment>()
        }

    }

    private inline fun <reified T : Fragment> navigateTo() {
        supportFragmentManager.commit {
            replace<T>(R.id.nav_host_fragment, T::class.simpleName)
        }
    }
}