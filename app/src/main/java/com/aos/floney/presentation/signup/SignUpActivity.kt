package com.aos.floney.presentation.signup

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aos.floney.R
import com.aos.floney.databinding.ActivityOnBoardBinding
import com.aos.floney.databinding.ActivitySignupBinding
import com.aos.floney.util.binding.BindingActivity

class SignUpActivity : BindingActivity<ActivitySignupBinding>(R.layout.activity_signup) {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewPaper = binding.viewpaper2
        viewPaper.adapter = SignViewPaperAdapter(this)
    }
}