package com.aos.floney.presentation.calculate

import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentCalculateBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

class CalculateFragment  : BindingFragment<FragmentCalculateBinding>(R.layout.fragment_calculate){
    private val viewModel: CalculateViewModel by viewModels()

}