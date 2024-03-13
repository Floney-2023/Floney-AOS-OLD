package com.aos.floney.presentation.home.daily

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.aos.floney.R
import com.aos.floney.databinding.FragmentDailyBinding
import com.aos.floney.domain.entity.DailyItemType
import com.aos.floney.domain.entity.GetbooksDaysData
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

@AndroidEntryPoint
class DailyFragment  : BindingFragment<FragmentDailyBinding>(R.layout.fragment_daily){
    private val viewModel: HomeViewModel by viewModels(ownerProducer = { requireActivity() })
    private lateinit var adapter: DailyAdapter
    private var firstCallCalendar = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstCallCalendar = true
        viewModel.updateDailyItems(viewModel.calendar.value.time)

        getCalendarInformationStateObserver()
    }
    private fun getCalendarInformationStateObserver() {
        viewModel.getDailyInformationState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data.dayLinesResponse?.isEmpty() == true) {
                        binding.dailyEmptyCalendar.visibility = View.VISIBLE
                        binding.dailyCalendar.visibility = View.GONE
                    } else {
                        //deactivateLoadingProgressBar()
                        updateCalendar(state.data.dayLinesResponse!!,state.data.carryOverInfo)
                        updateTotalView(state.data.totalExpense!!, state.data.carryOverInfo)

                        binding.dailyEmptyCalendar.visibility = View.GONE
                        binding.dailyCalendar.visibility = View.VISIBLE
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
    private fun updateTotalView(
        total: List<GetbooksDaysData.TotalExpense>,
        carryOverInfo: GetbooksDaysData.CarryOverInfo
    ){
        var totalCarryIncome = 0.0
        var totalCarryOutcome = 0.0
        if (carryOverInfo.carryOverStatus == true)
        {
            if (carryOverInfo.carryOverMoney>0){
                totalCarryIncome+=carryOverInfo.carryOverMoney
            }
            else {
                totalCarryOutcome+=carryOverInfo.carryOverMoney
            }
        }
        for (item in total) {
            if (item.categoryType == DailyItemType.INCOME) {
                binding.totalIncome.text = ((item.money+totalCarryIncome).toInt()).toString()+"원"
            } else {
                binding.totalOutcome.text = ((item.money+totalCarryOutcome).toInt()).toString()+"원"
            }
        }

    }
    private fun updateCalendar(
        dailyItems: List<GetbooksDaysData.DailyItem>,
        carryOverInfo: GetbooksDaysData.CarryOverInfo
    ) {
        viewLifeCycleScope.launch {
            viewModel.calendar.collect {

                //val selectDay = viewModel.calendar.value.time

                adapter = DailyAdapter(viewModel)
                binding.dailyCalendar.layoutManager = LinearLayoutManager(context)



                adapter = DailyAdapter(
                    viewModel = viewModel
                )

                binding.dailyCalendar.adapter = adapter
                adapter.submitList(dailyItems)

                firstCallCalendar = false
            }
        }
    }
    fun checktype(money : Double){
        if (money>0)
            return
    }

}