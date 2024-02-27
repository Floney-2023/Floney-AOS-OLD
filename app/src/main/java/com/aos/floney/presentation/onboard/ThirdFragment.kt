package com.aos.floney.presentation.onboard

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.navigation.fragment.findNavController
import com.aos.floney.R
import com.aos.floney.databinding.ActivityOnBoardBinding
import com.aos.floney.databinding.FragmentHomeBinding
import com.aos.floney.databinding.FragmentOnboardThirdBinding
import com.aos.floney.presentation.HomeActivity
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

class ThirdFragment : BindingFragment<FragmentOnboardThirdBinding>(R.layout.fragment_onboard_third) {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.startButton.setOnClickListener {
            navigateToMain()
            onBoardingFinished()
        }
    }
    private fun onBoardingFinished() {
        val prefs = requireActivity().getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        prefs.edit().putBoolean("finished", true).apply()
    }
    private fun navigateToMain() {
        navigateTo<HomeActivity>()
    }

    private fun navigateToOnboard() {
        navigateTo<OnBoardActivity>()
    }

    private inline fun <reified T : Activity> navigateTo() {
        Intent(requireActivity(), T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }

}