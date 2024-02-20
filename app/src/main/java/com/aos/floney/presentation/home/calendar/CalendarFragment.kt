package com.aos.floney.presentation.home.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.aos.floney.R
import com.aos.floney.databinding.FragmentCalendarBinding
import com.aos.floney.domain.entity.CalendarData
import com.aos.floney.domain.entity.CalendarItemType
import com.aos.floney.domain.entity.GetbooksMonthData
import com.aos.floney.presentation.home.HomeViewModel
import com.aos.floney.util.fragment.viewLifeCycle
import com.aos.floney.util.fragment.viewLifeCycleScope
import com.aos.floney.util.view.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import timber.log.Timber
import java.util.Calendar

@AndroidEntryPoint
class CalendarFragment  : BindingFragment<FragmentCalendarBinding>(R.layout.fragment_calendar){
    private val viewModel: HomeViewModel by viewModels(ownerProducer = {  requireActivity() })
    private lateinit var adapter: CalendarAdapter
    private var firstCallCalendar = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstCallCalendar = true

        getCalendarInformationStateObserver()
    }
    private fun getCalendarInformationStateObserver() {
        viewModel.getCalendarInformationState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data.calendarItems!!.isEmpty()&& !firstCallCalendar) {
                    } else {
                        // 이월 금액 설정
                        if (state.data.carryOverInfo.carryOverStatus == true)
                        {
                            if (state.data.carryOverInfo.carryOverMoney>0){
                                state.data.calendarItems[0].money+=state.data.carryOverInfo.carryOverMoney
                                state.data.totalIncome+=state.data.carryOverInfo.carryOverMoney
                            }
                            else {
                                state.data.calendarItems[1].money += state.data.carryOverInfo.carryOverMoney*(-1)
                                state.data.totalOutcome+=state.data.carryOverInfo.carryOverMoney*(-1)
                            }
                        }
                        //deactivateLoadingProgressBar()
                        updateCardView(state.data.totalIncome, state.data.totalOutcome)
                        updateCalendar(state.data.calendarItems)
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
    private fun updateCardView(totalIncome : Double, totalOutcome: Double){
        binding.totalOutcome.text = totalOutcome.toInt().toString()+"원"
        binding.totalIncome.text = totalIncome.toInt().toString()+"원"
    }
    private fun createEmptyItemList(size: Int): MutableList<CalendarData> {
        return MutableList(size) { CalendarData(GetbooksMonthData.CalendarItem("0",0.0,CalendarItemType.INCOME), GetbooksMonthData.CalendarItem("0",0.0,CalendarItemType.OUTCOME))}
    }
    private fun updateCalendar(
        calendarItems: List<GetbooksMonthData.CalendarItem>
    ) {
        viewLifeCycleScope.launch {
            viewModel.calendar.collect {
                // 빈칸으로 시작
                val daySize = viewModel.updateCalendarDayList(
                    viewModel.calendar.value.get(Calendar.YEAR),
                    viewModel.calendar.value.get(Calendar.MONTH))

                binding.calendar.layoutManager = GridLayoutManager(context, Calendar.DAY_OF_WEEK)

                // CalendarData 리스트 생성 (초기에 빈 객체로 초기화)
                var expenseItemList = createEmptyItemList(daySize)

                // CalendarItem 리스트를 2개씩 묶어 CalendarData에 추가
                calendarItems.chunked(2).forEach { chunk ->
                    expenseItemList.add(CalendarData(chunk.get(0), chunk.get(1)))

                }

                adapter = CalendarAdapter(
                    currMonth = viewModel.calendar.value.get(
                        Calendar.MONTH)
                ) { date ->
                    viewModel.clickSelectDate(date)
                    val bottomSheetPostFragment = BottomSheetFragment()
                    bottomSheetPostFragment.show(
                        childFragmentManager,
                        bottomSheetPostFragment.tag
                    )
                }

                binding.calendar.adapter = adapter

                adapter.submitList(expenseItemList)

                firstCallCalendar = false
            }
        }
    }

}