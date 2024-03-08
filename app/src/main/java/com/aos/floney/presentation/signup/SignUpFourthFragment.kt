package com.aos.floney.presentation.signup

import android.os.Bundle
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
import com.aos.floney.databinding.FragmentSignupFourthBinding
import com.aos.floney.databinding.FragmentSignupThirdBinding
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
@AndroidEntryPoint
class SignUpFourthFragment : BindingFragment<FragmentSignupFourthBinding>(R.layout.fragment_signup_fourth) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.back.setOnClickListener {
            findNavController().popBackStack()
        }
        binding.nextButton.setOnClickListener {
            findNavController().navigate(R.id.action_FourthFragment_to_FifthFragment)
        }
    }

}
