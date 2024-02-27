package com.aos.floney.presentation.onboard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPaperAdapter(activity: FragmentActivity) :FragmentStateAdapter(activity) {

    val fragments : List<Fragment>

    init {
        fragments = listOf(FirstFragment(),SecondFragment(),ThirdFragment())
    }
    override fun getItemCount(): Int {
        return fragments.size
    }

    override fun createFragment(position: Int): Fragment {
        return fragments[position]
    }

}