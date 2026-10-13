package com.example.ecommercenativec19.feature.signin

/** UI-only state of the Sign in screen: what the user typed and whether the password is revealed. */
data class SignInUiState(
    val userName: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
)
