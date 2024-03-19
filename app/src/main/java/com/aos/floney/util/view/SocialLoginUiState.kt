package com.aos.floney.util.view

sealed interface SocialLoginUiState {
    object LoginSuccess : SocialLoginUiState
    object LoginFail : SocialLoginUiState
    object IDle : SocialLoginUiState
    object KakaoLogin : SocialLoginUiState
}