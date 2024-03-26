package com.aos.floney.presentation.signup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.aos.floney.R
import com.aos.floney.databinding.FragmentOnboardThirdBinding
import com.aos.floney.databinding.FragmentSignupFifthBinding
import com.aos.floney.databinding.FragmentSignupFirstBinding
import com.aos.floney.presentation.HomeActivity
import com.aos.floney.presentation.login.LoginActivity
import com.aos.floney.util.fragment.viewLifeCycle
import com.aos.floney.util.fragment.viewLifeCycleScope
import com.aos.floney.util.view.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import timber.log.Timber

@AndroidEntryPoint
class SignUpFifthFragment : BindingFragment<FragmentSignupFifthBinding>(R.layout.fragment_signup_fifth) {
    private val viewModel: SignUpViewModel by viewModels()
    override fun onStart() {
        super.onStart()
        viewModel.updatemypageItems() // 닉네임 정보 불러오기

    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        updateMyPageItemObserver() // 정보 변경 관찰
        settingStartButton() // 플로니 시작하기 버튼

    }
    fun settingStartButton(){
        binding.startFloney.setOnClickListener {
            navigateTo<LoginActivity>() // 가계부 추가 부분으로 이루어져야함. (로그인 화면으로 가게 임시로 설정)
        }
    }
    fun updateMyPageItemObserver(){
        viewModel.getusersMypageState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data.myBooks?.isEmpty() == true) {
                        // 값이 없을 경우 로직 구현
                    } else {
                        settingNickname(state.data.nickname)
                    }
                }

                is UiState.Failure -> Timber.e("Failure : ${state.msg}")
                is UiState.Empty -> Unit
                is UiState.Loading -> {
                    //activateLoadingProgressBar()
                }
            }
        }.launchIn(viewLifeCycleScope)
    }
    private fun settingNickname(nickname : String){
        binding.textView2.text = nickname
    }
    private inline fun <reified T : Activity> navigateTo() {
        Intent(requireActivity(), T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }
}
