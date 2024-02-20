package com.aos.floney.application

import com.aos.floney.BuildConfig
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber
import android.app.Application

@HiltAndroidApp
class Application : Application() {

    init {
        setupTimber()
    }

    private fun setupTimber() {
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }
}