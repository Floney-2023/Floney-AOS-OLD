package com.aos.floney.presentation.home.daily

import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentDailyBinding
import com.aos.floney.presentation.home.calendar.CalendarViewModel
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

class DailyFragment  : BindingFragment<FragmentDailyBinding>(R.layout.fragment_daily){
    private val viewModel: CalendarViewModel by viewModels()

}