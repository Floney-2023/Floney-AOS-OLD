package com.aos.floney.presentation.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.aos.floney.R
import com.aos.floney.databinding.ActivityLoginBinding
import com.aos.floney.presentation.HomeActivity
import com.aos.floney.presentation.MainViewModel
import com.aos.floney.presentation.home.HomeFragment
import com.aos.floney.presentation.home.YearMonthPickerFragment
import com.aos.floney.presentation.signup.SignUpActivity
import com.aos.floney.util.binding.BindingActivity
import com.aos.floney.util.view.ErrorToast
import com.aos.floney.util.view.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

@AndroidEntryPoint
class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val viewModel by viewModels<LoginViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.singUp.setOnClickListener {
            navigateActivityTo<SignUpActivity>()
        }
        settingLoginButton()
        initLoginObserver()
    }
    private fun settingLoginButton(){
        binding.startLogin.setOnClickListener{
            // 이미 가입된 계정
//            val alreadySignFragment = AlreadySignFragment { year, month ->
//
//            }
//            alreadySignFragment.show(supportFragmentManager, "YearMonthPicker")


            viewModel.postuserLogin(binding.idText.text.toString(),binding.pwText.text.toString())
        }
    }
    private fun initLoginObserver() {
        viewModel.loginState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Loading -> {

                }

                is UiState.Success -> {

                    navigateActivityTo<HomeActivity>()

                }

                is UiState.Failure -> {
                    handlePasswordError(state.msg)
                }

                is UiState.Empty -> {
                }
            }
        }.launchIn(lifecycleScope)
    }
    private fun handlePasswordError(errorCode: String) {
        val errorMessage = when (errorCode) {
            "U008" -> "가입된 정보가 없습니다"
            "U009" -> "이메일 또는 비밀번호를 다시 확인하세요"
            "U022" -> "현재 비밀번호가 일치하지 않습니다"
            else -> "알 수 없는 오류가 발생했습니다"
        }

        ErrorToast.createToast(this, errorMessage)?.show()
    }
    private inline fun <reified T : Fragment> navigateTo() {
        supportFragmentManager.commit {
            replace<T>(R.id.nav_host_fragment, T::class.simpleName)
        }
    }
    private inline fun <reified T : Activity> navigateActivityTo() {
        Intent(this, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }
}