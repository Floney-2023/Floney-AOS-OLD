package com.aos.floney.presentation.signup
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.aos.floney.R
import com.aos.floney.databinding.FragmentSignupThirdBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SignUpThirdFragment : Fragment(R.layout.fragment_signup_third) {

    private lateinit var binding: FragmentSignupThirdBinding
    private lateinit var countDownTimer: CountDownTimer

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSignupThirdBinding.bind(view)

        // 타이머 텍스트 초기화
        binding.timerTextView.text = "05:00"

        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }

        binding.nextButton.setOnClickListener {
            findNavController().navigate(R.id.action_thirdFragment_to_FourthFragment)
        }

        setupEditTextListeners()

        // 5분 타이머 시작
        startTimer()
    }

    private fun startTimer() {
        countDownTimer = object : CountDownTimer(300000, 1000) { // 5분(300000밀리초) 타이머
            override fun onTick(millisUntilFinished: Long) {
                val minutes = (millisUntilFinished / 1000) / 60
                val seconds = (millisUntilFinished / 1000) % 60
                val timeString = String.format("%02d:%02d", minutes, seconds)
                binding.timerTextView.text = timeString
            }

            override fun onFinish() {
                // 타이머가 종료되면 필요한 작업을 수행하세요.
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

    override fun onDestroy() {
        super.onDestroy()
        // Fragment가 종료될 때 타이머를 중지하여 메모리 누수를 방지합니다.
        if (::countDownTimer.isInitialized) {
            countDownTimer.cancel()
        }
    }
}
