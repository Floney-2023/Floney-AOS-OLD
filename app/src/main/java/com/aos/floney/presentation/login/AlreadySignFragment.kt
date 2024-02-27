package com.aos.floney.presentation.login

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.NumberPicker
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.aos.floney.databinding.FragmentAlreadySignBinding
import com.aos.floney.databinding.FragmentYearMonthPickerBinding
import com.aos.floney.presentation.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class AlreadySignFragment(
    private val onSelect: (Int, Int) -> Unit
)
 : DialogFragment() {

    private var _binding: FragmentAlreadySignBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels(ownerProducer = {  requireActivity() })
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentAlreadySignBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))


        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}