package com.aos.floney.presentation.home

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
import com.aos.floney.databinding.FragmentYearMonthPickerBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class YearMonthPickerFragment(
    private val onSelect: (Int, Int) -> Unit
)
 : DialogFragment() {

    private var _binding: FragmentYearMonthPickerBinding? = null
    private val binding get() = _binding!!

    private val viewModel: HomeViewModel by viewModels(ownerProducer = {  requireActivity() })
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentYearMonthPickerBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val yearPicker: NumberPicker = binding.pickerYear
        val monthPicker: NumberPicker = binding.pickerMonth
        val btnClicker: AppCompatButton = binding.btnYearMonthPick


        // 년도 설정
        val currentYear = Calendar.getInstance().get(Calendar.YEAR)
        yearPicker.minValue = 2000
        yearPicker.maxValue = 2100
        yearPicker.value = currentYear // 현재 년도로 초기값 설정
        yearPicker.wrapSelectorWheel = false

        // 월 설정
        val currentMonth = Calendar.getInstance().get(Calendar.MONTH)
        monthPicker.minValue = 1
        monthPicker.maxValue = 12
        monthPicker.value = currentMonth + 1// 현재 달 월로 초기값 설정
        monthPicker.wrapSelectorWheel = false

        btnClicker.setOnClickListener{
            val selectedYear = yearPicker.value
            val selectedMonth = monthPicker.value - 1
            Log.d("selectMonthYear", "Observer yearmonthpicker: ${selectedYear} ${selectedMonth}")
            // viewmodel 선택한 날짜 바꾸기

            onSelect(selectedYear, selectedMonth)

            // 창 닫기
            dismiss()
        }

        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}