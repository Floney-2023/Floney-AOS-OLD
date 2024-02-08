package com.aos.floney.presentation.home.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aos.floney.R
import com.aos.floney.databinding.FragmentCalendarBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import java.text.SimpleDateFormat
import java.util.Locale

class CalendarFragment  : BindingFragment<FragmentCalendarBinding>(R.layout.fragment_calendar){
    private val viewModel: CalendarViewModel by viewModels(ownerProducer = { requireParentFragment() })
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