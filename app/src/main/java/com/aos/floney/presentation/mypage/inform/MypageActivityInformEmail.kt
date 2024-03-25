package com.aos.floney.presentation.mypage.inform

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.activity.viewModels
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.aos.floney.R
import com.aos.floney.databinding.ActivityMypageInformEmailloginBinding
import com.aos.floney.databinding.ActivityMypageInformSimpleloginBinding
import com.aos.floney.presentation.login.LoginActivity
import com.aos.floney.presentation.mypage.MypageFragment
import com.aos.floney.presentation.mypage.MypageViewModel
import com.aos.floney.presentation.mypage.inform.exit.MypageFragmentInformExitFirst
import com.aos.floney.presentation.mypage.inform.profileImg.MypageFragmentInformProfileImg
import com.aos.floney.presentation.mypage.inform.pwChange.MypageFragmentInformpwChange
import com.aos.floney.util.binding.BindingActivity
import com.aos.floney.util.view.ErrorToast
import com.aos.floney.util.view.SampleToast
import com.aos.floney.util.view.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

@AndroidEntryPoint
class MypageActivityInformEmail  : BindingActivity<ActivityMypageInformEmailloginBinding>(R.layout.activity_mypage_inform_emaillogin){
    private val viewModel by viewModels<MypageViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initsetting()

    }
    private fun initsetting(){
        nicknameHintSetting()
        backButtonClick()
        nicknameChangeClick()
        profileChangeClick()
        passWordChangeClick()
        logoutClick()
        exitClick()
        getusersNicknameUpdateObserver()
        getusersLogoutObserver()
    }
    private fun nicknameHintSetting(){
        val nickname = intent.getStringExtra("nickname")
        binding.nicknameEditText.hint = nickname
    }
    private fun backButtonClick(){
        binding.backButton.setOnClickListener {
            finish()
        }
    }
    private fun nicknameChangeClick(){
        binding.nicknameChangeButton.setOnClickListener {
            if (binding.nicknameEditText.text.toString().isEmpty())
                SampleToast.createToast(this,"닉네임을 입력해주세요.")?.show()
            else {
                viewModel.getusersNicknameUpdate(binding.nicknameEditText.text.toString())
                // 키보드 숨기기
                hideKeyboard()

            }
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
            viewModel.getusersLogout()
        }
    }
    private fun exitClick(){
        binding.exit.setOnClickListener {
            navigateTo<MypageFragmentInformExitFirst>()
        }
    }
    private fun getusersNicknameUpdateObserver() {
        viewModel.getusersNicknameUpdateState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {

                is UiState.Loading -> Unit

                is UiState.Success -> {
                    Timber.d("Success : changeNickname ")
                    SampleToast.createToast(this, "변경이 완료되었습니다.")?.show()
                }

                is UiState.Failure -> {
                    handlePasswordError(state.msg)
                }

                is UiState.Empty -> Unit
            }
        }.launchIn(lifecycleScope)
    }
    private fun getusersLogoutObserver() {
        viewModel.getusersLogoutState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {

                is UiState.Loading -> Unit

                is UiState.Success -> {

                }

                is UiState.Failure -> {
                    //handlePasswordError(state.msg)
                    when(state.msg){
                        "" -> navigateToLogin()
                        "U007" -> ErrorToast.createToast(this,"올바르지 않은 토큰입니다.")?.show()
                    }
                }

                is UiState.Empty -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun handlePasswordError(errorCode: String) {
        val errorMessage = when (errorCode) {
            "U008" -> "해당 이메일로 가입된 유저가 없습니다"
            else -> "알 수 없는 오류가 발생했습니다. 다시 시도해 주세요."
        }

        SampleToast.createToast(this, errorMessage)?.show()
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    private fun navigateToLogin() {
        navigateActivityTo<LoginActivity>()
    }
    private inline fun <reified T : Activity> navigateActivityTo() {
        Intent(this, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }
    private inline fun <reified T : Fragment> navigateTo() {
        window.decorView.findViewById<View>(android.R.id.content).isClickable = false

        supportFragmentManager.commit {
            replace<T>(R.id.mypageInformEmail, T::class.simpleName)
            addToBackStack(ROOT_FRAGMENT_HOME_SETTING)
        }
    }
    companion object {
        private const val ROOT_FRAGMENT_HOME_SETTING = "MypageInfromEmailLogin"
    }
}