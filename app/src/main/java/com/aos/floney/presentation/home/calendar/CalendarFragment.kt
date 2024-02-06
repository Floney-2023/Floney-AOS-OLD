package com.aos.floney.presentation.home.calendar

import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentCalendarBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

class CalendarFragment  : BindingFragment<FragmentCalendarBinding>(R.layout.fragment_calendar){
    private val viewModel: CalendarViewModel by viewModels()

}