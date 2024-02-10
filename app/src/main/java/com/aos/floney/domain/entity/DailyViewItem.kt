package com.aos.floney.domain.entity

data class DailyViewItem(
    val id: Int,
    val money: Int,
    val img: String?, // null default
    val category: String,
    val assetType: String,
    val content: String,
    val exceptStatus: Boolean,
    val userNickName: String
)