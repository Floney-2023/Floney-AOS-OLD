package com.aos.floney.presentation.signup

import android.os.Bundle
import android.util.Patterns
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
import com.aos.floney.databinding.FragmentSignupSecondBinding
import com.aos.floney.presentation.mypage.MypageViewModel
import com.aos.floney.presentation.onboard.ThirdFragment
import com.aos.floney.util.view.ErrorToast
import com.aos.floney.util.view.SampleToast
import com.aos.floney.util.view.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import timber.log.Timber

@AndroidEntryPoint
class SignUpSecondFragment : BindingFragment<FragmentSignupSecondBinding>(R.layout.fragment_signup_second) {

    private val viewModel: SignUpViewModel by viewModels()
    private var marketing : Boolean = false
    private lateinit var email : String
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        marketing = requireArguments().getBoolean("marketing", false)

        getusersEmailMailObserver()

        binding.sendMail.setOnClickListener {
            email = binding.idText.text.toString().trim() // EditText에서 이메일 주소 가져오기

            if (email.isEmpty()) {
                // 이메일 주소를 입력하지 않은 경우 사용자에게 알림
                ErrorToast.createToast(requireContext(), "이메일 주소를 입력해주세요.")?.show()
            } else if (isEmailValid(email)) {
                viewModel.getEmailMailUser(email)
            } else {
                // 이메일 형식이 올바르지 않은 경우 사용자에게 알림
                ErrorToast.createToast(requireContext(), "유효한 이메일을 입력해주세요.")?.show()
            }
        }
    }
    private fun getusersEmailMailObserver() {
        viewModel.getUserEmailMailState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {

                is UiState.Loading -> Unit

                is UiState.Success -> {
                    val bundle = Bundle().apply {
                        putString("email", email) // 이메일 값을 번들에 넣음
                        putBoolean("marketing", marketing)
                    }
                    findNavController().navigate(R.id.action_secondFragment_to_thirdFragment, bundle)
                }

                is UiState.Failure -> {

                    Timber.e("Success : Second${state} ")
                    handlePasswordError(state.msg)
                }

                is UiState.Empty -> Unit
            }
        }.launchIn(lifecycleScope)
    }
    private fun handlePasswordError(errorCode: String) {
        val errorMessage = when (errorCode) {
            "U001" -> "이미 등록된 이메일 입니다."
            else -> "알 수 없는 오류가 발생했습니다. 다시 시도해 주세요."
        }

        ErrorToast.createToast(requireContext(), errorMessage)?.show()
    }
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


}
