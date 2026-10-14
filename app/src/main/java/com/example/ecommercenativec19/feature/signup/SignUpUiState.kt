package com.example.ecommercenativec19.feature.signup

import com.example.ecommercenativec19.feature.auth.AuthField
import com.example.ecommercenativec19.feature.auth.FieldError

data class SignUpUiState(
    val fullName: String = "",
    val mobileNumber: String = "",
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val fieldErrors: Map<AuthField, FieldError> = emptyMap(),
) {
    // The generated toString would print the typed password.
    override fun toString(): String =
        "SignUpUiState(fullName=$fullName, mobileNumber=$mobileNumber, email=$email, " +
            "passwordVisible=$passwordVisible, fieldErrors=$fieldErrors)"
}
