package com.aos.floney.presentation.mypage

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialog
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
        //binding.profileImg.setImageResource(state.data.profileImg)

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
                    progressOFF()
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
        progressDialog = AppCompatDialog(requireContext())
        progressDialog.setCancelable(false)
        progressDialog.getWindow()?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        progressDialog.setContentView(R.layout.item_progress_loading)
        progressDialog.show()
        var img_loading_framge = progressDialog.findViewById<ImageView>(R.id.iv_frame_loading)
        var frameAnimation = img_loading_framge?.getBackground() as AnimationDrawable
        img_loading_framge?.post(object : Runnable{
            override fun run() {
                frameAnimation.start()
            }

        })
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