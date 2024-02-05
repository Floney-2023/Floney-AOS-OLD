package com.aos.floney.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentHomeBinding
import com.aos.floney.presentation.home.calendar.CalendarFragment
import com.aos.floney.presentation.home.daily.DailyFragment
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

class HomeFragment  : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home){
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)

        // 캘린더, 일별 버튼 check 시 fragment 이동
        settingCalendarType()

    }
    fun settingCalendarType(){
        binding.calendarCheckButton.setOnClickListener{
            navigateTo<CalendarFragment>()
        }
        binding.calendarCheckButton.setOnClickListener{
            navigateTo<DailyFragment>()
        }
    }
    private inline fun <reified T : Fragment> navigateTo() {
        childFragmentManager.commit {
            replace<T>(R.id.calendarTypeFragment, T::class.simpleName)
        }
    }

}