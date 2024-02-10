package com.aos.floney.presentation.home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentHomeBinding
import com.aos.floney.presentation.home.calendar.CalendarFragment
import com.aos.floney.presentation.home.calendar.CalendarViewModel
import com.aos.floney.presentation.home.daily.DailyFragment
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import java.text.SimpleDateFormat
import java.util.Locale

class HomeFragment  : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home){
    private val viewModel: HomeViewModel by viewModels()
    private var dateFormat = SimpleDateFormat("yyyy.MM", Locale.getDefault())

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingCalendarText()
        settingCalendarType()
    }

    private fun settingCalendarText() {
        updateDisplayedDate()

        binding.iconArrowLeft.setOnClickListener {
            when (dateFormat.toPattern()) {
                "yyyy.MM" -> viewModel.moveToPreviousMonth()
                "M.d" -> viewModel.moveToPreviousDay()
            }
            updateDisplayedDate()
        }

        binding.iconArrowRight.setOnClickListener {
            when (dateFormat.toPattern()) {
                "yyyy.MM" -> viewModel.moveToNextMonth()
                    "M.d" -> viewModel.moveToNextDay()
            }
            updateDisplayedDate()
        }
    }

    private fun updateDisplayedDate() {
        binding.calendarNowYearMonth.text = dateFormat.format(viewModel.calendar.time)
    }

    private fun settingCalendarType() {
        navigateTo<CalendarFragment>()

        binding.calendarCheckButton.setOnClickListener {
            switchCalendarType("yyyy.MM", R.drawable.calendar_type_area,Color.TRANSPARENT)
        }

        binding.dailyCheckButton.setOnClickListener {
            switchCalendarType("M.d", Color.TRANSPARENT, R.drawable.calendar_type_area)
        }
    }

    private fun switchCalendarType(dateFormatPattern: String, calendarBackgroundResource: Int, dailyBackgroundResource: Int) {
        dateFormat = SimpleDateFormat(dateFormatPattern, Locale.getDefault())
        updateDisplayedDate()

        binding.calendarCheckButton.setBackgroundResource(calendarBackgroundResource)
        binding.dailyCheckButton.setBackgroundResource(dailyBackgroundResource)

        when (dateFormatPattern) {
            "yyyy.MM" -> navigateTo<CalendarFragment>()
            "M.d" -> navigateTo<DailyFragment>()
        }
    }

    private inline fun <reified T : Fragment> navigateTo() {
        childFragmentManager.commit {
            replace<T>(R.id.calendarTypeFragment, T::class.simpleName)
        }
    }
}