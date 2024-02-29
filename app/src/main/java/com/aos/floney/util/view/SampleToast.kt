package com.aos.floney.util.view

import android.content.Context
import android.content.res.Resources
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.aos.floney.R
import com.aos.floney.databinding.ItemToastSampleBinding

object SampleToast {

    fun createToast(context: Context, message: String): Toast? {
        val inflater = LayoutInflater.from(context)
        val binding: ItemToastSampleBinding =
            DataBindingUtil.inflate(inflater, R.layout.item_toast_sample, null, false)

        binding.tvSample.text = message

        return Toast(context).apply {
            setGravity(Gravity.BOTTOM or Gravity.FILL_HORIZONTAL, 0, 22.toPx())
            duration = Toast.LENGTH_LONG
            view = binding.root
        }
    }

    private fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()
}