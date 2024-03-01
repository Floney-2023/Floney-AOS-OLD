package com.aos.floney.presentation.signup

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.aos.floney.presentation.onboard.FirstFragment
import com.aos.floney.presentation.onboard.SecondFragment
import com.aos.floney.presentation.onboard.ThirdFragment

class SignViewPaperAdapter(activity: FragmentActivity) :FragmentStateAdapter(activity) {

    val fragments : List<Fragment>

    init {
        fragments = listOf(SignUpFirstFragment(), SignUpSecondFragment(), SignUpThirdFragment(),
            SignUpFourthFragment(),SignUpFifthFragment())
    }
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}