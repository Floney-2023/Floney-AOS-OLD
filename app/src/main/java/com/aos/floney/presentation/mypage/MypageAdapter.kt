package com.aos.floney.presentation.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.aos.floney.R
import com.aos.floney.databinding.ItemCustomDailyBinding
import com.aos.floney.databinding.ItemWalletDetailViewBinding
import com.aos.floney.domain.entity.DailyItemType
import com.aos.floney.domain.entity.GetbooksDaysData
import com.aos.floney.domain.entity.mypage.UserMypageData
import com.aos.floney.presentation.home.HomeViewModel
import com.aos.floney.util.view.ItemDiffCallback
import timber.log.Timber
import java.util.Date

class MypageAdapter(
    private val onBookClick: (String) -> Unit,
    private val clickBookKey : String
) : ListAdapter<UserMypageData.Book, MypageAdapter.ViewHolder>(
        ItemDiffCallback<UserMypageData.Book>(
            onItemsTheSame = { old, new -> old == new },
            onContentsTheSame = { old, new -> old == new }
        )
    ){
    class ViewHolder(
        private val binding : ItemWalletDetailViewBinding,
        private val onBookClick: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        val bookImg: ImageView = binding.bookImg
        val bookName: TextView = binding.name
        val bookMemberCount: TextView = binding.memberCount
        val boolActivated: ImageView = binding.boolActivated
        fun onBind(booksItem: UserMypageData.Book, bookKey : String) {
            //bookImg.setImageResource(booksItem.bookImg)


            bookName.text = booksItem.name
            bookMemberCount.text = booksItem.memberCount.toString() +"명"

            if (bookKey == booksItem.bookKey)
                boolActivated.setImageResource(R.drawable.icon_check_circle_activated)


            binding.root.setOnClickListener {
                onBookClick(booksItem.bookKey)
            }

        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemWalletDetailViewBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding, onBookClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val bookItems = getItem(position)
        holder.onBind(bookItems, clickBookKey)
    }
}
