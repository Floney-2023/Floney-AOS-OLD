package com.aos.floney.presentation.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aos.floney.R
import com.aos.floney.databinding.FragmentOnboardThirdBinding
import com.aos.floney.databinding.FragmentSignupFirstBinding
import com.aos.floney.databinding.FragmentSignupFourthBinding
import com.aos.floney.databinding.FragmentSignupThirdBinding
import com.aos.floney.util.view.ErrorToast
import com.aos.floney.util.view.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import timber.log.Timber

@AndroidEntryPoint
class SignUpFourthFragment : BindingFragment<FragmentSignupFourthBinding>(R.layout.fragment_signup_fourth) {
    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val email = requireArguments().getString("email", "")
        val marketing = requireArguments().getBoolean("marketing", false)
        binding.idEditText.text = email

        backButtonSettings()
        nextButtonSettings()
        postusersObserver(email, marketing)
    }
    private fun backButtonSettings(){
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }
    private fun nextButtonSettings() {
        binding.nextButton.setOnClickListener {
            val password = binding.pwText.text.toString()
            val confirmPassword = binding.pwTextCheck.text.toString()
            val nickname = binding.nicknameText.text.toString()

            // 비밀번호가 비어있는 경우
            if (password.isBlank()) {
                ErrorToast.createToast(requireContext(),"비밀번호를 입력해주세요.")?.show()
                return@setOnClickListener
            }

            // 비밀번호 확인이 비어있는 경우
            if (confirmPassword.isBlank()) {
                ErrorToast.createToast(requireContext(),"비밀번호 확인을 입력해주세요.")?.show()
                return@setOnClickListener
            }

            // 비밀번호가 조건을 만족하지 않는 경우
            if (!isPasswordValid(password)) {
                ErrorToast.createToast(requireContext(),"비밀번호 양식을 확인해주세요.")?.show()
                return@setOnClickListener
            }

            // 비밀번호와 비밀번호 확인이 일치하지 않는 경우
            if (password != confirmPassword) {
                ErrorToast.createToast(requireContext(),"비밀번호가 일치하지 않습니다.")?.show()
                return@setOnClickListener
            }

            // 닉네임이 비어있는 경우
            if (nickname.isBlank()) {
                ErrorToast.createToast(requireContext(),"닉네임을 입력해주세요.")?.show()
                return@setOnClickListener
            }

            // 모든 조건을 만족하면 다음 단계로 진행
            findNavController().navigate(R.id.action_thirdFragment_to_FourthFragment)
        }
    }
    private fun postusersObserver(email: String, marketing: Boolean) {
        viewModel.postUserEmailMailState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {

                is UiState.Loading -> Unit

                is UiState.Success -> {

                    val bundle = Bundle().apply {
                        putString("email", email) // 이메일 값을 번들에 넣음
                        putBoolean("marketing", marketing)
                    }
                    // 타이머가 만료되지 않은 경우
                    findNavController().navigate(R.id.action_thirdFragment_to_FourthFragment,bundle)
                }

                is UiState.Failure -> {

                    Timber.e("Success : email authentication${state} ")
                    handlePasswordError(state.msg)
                }

                is UiState.Empty -> Unit
            }
        }.launchIn(lifecycleScope)
    }
    private fun handlePasswordError(errorCode: String) {
        val errorMessage = when (errorCode) {
            "U001" -> "이미 존재하는 유저입니다."
            else -> "알 수 없는 오류가 발생했습니다. 다시 시도해 주세요."
        }

        ErrorToast.createToast(requireContext(), errorMessage)?.show()
    }

    // 비밀번호 유효성 검사
    private fun isPasswordValid(password: String): Boolean {
        // 비밀번호 조건을 정의하고 해당 조건을 만족하는지 검사하는 코드를 작성하세요
        val passwordRegex = "^(?=.*[0-9])(?=.*[a-zA-Z])(?=.*[@#$%^&+=])(?=\\S+$).{8,}$".toRegex()
        return passwordRegex.matches(password)
    }


}
