package com.aos.floney.presentation.home.calendar

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.aos.floney.R
import com.aos.floney.databinding.FragmentDailyDialogBinding
import com.aos.floney.presentation.home.HomeViewModel
import com.aos.floney.presentation.home.daily.DailyAdapter
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.launch
import com.aos.floney.util.binding.BindingActivity
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
@AndroidEntryPoint
class BottomSheetFragment : BottomSheetDialogFragment() {
    private val viewModel: HomeViewModel by viewModels(ownerProducer = {  requireActivity() })
    private lateinit var binding: FragmentDailyDialogBinding
    private lateinit var adapter: DailyAdapter
    private var yearFormat = SimpleDateFormat("yyyy년", Locale.getDefault())
    private var dayFormat = SimpleDateFormat("M월 d일", Locale.getDefault())

    override fun onStart() {
        super.onStart()
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentDailyDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initsetting()
        observeCalendarAndItems()
    }

    override fun onResume() {
        super.onResume()
        // Observer 등록


    }

    fun initsetting(){
        //viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        adapter = DailyAdapter(viewModel)
        binding.dailyCalendar.layoutManager = LinearLayoutManager(context)
        binding.dailyCalendar.adapter = adapter

        Log.d("selectDay", "Observer triggered: " +viewModel.calendar.value!!.time)
        settingDate()

        // ViewModel의 초기값으로 초기화
        val initialDailyItems = viewModel.dailyItems.value
        if (initialDailyItems != null && initialDailyItems.isNotEmpty()) {
            binding.dailyEmptyCalendar.visibility = View.GONE
            binding.dailyCalendar.visibility = View.VISIBLE
            settingDate()
        } else {
            binding.dailyEmptyCalendar.visibility = View.VISIBLE
            binding.dailyCalendar.visibility = View.GONE
        }


        // bottomsheet 높이 설정
        //NavigationUI.setupWithNavController(binding.bottomNavigationView,navController)
        //val bottomSheetContainer: FrameLayout = binding.dailyDialogView

        //val bottomSheetBehavior = BottomSheetBehavior.from<View>(bottomSheetContainer)
        //bottomSheetBehavior.peekHeight = 76
        //bottomSheetBehavior.isGestureInsetBottomIgnored = true
    }

    private fun observeCalendarAndItems() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                launch {
                    try {
                        viewModel.calendar.collect {
                            Log.d("selectDay", "Observer scope: ${it.time}")
                            // Update UI here
                            binding.dailyDialogYear.text = yearFormat.format(it.time)
                            binding.dailyDialogDay.text = dayFormat.format(it.time)
                        }
                    } catch (e: Throwable) {
                        println("Exception from the flow: $e")
                    }
                }
                launch {
                    viewModel.dailyItems.collect {
                        try {
                            if (it.isEmpty()) {
                                binding.dailyEmptyCalendar.visibility = View.VISIBLE
                                binding.dailyCalendar.visibility = View.GONE
                            } else {
                                binding.dailyEmptyCalendar.visibility = View.GONE
                                binding.dailyCalendar.visibility = View.VISIBLE
                            }
                            adapter.notifyDataSetChanged()
                        } catch (e: Throwable) {
                            println("Exception from the flow: $e")
                        }
                    }
                }
            }
        }
    }
    fun settingDate()
    {


    }

}