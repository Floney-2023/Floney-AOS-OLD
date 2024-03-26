package com.aos.floney.presentation.login

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.aos.floney.BuildConfig.GOOGLE_OAUTH_KEY
import com.aos.floney.R
import com.aos.floney.databinding.ActivityLoginBinding
import com.aos.floney.presentation.HomeActivity
import com.aos.floney.presentation.MainViewModel
import com.aos.floney.presentation.home.HomeFragment
import com.aos.floney.presentation.home.YearMonthPickerFragment
import com.aos.floney.presentation.signup.SignUpActivity
import com.aos.floney.util.binding.BindingActivity
import com.aos.floney.util.view.ErrorToast
import com.aos.floney.util.view.SampleToast
import com.aos.floney.util.view.SocialLoginUiState
import com.aos.floney.util.view.UiState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.common.api.Scope
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import timber.log.Timber

@AndroidEntryPoint
class LoginActivity : BindingActivity<ActivityLoginBinding>(R.layout.activity_login) {

    private val viewModel by viewModels<LoginViewModel>()
    private val googleSignInClient: GoogleSignInClient by lazy { getGoogleClient() }
    private lateinit var googleAuthLauncher: ActivityResultLauncher<Intent>

    // 카카오계정으로 로그인 공통 callback 구성
    // 카카오톡으로 로그인 할 수 없어 카카오계정으로 로그인할 경우 사용됨
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            Log.e(TAG, "카카오계정으로 로그인 실패", error)
            if (error.toString().contains("statusCode=302")){
                loginWithKakaoAccount()
            }
            else{
                Log.e(TAG, "카카오계정으로 로그인 실패 : 302 아냐", error)
            }
            //viewModel.kakaoLoginFail()
        } else if (token != null) {
            Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken}")
            //viewModel.kakaoLoginSuccess()
        }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding.singUp.setOnClickListener {
            navigateActivityTo<SignUpActivity>()
        }
        settingGoogleSignInClient()
        settingLoginButton()
        settingKakaoLoginButton()
        settingGoogleLoginButton()
        initLoginObserver()
    }
    private fun settingGoogleSignInClient(){

        // ActivityResultLauncher 초기화
        googleAuthLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)

            try {
                val account = task.getResult(ApiException::class.java)

                // 이름, 이메일 등이 필요하다면 아래와 같이 account를 통해 각 메소드를 불러올 수 있다.
                val userName = account?.givenName
                val serverAuth = account?.serverAuthCode
                SampleToast.createToast(this, "성공...")?.show()

            } catch (e: ApiException) {
                SampleToast.createToast(this, "실패..")?.show()
                Log.e(TAG, "실패 : ${e.stackTraceToString()}")
                // 에러 처리
            }
        }
    }
    private fun settingLoginButton(){
        binding.startLogin.setOnClickListener{
            // 이미 가입된 계정
//            val alreadySignFragment = AlreadySignFragment { year, month ->
//
//            }
//            alreadySignFragment.show(supportFragmentManager, "YearMonthPicker")


            viewModel.postuserLogin(binding.idText.text.toString(),binding.pwText.text.toString())
        }

    }
    private fun settingKakaoLoginButton(){
        binding.kakaoBtn.setOnClickListener {


            /*if (UserApiClient.instance.isKakaoTalkLoginAvailable(this)) {
                UserApiClient.instance.loginWithKakaoTalk(this) { token, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오톡으로 로그인 실패", error)
                        // 사용자가 카카오톡 설치 후 디바이스 권한 요청 화면에서 로그인을 취소한 경우,
                        // 의도적인 로그인 취소로 보고 카카오계정으로 로그인 시도 없이 로그인 취소로 처리 (예: 뒤로 가기)
                        if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                            return@loginWithKakaoTalk
                        }
                        // 카카오톡에 연결된 카카오계정이 없는 경우, 카카오계정으로 로그인 시도
                        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
                    } else if (token != null) {
                        Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken}")
                        //viewModel.kakaoLoginSuccess()
                    }
                }
            } else {
                UserApiClient.instance.loginWithKakaoAccount(this, callback = callback)
            }*/
            viewModel.kakaoLogin(this)
        }
    }
    private fun settingGoogleLoginButton(){
        binding.googleBtn.setOnClickListener {
            requestGoogleLogin()
        }
    }
    private fun loginWithKakaoAccount(){
        UserApiClient.instance.loginWithKakaoAccount(this, callback = callback!!)
    }
    private fun requestGoogleLogin() {
        googleSignInClient.signOut()
        val signInIntent = googleSignInClient.signInIntent
        googleAuthLauncher.launch(signInIntent)
    }

    private fun getGoogleClient(): GoogleSignInClient {
        val googleSignInOption = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestScopes(Scope("https://www.googleapis.com/auth/pubsub"))
            .requestServerAuthCode(GOOGLE_OAUTH_KEY)
            .requestEmail() // 이메일도 요청할 수 있다.
            .build()

        return GoogleSignIn.getClient(this, googleSignInOption)
    }

    private fun moveSignUpActivity() {
        this.run {
            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
    }
    private fun initLoginObserver() {
        viewModel.loginState.flowWithLifecycle(lifecycle).onEach { state ->
            when (state) {
                is UiState.Loading -> {

                }

                is UiState.Success -> {

                    navigateActivityTo<HomeActivity>()

                }

                is UiState.Failure -> {
                    handlePasswordError(state.msg)
                }

                is UiState.Empty -> {
                }
            }
        }.launchIn(lifecycleScope)
    }
    private fun handlePasswordError(errorCode: String) {
        val errorMessage = when (errorCode) {
            "U008" -> "가입된 정보가 없습니다"
            "U009" -> "이메일 또는 비밀번호를 다시 확인하세요"
            "U022" -> "현재 비밀번호가 일치하지 않습니다"
            else -> "알 수 없는 오류가 발생했습니다"
        }

        ErrorToast.createToast(this, errorMessage)?.show()
    }
    private inline fun <reified T : Fragment> navigateTo() {
        supportFragmentManager.commit {
            replace<T>(R.id.nav_host_fragment, T::class.simpleName)
        }
    }
    private inline fun <reified T : Activity> navigateActivityTo() {
        Intent(this, T::class.java).apply {
            addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(this)
        }
    }
}