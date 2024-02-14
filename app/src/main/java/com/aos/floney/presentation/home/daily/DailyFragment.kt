package com.aos.floney.presentation.home.daily

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import com.aos.floney.R
import com.aos.floney.databinding.FragmentDailyBinding
import com.aos.floney.presentation.home.HomeViewModel
import com.aos.floney.presentation.home.calendar.CalendarViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
@AndroidEntryPoint
class DailyFragment  : BindingFragment<FragmentDailyBinding>(R.layout.fragment_daily){
    private val viewModel: HomeViewModel by viewModels(ownerProducer = { requireActivity() })
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
        }


    }

}