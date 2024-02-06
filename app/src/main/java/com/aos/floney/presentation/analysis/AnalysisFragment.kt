package com.aos.floney.presentation.analysis

import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.databinding.FragmentAnalysisBinding
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment

class AnalysisFragment  : BindingFragment<FragmentAnalysisBinding>(R.layout.fragment_analysis){
    private val viewModel: AnalysisViewModel by viewModels()

}