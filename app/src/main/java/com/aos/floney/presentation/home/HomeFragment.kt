package com.aos.floney.presentation.home

import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentHomeBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

class HomeFragment  : BindingFragment<FragmentHomeBinding>(R.layout.fragment_home){
    private val viewModel: HomeViewModel by viewModels()

}