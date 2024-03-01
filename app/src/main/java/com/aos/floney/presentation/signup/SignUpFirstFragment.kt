package com.aos.floney.presentation.signup

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.CheckBox
import androidx.appcompat.widget.AppCompatCheckBox
import androidx.navigation.fragment.findNavController
import com.aos.floney.R
import com.aos.floney.databinding.FragmentSignupFirstBinding
import com.aos.floney.presentation.login.LoginActivity
import com.aos.floney.util.view.SampleToast
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment


@AndroidEntryPoint
class SignUpFirstFragment : BindingFragment<FragmentSignupFirstBinding>(R.layout.fragment_signup_first) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initsetting()
    }
    private fun initsetting(){
        binding.checkService.setOnClickListener { updateCheckAll() }
        binding.checkPrivacy.setOnClickListener { updateCheckAll() }
        binding.checkMarketing.setOnClickListener { updateCheckAll() }
        binding.checkAge.setOnClickListener { updateCheckAll() }
        binding.allCheck.setOnClickListener { onCheckAllClicked(it)}

        binding.imageView2.setOnClickListener {
            navigateTo<LoginActivity>()
        }
        binding.nextButton.setOnClickListener {
            if ( binding.checkService?.isChecked == true &&
                binding.checkPrivacy?.isChecked == true &&
                binding.checkAge?.isChecked == true)
                findNavController().navigate(R.id.action_fistFragment_to_secondFragment)
            else {
                SampleToast.createToast(requireContext(), "필수 약관에 모두 동의해주세요.")?.show()
            }
        }
    }
    private fun updateCheckAll() {
        // "전체 동의" 체크 상태 업데이트
        binding.allCheck?.isChecked = binding.checkService?.isChecked == true &&
                binding.checkPrivacy?.isChecked == true &&
                binding.checkMarketing?.isChecked == true &&
                binding.checkAge?.isChecked == true
    }
    fun onCheckAllClicked(view: View) {
        val checkAll = view as AppCompatCheckBox

        // Set the state of other checkboxes based on the state of "전체 동의"
        binding.checkService.isChecked = checkAll.isChecked
        binding.checkPrivacy.isChecked = checkAll.isChecked
        binding.checkMarketing.isChecked = checkAll.isChecked
        binding.checkAge.isChecked = checkAll.isChecked

    }
    private inline fun <reified T : Activity> navigateTo() {
        Intent(requireActivity(), T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }
}
