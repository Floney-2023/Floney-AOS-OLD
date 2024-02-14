package com.aos.floney.presentation.home.calendar

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.widget.NumberPicker
import androidx.appcompat.widget.AppCompatButton
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.aos.floney.R
import com.aos.floney.presentation.home.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar
@AndroidEntryPoint
class YearMonthPickerFragment : DialogFragment() {

    private val viewModel: HomeViewModel by viewModels(ownerProducer = {  requireActivity() })
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val inflater = requireActivity().layoutInflater
        val dialogView = inflater.inflate(R.layout.fragment_year_month_picker, null)

        val yearPicker: NumberPicker = dialogView.findViewById(R.id.picker_year)
        val monthPicker: NumberPicker = dialogView.findViewById(R.id.picker_month)
        val btnClicker: AppCompatButton = dialogView.findViewById(R.id.btn_year_month_pick)


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
            viewModel.clickSelectYearMonth(selectedYear,selectedMonth)
            // 창 닫기
            dismiss()
        }

        builder.setView(dialogView)

        return builder.create()
    }
}