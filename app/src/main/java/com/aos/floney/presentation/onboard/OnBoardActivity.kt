package com.aos.floney.presentation.onboard

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aos.floney.R
import com.aos.floney.databinding.ActivityOnBoardBinding
import com.aos.floney.util.binding.BindingActivity

class OnBoardActivity : BindingActivity<ActivityOnBoardBinding>(R.layout.activity_on_board) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewPaper = binding.viewpaper2
        viewPaper.adapter = ViewPaperAdapter(this)
        binding.wormDotsIndicator.attachTo(viewPaper)

    }
}