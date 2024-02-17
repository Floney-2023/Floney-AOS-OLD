package com.aos.floney.presentation.home.daily

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.aos.floney.R
import com.aos.floney.databinding.FragmentDailyBinding
import com.aos.floney.domain.entity.CalendarItem
import com.aos.floney.domain.entity.CalendarItemType
import com.aos.floney.domain.entity.DailyItem
import com.aos.floney.domain.entity.TotalExpense
import com.aos.floney.presentation.home.HomeViewModel
import com.aos.floney.presentation.home.calendar.CalendarAdapter
import com.aos.floney.presentation.home.calendar.CalendarViewModel
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
class DailyFragment  : BindingFragment<FragmentDailyBinding>(R.layout.fragment_daily){
    private val viewModel: HomeViewModel by viewModels(ownerProducer = { requireActivity() })
    private lateinit var adapter: DailyAdapter
    private var firstCallCalendar = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstCallCalendar = true

        initsetting()
        getCalendarInformationStateObserver()
    }
    fun initsetting(){
        //viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        /*adapter = DailyAdapter(viewModel)
        binding.dailyCalendar.layoutManager = LinearLayoutManager(context)
        binding.dailyCalendar.adapter = adapter

        Log.d("DailyFragment", "Observer triggered: ")

        lifecycleScope.launch{
            lifecycle.repeatOnLifecycle(Lifecycle.State.RESUMED){
                launch {
                    viewModel.dailyItems.collect{
                        try{
                            if (it.isEmpty()) {
                                binding.dailyEmptyCalendar.visibility = View.VISIBLE
                                binding.dailyCalendar.visibility = View.GONE
                            }
                            else{
                                binding.dailyEmptyCalendar.visibility = View.GONE
                                binding.dailyCalendar.visibility = View.VISIBLE
                            }
                            adapter.notifyDataSetChanged()
                        }
                        catch (e:Throwable){
                            println("Exception from the flow: $e")
                        }

                    }
                }
            }
        }*/
    }
    private fun getCalendarInformationStateObserver() {
        viewModel.getDailyInformationState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data.first?.isEmpty() == true) {
                        binding.dailyEmptyCalendar.visibility = View.VISIBLE
                        binding.dailyCalendar.visibility = View.GONE
                    } else {
                        //deactivateLoadingProgressBar()
                        updateCalendar(state.data.first!!)
                        updateTotalView(state.data.second?.get(0)!!)

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
    private fun updateTotalView(total: TotalExpense){
        if (total.assetType == CalendarItemType.INCOME)
            binding.totalIncome.text = total.money.toString()
        else
            binding.totalOutcome.text = total.money.toString()

    }
    private fun updateCalendar(dailyItems: List<DailyItem>) {
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

}