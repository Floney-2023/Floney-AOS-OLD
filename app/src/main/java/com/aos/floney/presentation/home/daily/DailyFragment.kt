package com.aos.floney.presentation.home.daily

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aos.floney.R
import com.aos.floney.databinding.FragmentDailyBinding
import com.aos.floney.presentation.home.HomeViewModel
import com.aos.floney.presentation.home.calendar.CalendarViewModel
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

class DailyFragment  : BindingFragment<FragmentDailyBinding>(R.layout.fragment_daily){
    private val viewModel: HomeViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var adapter: DailyAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initsetting()
    }
    fun initsetting(){
        //viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        adapter = DailyAdapter(viewModel)
        binding.dailyCalendar.layoutManager = LinearLayoutManager(context)
        binding.dailyCalendar.adapter = adapter

        Log.d("DailyFragment", "Observer triggered: ")

        viewModel.dailyItems.observe(viewLifecycleOwner, { items ->
            Log.d("DailyFragment", "Observer triggered: $items")
            adapter.notifyDataSetChanged()
        })


    }

}