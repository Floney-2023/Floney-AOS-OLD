package com.aos.floney.presentation.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aos.floney.R
import com.aos.floney.databinding.FragmentHomeBinding
import com.aos.floney.presentation.home.calendar.CalendarFragment
import com.aos.floney.presentation.home.daily.DailyFragment
import com.aos.floney.util.fragment.viewLifeCycle
import com.aos.floney.util.fragment.viewLifeCycleScope
import com.aos.floney.util.view.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.Locale

@AndroidEntryPoint
class HomeFragment  : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home){
    private val viewModel: HomeViewModel by viewModels(ownerProducer = {  requireActivity() })
    private var dateFormat = SimpleDateFormat("yyyy.MM", Locale.getDefault())

    override fun onStart() {
        super.onStart()
        viewModel.updateBookKeyItems() // 회원정보 변경 후(Activity->Fragment), 데이터 업데이트 하고자.
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        settingBookKey()
    }

    private fun settingBookKey(){
        viewModel.getUsersCheckState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data.bookKey?.isEmpty() == true) {
                        // 값이 없을 경우 로직 구현
                    } else {
                        Timber.e("BookKey : ${state.data}")
                        viewModel.updateBookKey(state.data.bookKey)
                        viewModel.updateCalendarItems()
                        viewModel.updateDailyItems(viewModel.calendar.value.time)
                        viewModel.updatebooksInfoItems()
                        settingCalendarText()
                        settingCalendarType()
                        settingCalendarDialog()
                        settingCalendarBookInfo()
                    }
                }

                is UiState.Failure -> Timber.e("Failure : ${state.msg}")
                is UiState.Empty -> Unit
                is UiState.Loading -> {
                    //activateLoadingProgressBar()
                }
            }
        }.launchIn(viewLifeCycleScope)
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
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.CREATED) {
                launch {
                    try {
                        viewModel.calendar.collect {
                            binding.calendarNowYearMonth.text = dateFormat.format(it.time)
                            Log.d("selectMonthYear", "Observer updateDisplay: ${it.time}")
                        }
                    } catch (e: Throwable) {
                        Log.d("selectMonthYear", "Observer updateDisplay: ${e}")
                    }
                }
            }
        }
    }
    private fun settingCalendarDialog() {
        binding.calendarNowYearMonth.setOnClickListener {
            // 캘린더 별 볼 때만 클릭 가능
            if (dateFormat.toPattern() == "yyyy.MM") {
                val yearMonthPickerFragment = YearMonthPickerFragment { year, month ->
                    viewModel.clickSelectYearMonth(year, month)
                    updateDisplayedDate()
                }
                yearMonthPickerFragment.show(parentFragmentManager, "YearMonthPicker")
            }
        }
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

    private fun settingCalendarBookInfo(){
        viewModel.getbooksInformationState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data.ourBookUsers?.isEmpty() == true) {
                        // 값이 없을 경우 로직 구현
                    } else {
                        /*if (state.data.bookImg != null)
                            binding.walletImage.setImageResource(state.data.bookImg)*/
                        binding.walletName.text = state.data.bookName
                    }
                }

                is UiState.Failure -> Timber.e("Failure : ${state.msg}")
                is UiState.Empty -> Unit
                is UiState.Loading -> {
                    //activateLoadingProgressBar()
                }
            }
        }.launchIn(viewLifeCycleScope)
    }
    private inline fun <reified T : Fragment> navigateTo() {
        childFragmentManager.commit {
            replace<T>(R.id.calendarTypeFragment, T::class.simpleName)
        }
    }
}