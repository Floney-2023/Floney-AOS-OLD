package com.aos.floney.presentation.home.calendar

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.aos.floney.R
import com.aos.floney.databinding.FragmentDailyDialogBinding
import com.aos.floney.presentation.home.HomeViewModel
import com.aos.floney.presentation.home.daily.DailyAdapter
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingActivity

class BottomSheetFragment : BottomSheetDialogFragment() {
    private val viewModel: HomeViewModel by viewModels(ownerProducer = { requireActivity() })
    private lateinit var binding: FragmentDailyDialogBinding
    private lateinit var adapter: DailyAdapter
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
    }
    fun initsetting(){
        //viewModel = ViewModelProvider(this).get(CalendarViewModel::class.java)
        adapter = DailyAdapter(viewModel)
        binding.dailyCalendar.layoutManager = LinearLayoutManager(context)
        binding.dailyCalendar.adapter = adapter

        Log.d("BottomSheetDialog", "Observer triggered: ")

        viewModel.dailyItems.observe(viewLifecycleOwner, { items ->
            Log.d("DailyFragment", "Observer triggered: $items")
            if (items.isEmpty()) {
                binding.dailyEmptyCalendar.visibility = View.VISIBLE
                binding.dailyCalendar.visibility = View.GONE
            }
            else{
                binding.dailyEmptyCalendar.visibility = View.GONE
                binding.dailyCalendar.visibility = View.VISIBLE
            }
            adapter.notifyDataSetChanged()
        })


    }

}