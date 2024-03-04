package com.aos.floney.presentation.mypage

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentMypageBinding
import com.aos.floney.presentation.mypage.settings.MypageFragmentSetting
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

class MypageFragment  : BindingFragment<FragmentMypageBinding>(R.layout.fragment_mypage){
    private val viewModel: MypageViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initsetting()

    }
    private fun initsetting(){
        binding.alarm.setOnClickListener {

        }
        binding.settingButton.setOnClickListener {
            navigateTo<MypageFragmentSetting>()
        }
    }
    private inline fun <reified T : Fragment> navigateTo() {
        childFragmentManager.commit {
            replace<T>(R.id.mypageFragment, T::class.simpleName)
            addToBackStack(ROOT_FRAGMENT_HOME)
        }
    }
    companion object {
        private const val ROOT_FRAGMENT_HOME = "MypageFragment"
    }
}