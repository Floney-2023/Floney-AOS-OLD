package com.aos.floney.presentation.home.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import com.aos.floney.R
import com.aos.floney.databinding.FragmentCalendarBinding
import com.aos.floney.domain.entity.CalendarItem
import com.aos.floney.presentation.home.HomeViewModel
import com.aos.floney.util.fragment.snackBar
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
    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: CalendarAdapter
    private var firstCallCalendar = true

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        firstCallCalendar = true

        initsetting()
        getCalendarInformationStateObserver()
    }
    fun initsetting(){
        //viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        /*adapter = CalendarAdapter(viewModel)

        binding.calendar.layoutManager = GridLayoutManager(context, 7)
        binding.calendar.adapter = adapter


        viewModel.calendarItems.observe(viewLifecycleOwner, { items ->
            Log.d("CalendarFragment", "Observer triggered: $items")
            adapter.notifyDataSetChanged()
        })
*/
    }
    private fun getCalendarInformationStateObserver() {
        viewModel.getCalendarInformationState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data.isEmpty() && !firstCallCalendar) {
                    } else {
                        //deactivateLoadingProgressBar()
                        updateCalendar(state.data)
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
    private fun updateCalendar(plantHistories: List<CalendarItem>) {
        viewLifeCycleScope.launch {
            viewModel.calendar.collect {

                /*val dayList = viewModel.updateCalendarDayList(
                    viewModel.calendar.value.get(Calendar.YEAR),
                    viewModel.calendar.value.get(Calendar.MONTH))
*/
                binding.calendar.layoutManager = GridLayoutManager(context, Calendar.DAY_OF_WEEK)

                adapter = CalendarAdapter(
                    currMonth = viewModel.calendar.value.get(
                        Calendar.MONTH),
                    plantHistories = plantHistories,
                    viewModel
                )

                binding.calendar.adapter = adapter

                adapter.submitList(plantHistories)

                firstCallCalendar = false
            }
        }
    }

}