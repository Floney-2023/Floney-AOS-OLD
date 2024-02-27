package com.aos.floney.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.aos.floney.R
import com.aos.floney.presentation.onboard.OnBoardActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        // 일정 시간 지연 이후 실행하기 위한 코드
        Handler(Looper.getMainLooper()).postDelayed({

            if (isOnBoardingFinished()) {
                navigateToMain()
            } else {
                navigateToOnboard()
            }
            finish()

        }, 1000) // 시간 1초 이후 실행


    }
    private fun navigateToMain() {
        navigateTo<HomeActivity>()
    }
    private fun navigateToOnboard() {
        navigateTo<OnBoardActivity>()
    }

    private inline fun <reified T : Activity> navigateTo() {
        Intent(this, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }
    //2
    private fun isOnBoardingFinished(): Boolean {
        val prefs = this.getSharedPreferences("onBoarding", Context.MODE_PRIVATE)
        return prefs.getBoolean("finished", false)
    }

}