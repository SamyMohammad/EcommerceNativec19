package com.example.ecommercenativec19.feature.signup

/** UI-only state of the Sign up screen: the typed field text and whether the password is revealed. */
data class SignUpUiState(
    val fullName: String = "",
    val mobileNumber: String = "",
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
)
