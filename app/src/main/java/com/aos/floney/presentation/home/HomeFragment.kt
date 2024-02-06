package com.aos.floney.presentation.home

import android.graphics.Color
import android.graphics.ColorSpace.Rgb
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // 달, 일자 변경 시 이벤트 setting
        settingCalendarText()
        // 캘린더, 일별 버튼 check 시 fragment 이동
        settingCalendarType()
        //return super.onCreateView(inflater, container, savedInstanceState)

    }
    fun settingCalendarText(){
        binding.iconArrowLeft.setOnClickListener{

        }
        binding.iconArrowRight.setOnClickListener{

        }
    }
    fun settingCalendarType(){
        // 디폴트 (캘린더 뷰 시작)
        navigateTo<CalendarFragment>()

        // 캘린더 뷰
        binding.calendarCheckButton.setOnClickListener{
            binding.calendarCheckButton.setBackgroundResource(R.drawable.calendar_type_area)
            binding.dailyCheckButton.setBackgroundColor(Color.TRANSPARENT)
            navigateTo<CalendarFragment>()
        }
        // 일별 뷰
        binding.dailyCheckButton.setOnClickListener{
            binding.dailyCheckButton.setBackgroundResource(R.drawable.calendar_type_area)
            binding.calendarCheckButton.setBackgroundColor(Color.TRANSPARENT)
            navigateTo<DailyFragment>()
        }
    }
    private inline fun <reified T : Fragment> navigateTo() {
        childFragmentManager.commit {
            replace<T>(R.id.calendarTypeFragment, T::class.simpleName)
        }
    }

}