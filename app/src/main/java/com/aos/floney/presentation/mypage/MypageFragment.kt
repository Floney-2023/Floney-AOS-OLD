package com.aos.floney.presentation.mypage

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialog
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.aos.floney.R
import com.aos.floney.databinding.FragmentMypageBinding
import com.aos.floney.domain.entity.mypage.UserMypageData
import com.aos.floney.presentation.home.HomeViewModel
import com.aos.floney.presentation.mypage.inform.MypageActivityInformEmail
import com.aos.floney.presentation.mypage.inform.MypageActivityInformSimple
import com.aos.floney.presentation.mypage.settings.MypageFragmentSetting
import com.aos.floney.util.fragment.viewLifeCycle
import com.aos.floney.util.fragment.viewLifeCycleScope
import com.aos.floney.util.view.UiState
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kr.ac.konkuk.gdsc.plantory.util.binding.BindingFragment
import timber.log.Timber


@AndroidEntryPoint
class MypageFragment  : BindingFragment<FragmentMypageBinding>(R.layout.fragment_mypage){
    private val mypageViewModel by viewModels<MypageViewModel>(ownerProducer = {  requireActivity() })
    private val homeviewModel: HomeViewModel by viewModels(ownerProducer = {  requireActivity() })
    private lateinit var adapter : MypageAdapter
    private lateinit var progressDialog: AppCompatDialog

    override fun onStart() {
        super.onStart()
        mypageViewModel.updatemypageItems() // 회원정보 변경 후(Activity->Fragment), 데이터 업데이트 하고자.
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.viewModel = mypageViewModel

        initsetting()
        updateMyPageItem()
        bookKeyObserver()
    }
    private fun initsetting(){

        binding.alarm.setOnClickListener {

        }
        binding.settingButton.setOnClickListener {
            navigateTo<MypageFragmentSetting>()
        }
    }
    fun updateMyPageItem(){
        mypageViewModel.getusersMypageState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    if (state.data.myBooks?.isEmpty() == true) {
                        // 값이 없을 경우 로직 구현
                    } else {
                        updateMypageSetting(state)

                    }
                }

                is UiState.Failure -> Timber.e("Failure : ${state.msg}")
                is UiState.Empty -> Unit
                is UiState.Loading -> {
                    //activateLoadingProgressBar()
                }
            }
        }.launchIn(viewLifeCycleScope)
    }
    fun updateMypageSetting(state: UiState.Success<UserMypageData>) {

        if (state.data.profileImg != "user_default"){

            Glide.with(requireContext())
                .load(state.data.profileImg)
                .apply(
                    RequestOptions()
                    .placeholder(R.drawable.icon_profile_) // 이미지 로딩 중에 보여줄 placeholder 이미지 설정
                    .error(R.drawable.icon_profile_) // 이미지 로딩 실패 시 보여줄 이미지 설정
                    .diskCacheStrategy(DiskCacheStrategy.ALL)) // 디스크 캐시 설정
                .into(binding.profileImg) // ImageView에 이미지 설정
        }

        val loginType = state.data.provider
        binding.userInformView.setOnClickListener {
            when (loginType) {
                "EMAIL" -> navigateActivityTo<MypageActivityInformEmail>(state.data.nickname)
                "KAKAO" -> navigateActivityTo<MypageActivityInformSimple>(state.data.nickname)
            }
        }

        binding.nickName.text = state.data.nickname
        binding.email.text = state.data.email

        binding.walletView.removeAllViews()

        updateWalletView(state.data.myBooks)

        Timber.d("Success : update ${homeviewModel.bookKey.value}")
        // Iterate through each item in myBooks
        /*for (book in state.data.myBooks) {

            val walletDetailView = layoutInflater.inflate(R.layout.item_wallet_detail_view, null)

            walletDetailView.findViewById<TextView>(R.id.name).text = book.name
            walletDetailView.findViewById<TextView>(R.id.member_count).text = book.memberCount.toString() + "명"

            // Set layout parameters with margins
            val layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            layoutParams.setMargins(0, 0, 0, 20) // Adjust the margins as needed

            // 클릭 시 가계부 bookKey 변경
            walletDetailView.setOnClickListener {
                //homeviewModel.updateBookKey(book.bookKey)
                mypageViewModel.getusersBookKey(book.bookKey)
            }

            // Add wallet_detail_view to wallet_view with layout parameters
            binding.walletView.addView(walletDetailView, layoutParams)

        }*/
    }
    fun updateWalletView(booksList : List<UserMypageData.Book>){

        val nowBookKey = homeviewModel.bookKey.value

        val sortedBooksList = booksList.sortedByDescending { it.bookKey == nowBookKey }
        Timber.d("Success : observer ${sortedBooksList}")
        adapter = MypageAdapter(
            clickBookKey = nowBookKey,
            onBookClick = { bookKey ->
                progressON()
                mypageViewModel.getusersBookKey(bookKey)
                homeviewModel.updateBookKey(bookKey)
            }
        )

        binding.walletItemView.adapter = adapter

        adapter.submitList(sortedBooksList)

        binding.walletView.addView(binding.walletItemView)

        if (booksList.size == 1){
            val walletEmptyView = layoutInflater.inflate(R.layout.item_wallet_empty_view, null)
            binding.walletView.addView(walletEmptyView)
        }
    }
    fun bookKeyObserver(){
        mypageViewModel.getusersBookKeyState.flowWithLifecycle(viewLifeCycle).onEach { state ->
            when (state) {
                is UiState.Success -> {
                    Timber.d("Success : observer ${homeviewModel.bookKey.value}")
                    // 프로그레스바 Off
                    //progressOFF()
                    updateMyPageItem()
                }

                is UiState.Failure -> Timber.e("Failure : ${state.msg}")
                is UiState.Empty -> Unit
                is UiState.Loading -> {
                    //activateLoadingProgressBar()
                }
            }
        }.launchIn(viewLifeCycleScope)
    }
    fun progressON(){
        progressDialog = AppCompatDialog(requireContext()).apply {
            setContentView(R.layout.item_progress_loading) // 로딩 애니메이션을 위한 레이아웃 파일
            window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT)) // 배경 투명
            setCancelable(false) // 백 버튼으로 닫을 수 없도록 설정
        }

        val circle1 = progressDialog.findViewById<View>(R.id.circle1)
        val circle2 = progressDialog.findViewById<View>(R.id.circle2)
        val circle3 = progressDialog.findViewById<View>(R.id.circle3)

        val animationDelay = 200L // 애니메이션 시작 지연 시간 (0.2초)
        val animationDuration = 600L // 애니메이션 지속 시간 (0.6초)

        val animator1 = ObjectAnimator.ofFloat(circle1, "translationY", 0f, -30f, 0f).apply {
            duration = animationDuration
            interpolator = AccelerateDecelerateInterpolator()
            repeatMode = ValueAnimator.REVERSE
            repeatCount = 0 // 한 번만 실행
            startDelay = animationDelay * 0
        }

        val animator2 = ObjectAnimator.ofFloat(circle2, "translationY", 0f, -30f, 0f).apply {
            duration = animationDuration
            interpolator = AccelerateDecelerateInterpolator()
            repeatMode = ValueAnimator.REVERSE
            repeatCount = 0 // 한 번만 실행
            startDelay = animationDelay * 1
        }

        val animator3 = ObjectAnimator.ofFloat(circle3, "translationY", 0f, -30f, 0f).apply {
            duration = animationDuration
            interpolator = AccelerateDecelerateInterpolator()
            repeatMode = ValueAnimator.REVERSE
            repeatCount = 0 // 한 번만 실행
            startDelay = animationDelay * 2
        }

        // 마지막 애니메이션인 animator3에 대한 리스너 설정
        animator3.addListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                super.onAnimationEnd(animation)
                animator1.cancel()
                animator2.cancel()
                animator3.cancel()
                progressOFF()
            }
        })

        // 애니메이션들을 함께 실행
        animator1.start()
        animator2.start()
        animator3.start()

        progressDialog.show()
    }
    fun progressOFF(){
        if(progressDialog != null && progressDialog.isShowing()){
            progressDialog.dismiss()
        }
    }
    private inline fun <reified T : Fragment> navigateTo() {
        childFragmentManager.commit {
            replace<T>(R.id.mypageFragment, T::class.simpleName)
            addToBackStack(ROOT_FRAGMENT_HOME)
        }
    }
    private inline fun <reified T : Activity> navigateActivityTo(nickname:String) {
        val intent = Intent(getActivity(), T::class.java)
        intent.putExtra("nickname", nickname)
        startActivity(intent)
    }
    companion object {
        private const val ROOT_FRAGMENT_HOME = "MypageFragment"
    }
}