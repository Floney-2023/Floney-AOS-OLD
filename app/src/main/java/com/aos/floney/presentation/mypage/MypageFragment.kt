package com.aos.floney.presentation.mypage

import android.os.Bundle
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import androidx.lifecycle.flowWithLifecycle
import com.aos.floney.R
import com.aos.floney.databinding.FragmentMypageBinding
import com.aos.floney.domain.entity.UserMypageData
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
    private val viewModel: MypageViewModel by viewModels(ownerProducer = {  requireActivity() })

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initsetting()
        updateMyPageItem()

    }
    private fun initsetting(){

        binding.alarm.setOnClickListener {

        }
        binding.settingButton.setOnClickListener {
            navigateTo<MypageFragmentSetting>()
        }
    }
    fun updateMyPageItem(){
        viewModel.getusersMypageState.flowWithLifecycle(viewLifeCycle).onEach { state ->
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
        binding.nickName.text = state.data.nickname
        binding.email.text = state.data.email

        /*// 첫 번재 가계부 view
        //binding.bookImg.setImageResource(state.data.myBooks[0].bookImg)
        binding.name.text=state.data.myBooks[0].name
        binding.memberCount.text=state.data.myBooks[0].memberCount.toString()

        //state.data.myBooks 의 개수에 맞춰서 wallet_detail_view 생성해서 wallet_view에 추가해 나가기 부탁..*/
        binding.walletView.removeAllViews()

        // Iterate through each item in myBooks
        for (book in state.data.myBooks) {
            // Inflate the wallet_detail_view layout
            val walletDetailView = layoutInflater.inflate(R.layout.item_wallet_detail_view, null)

            // Set data to wallet_detail_view
            //walletDetailView.findViewById<ImageView>(R.id.book_img).setImageResource(book.bookImg)
            walletDetailView.findViewById<TextView>(R.id.name).text = book.name
            walletDetailView.findViewById<TextView>(R.id.member_count).text = book.memberCount.toString()

            // Add wallet_detail_view to wallet_view
            binding.walletView.addView(walletDetailView)
        }

        if (state.data.myBooks.size == 1){
            val walletEmptyView = layoutInflater.inflate(R.layout.item_wallet_empty_view, null)
            binding.walletView.addView(walletEmptyView)
        }

    }
    private inline fun <reified T : Fragment> navigateTo() {
        childFragmentManager.commit {
            replace<T>(R.id.mypageFragment, T::class.simpleName)
            addToBackStack(ROOT_FRAGMENT_HOME)
        }
    }
    companion object {
        private const val ROOT_FRAGMENT_HOME = "MypageFragment"
    }
}