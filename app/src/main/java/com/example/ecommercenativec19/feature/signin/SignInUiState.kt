package com.example.ecommercenativec19.feature.signin

import com.example.ecommercenativec19.feature.auth.AuthField
import com.example.ecommercenativec19.feature.auth.FieldError

data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val fieldErrors: Map<AuthField, FieldError> = emptyMap(),
) {
    // The generated toString would print the typed password.
    override fun toString(): String =
        "SignInUiState(email=$email, passwordVisible=$passwordVisible, fieldErrors=$fieldErrors)"
}
