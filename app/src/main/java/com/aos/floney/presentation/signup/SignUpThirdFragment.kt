package com.aos.floney.presentation.signup

import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.aos.floney.R
import com.aos.floney.databinding.FragmentSignupThirdBinding
import com.aos.floney.util.view.ErrorToast
import com.aos.floney.util.view.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class SignUpThirdFragment : Fragment(R.layout.fragment_signup_third) {

    private lateinit var binding: FragmentSignupThirdBinding
    private lateinit var countDownTimer: CountDownTimer
    private var timerExpired = false // 타이머 만료 여부를 저장하는 변수
    private val viewModel: SignUpViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignupThirdBinding.bind(view)

        val email = requireArguments().getString("email", "")
        val marketing = requireArguments().getBoolean("marketing", false)
        Timber.e("Success : email authentication${email} ${marketing}")
        backButtonSettings()
        nextButtonSettings(email)

        setupEditTextListeners()
        startTimer()
        postusersEmailMailObserver(email, marketing)

    }

    private fun backButtonSettings() {
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    private fun nextButtonSettings(email: String) {
        binding.nextButton.setOnClickListener {
            if (timerExpired) {
                // 타이머가 만료된 경우
                ErrorToast.createToast(requireContext(),"유효 시간이 초과되었습니다. 다시 시도해 주세요.")?.show()
            } else {
                val code = generateVerificationCode()
                if (code.length == 6) {
                    viewModel.postEmailMailUser(email, code)
                } else {
                    // 숫자 입력이 덜 되었을 때
                    ErrorToast.createToast(requireContext(), "숫자 6자리를 모두 입력해주세요.")?.show()
                }
            }
        }
    }

    private fun startTimer() {
        // 타이머 텍스트 초기화
        binding.timerTextView.text = "05:00"

        countDownTimer = object : CountDownTimer(300000, 1000) { // 5분(300000밀리초) 타이머
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                val timeString = String.format("%02d:%02d", minutes, seconds)
                binding.timerTextView.text = timeString
            }

            override fun onFinish() {
                // 타이머가 종료되면 필요한 작업을 수행하세요.
                timerExpired = true // 타이머 만료 상태로 변경
            }
        }
        countDownTimer.start()
    }

    private fun setupEditTextListeners() {
        val editTexts = listOf(
            binding.editText1,
            binding.editText2,
            binding.editText3,
            binding.editText4,
            binding.editText5,
            binding.editText6
        )

        for (i in 0 until editTexts.size - 1) {
            editTexts[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    if (s?.length == 1) {
                        editTexts[i + 1].requestFocus()
                    }
                }

                override fun afterTextChanged(s: Editable?) {}
            })
        }
    }
    private fun generateVerificationCode(): String {
        val editTexts = listOf(
            binding.editText1,
            binding.editText2,
            binding.editText3,
            binding.editText4,
            binding.editText5,
            binding.editText6
        )

        val codeBuilder = StringBuilder()
        for (editText in editTexts) {
            codeBuilder.append(editText.text.toString())
        }
        return codeBuilder.toString()
    }
    private fun postusersEmailMailObserver(email: String, marketing: Boolean) {
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
            "U018" -> "코드가 올바르지 않습니다."
            "U013" -> "유효 시간이 초과되었습니다. 다시 시도해 주세요."
            else -> "알 수 없는 오류가 발생했습니다. 다시 시도해 주세요."
        }

        ErrorToast.createToast(requireContext(), errorMessage)?.show()
    }
}
