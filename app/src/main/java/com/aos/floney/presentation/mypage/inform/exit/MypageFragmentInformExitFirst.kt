package com.aos.floney.presentation.mypage.inform.exit

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.CheckBox
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentMypageInformExitFirstBinding
import com.aos.floney.databinding.FragmentMypageInformProfilechangeBinding
import com.aos.floney.databinding.FragmentMypageSettingBinding
import com.aos.floney.presentation.mypage.MypageViewModel
import com.aos.floney.presentation.mypage.settings.alarm.MypageFragmentSettingAlarm
import com.aos.floney.presentation.mypage.settings.language.MypageFragmentSettingLanguage
import com.aos.floney.util.view.SampleToast
import dagger.hilt.android.AndroidEntryPoint
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

@AndroidEntryPoint
class MypageFragmentInformExitFirst  : BindingFragment<FragmentMypageInformExitFirstBinding>(R.layout.fragment_mypage_inform_exit_first){
    private val viewModel: MypageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsetting()
    }

    private fun initsetting() {
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.nextButton.setOnClickListener {
            if (!isAnyCheckBoxChecked()) {
                SampleToast.createToast(requireContext(), "탈퇴 이유를 선택해주세요.")?.show()
            } else if (binding.directInput.isChecked && binding.reasonText.text.isNullOrBlank()) {
                SampleToast.createToast(requireContext(), "탈퇴 이유를 작성해주세요.")?.show()
            } else {
                navigateTo<MypageFragmentInformExitSecond>()
            }
        }

        binding.directInput.setOnCheckedChangeListener { _, isChecked ->
            // If direct_input checkbox is checked, make the reason_edit_text visible; otherwise, make it gone
            binding.reasonEditText.visibility = if (isChecked) View.VISIBLE else View.GONE
        }

        // Add logic to allow only one checkbox to be checked at a time
        binding.checkService.setOnClickListener {
            uncheckOtherCheckBoxes(binding.checkService)
        }

        binding.checkPrivacy.setOnClickListener {
            uncheckOtherCheckBoxes(binding.checkPrivacy)
        }

        binding.checkMarketing.setOnClickListener {
            uncheckOtherCheckBoxes(binding.checkMarketing)
        }

        binding.checkAge.setOnClickListener {
            uncheckOtherCheckBoxes(binding.checkAge)
        }

        binding.directInput.setOnClickListener {
            uncheckOtherCheckBoxes(binding.directInput)
        }
    }

    private fun uncheckOtherCheckBoxes(checkedCheckBox: View) {
        // Uncheck other checkboxes except the one that is currently checked
        val checkBoxes = listOf(
            binding.checkService,
            binding.checkPrivacy,
            binding.checkMarketing,
            binding.checkAge,
            binding.directInput
        )

        checkBoxes.forEach { checkBox ->
            if (checkBox != checkedCheckBox && checkBox is CheckBox) {
                checkBox.isChecked = false
            }
        }
    }
    private fun isAnyCheckBoxChecked(): Boolean {
        val checkBoxList = listOf(
            binding.checkService,
            binding.checkPrivacy,
            binding.checkMarketing,
            binding.checkAge,
            binding.directInput
        )

        return checkBoxList.any { it.isChecked }
    }


    private inline fun <reified T : Fragment> navigateTo() {
        parentFragmentManager.commit {
            replace<T>(R.id.mypageInformEmail, T::class.simpleName)
            addToBackStack(ROOT_FRAGMENT_HOME_SETTING)
        }
    }

    companion object {
        private const val ROOT_FRAGMENT_HOME_SETTING = "MypageFragmentInformExitFirst"
    }
}