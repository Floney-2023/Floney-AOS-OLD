package com.aos.floney.presentation.signup

import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.fragment.findNavController
import com.aos.floney.R
import com.aos.floney.databinding.FragmentOnboardThirdBinding
import com.aos.floney.databinding.FragmentSignupFirstBinding
import com.aos.floney.databinding.FragmentSignupSecondBinding
import com.aos.floney.presentation.onboard.ThirdFragment
import com.aos.floney.util.view.ErrorToast
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
@AndroidEntryPoint
class SignUpSecondFragment : BindingFragment<FragmentSignupSecondBinding>(R.layout.fragment_signup_second) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.sendMail.setOnClickListener {
            val email = binding.idText.text.toString().trim() // EditText에서 이메일 주소 가져오기

            if (email.isEmpty()) {
                // 이메일 주소를 입력하지 않은 경우 사용자에게 알림
                ErrorToast.createToast(requireContext(), "이메일 주소를 입력해주세요.")?.show()
            } else if (isEmailValid(email)) {
                // 이메일 형식이 올바른 경우 다음으로 이동
                findNavController().navigate(R.id.action_secondFragment_to_thirdFragment)
            } else {
                // 이메일 형식이 올바르지 않은 경우 사용자에게 알림
                ErrorToast.createToast(requireContext(), "유효한 이메일을 입력해주세요.")?.show()
            }
        }
    }
    private fun isEmailValid(email: String): Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }


}
