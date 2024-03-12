package com.aos.floney.presentation

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.aos.floney.BuildConfig
import com.aos.floney.R
import com.aos.floney.domain.repository.DataStoreRepository
import com.aos.floney.presentation.login.LoginActivity
import com.aos.floney.presentation.onboard.OnBoardActivity
import com.aos.floney.util.view.UiState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var dataStoreRepository: DataStoreRepository
    private val viewModel by viewModels<MainViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({

            if (isOnBoardingFinished()) {
                getDeviceToken()
                navigateToLogin()
                //navigateToMain()
            } else {
                navigateToOnboard()
            }
            finish()

        }, 1000) // 시간 1초 이후 실행


    }
    private fun getDeviceToken(){
        lifecycleScope.launch {
            delay(DELAY_TIME)

            if (BuildConfig.DEBUG) {
                checkAutoLogin()
            } else {
                //checkAppUpdateInfo()
            }
        }
        /*lifecycleScope.launch {
            val currentToken = ""
            currentToken?.let {
                checkAndUpdateDeviceToken(it)
            }
        }*/
    }
    private fun checkAutoLogin() {
        lifecycleScope.launch {
            val accessToken = dataStoreRepository.getAccessToken()?.firstOrNull()
            if (accessToken.isNullOrBlank()) {
                navigateToLogin()
            } else {
                navigateToMain()
            }
        }
    }
    private fun checkAndUpdateDeviceToken(currentToken: String) {
        lifecycleScope.launch {
            val storedToken = viewModel.getDeviceToken()
            Timber.d("Stored Token: $storedToken")
            if ((storedToken != currentToken) || storedToken.isEmpty()) {
                viewModel.postRegisterUser(currentToken)
            } else {
                navigateToMain()
            }
        }
    }
    private fun setPostRegisterUserStateObserver() {
        viewModel.postRegisterUserState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Loading -> Unit

                is UiState.Success -> {
                    Timber.d("Success : Register ")
                    navigateToMain()
                }

                is UiState.Failure -> {
                    Timber.d("Failure : ${state.msg}")
                }

                is UiState.Empty -> Unit
            }
        }.launchIn(lifecycleScope)
    }
    private fun navigateToLogin() {
        navigateTo<LoginActivity>()
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
    companion object {
        private const val DELAY_TIME = 1500L
    }

}