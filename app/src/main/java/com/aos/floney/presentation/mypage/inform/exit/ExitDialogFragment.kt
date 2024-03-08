package com.aos.floney.presentation.mypage.inform.exit

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
import com.aos.floney.databinding.FragmentMypageInformExitDialogBinding
import com.aos.floney.databinding.FragmentYearMonthPickerBinding
import dagger.hilt.android.AndroidEntryPoint
import java.util.Calendar

@AndroidEntryPoint
class ExitDialogFragment(
    private val onSelect: (Boolean) -> Unit
)
 : DialogFragment() {

    private var _binding: FragmentMypageInformExitDialogBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentMypageInformExitDialogBinding.inflate(inflater, container, false)
        val view = binding.root

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        binding.btnExit.setOnClickListener {
            // Handle exit button click
            onSelect(true)
            dismiss()
            // Add your exit logic here
        }

        binding.btnDelete.setOnClickListener {
            // Handle delete button click
            onSelect(false)
            dismiss()
            // Add your delete logic here
        }

        return view
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}