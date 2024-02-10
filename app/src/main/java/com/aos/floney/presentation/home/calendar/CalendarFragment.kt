package com.aos.floney.presentation.home.calendar

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.aos.floney.R
import com.aos.floney.databinding.FragmentCalendarBinding
import com.aos.floney.presentation.home.HomeViewModel
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

class CalendarFragment  : BindingFragment<FragmentCalendarBinding>(R.layout.fragment_calendar){
    private val viewModel: HomeViewModel by viewModels(ownerProducer = { requireParentFragment() })
    private lateinit var adapter: CalendarAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initsetting()
    }
    fun initsetting(){
        //viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        adapter = CalendarAdapter(viewModel)

        binding.calendar.layoutManager = GridLayoutManager(context, 7)
        binding.calendar.adapter = adapter

        viewModel.calendarItems.observe(viewLifecycleOwner, { items ->
            Log.d("CalendarFragment", "Observer triggered: $items")
            adapter.notifyDataSetChanged()
        })

    }

}