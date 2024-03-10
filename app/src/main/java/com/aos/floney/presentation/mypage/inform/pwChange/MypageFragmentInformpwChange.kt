package com.aos.floney.presentation.mypage.inform.pwChange

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.View
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.aos.floney.R
import com.aos.floney.databinding.FragmentMypageInformProfilechangeBinding
import com.aos.floney.databinding.FragmentMypageInformPwchangeBinding
import com.aos.floney.databinding.FragmentMypageSettingBinding
import com.aos.floney.presentation.mypage.MypageViewModel
import com.aos.floney.presentation.mypage.settings.alarm.MypageFragmentSettingAlarm
import com.aos.floney.presentation.mypage.settings.language.MypageFragmentSettingLanguage
import com.aos.floney.util.view.SampleToast
import com.aos.floney.util.view.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import org.json.JSONObject
import timber.log.Timber

@AndroidEntryPoint
class MypageFragmentInformpwChange : BindingFragment<FragmentMypageInformPwchangeBinding>(R.layout.fragment_mypage_inform_pwchange) {
    private val viewModel: MypageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initsetting()
    }

    private fun initsetting() {
        putusersPasswordObserver()
        pwchangeButtonSetting()
        backButtonSetting()
    }

    private fun pwchangeButtonSetting() {
        binding.startButton.setOnClickListener {

            val nowPassword = binding.nowPasswordText.text.toString()
            val newPassword = binding.newPasswordText.text.toString()
            val newPasswordCheck = binding.newPasswordCheckText.text.toString()

            // 비밀번호 유효성 검사
            if (checkPasswordFields()&&isPasswordValid(nowPassword, newPassword, newPasswordCheck)) {
                viewModel.putusersPassword(newPassword, nowPassword)
            }
        }
    }

    private fun backButtonSetting() {
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun putusersPasswordObserver() {
        viewModel.putusersPasswordState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {

                is UiState.Loading -> Unit

                is UiState.Success -> {
                    Timber.d("Success : changePW ")
                    SampleToast.createToast(requireContext(), "변경이 완료되었습니다.")?.show()
                }

                is UiState.Failure -> {
                    handlePasswordError(state.msg)
                }

                is UiState.Empty -> Unit
            }
        }.launchIn(lifecycleScope)
    }

    private fun handlePasswordError(errorCode: String) {
        val errorMessage = when (errorCode) {
            "U008" -> "해당 이메일로 가입된 유저가 없습니다"
            "U017" -> "이전 비밀번호와 같습니다"
            "U022" -> "현재 비밀번호가 일치하지 않습니다"
            else -> "알 수 없는 오류가 발생했습니다"
        }

        SampleToast.createToast(requireContext(), errorMessage)?.show()
    }

    private fun checkPasswordFields(): Boolean {
        val nowPassword = binding.nowPasswordText.text.toString()
        val newPassword = binding.newPasswordText.text.toString()
        val newPasswordCheck = binding.newPasswordCheckText.text.toString()

        // 현재 비밀번호, 새 비밀번호, 새 비밀번호 확인에 대한 유효성 검사
        val isNowPasswordEmpty = nowPassword.isEmpty()
        val isNewPasswordEmpty = newPassword.isEmpty()
        val isNewPasswordCheckEmpty = newPasswordCheck.isEmpty()

        if (isNowPasswordEmpty) {
            SampleToast.createToast(requireContext(), "현재 비밀번호를 입력해주세요.")?.show()
            return false
        } else if (isNewPasswordEmpty) {
            SampleToast.createToast(requireContext(), "새 비밀번호를 입력해주세요.")?.show()
            return false
        } else if (isNewPasswordCheckEmpty) {
            SampleToast.createToast(requireContext(), "새 비밀번호 확인을 입력해주세요.")?.show()
            return false
        } else if (!isPasswordValid(nowPassword, newPassword, newPasswordCheck)) {
            SampleToast.createToast(requireContext(), "비밀번호 양식을 확인해주세요.")?.show()
            return false
        }
        return true
    }

    private fun isPasswordValid(nowPassword: String, newPassword: String, newPasswordCheck: String): Boolean {
        // 영문 대소문자, 숫자, 특수문자 포함 8자 이상
        val passwordRegex = Regex("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*[!@#$%^&*()-_+=]).{8,}$")

        if (!passwordRegex.matches(newPassword)) {
            SampleToast.createToast(requireContext(), "비밀번호 양식을 확인해주세요.")?.show()
            return false
        }

        // 새 비밀번호와 새 비밀번호 확인이 일치하는지 확인
        if (newPassword != newPasswordCheck) {
            SampleToast.createToast(requireContext(), "새 비밀번호가 일치하지 않습니다.")?.show()
            return false
        }

        return true
    }
}
