package com.example.ecommercenativec19.feature.signup

import com.example.ecommercenativec19.feature.auth.domain.AuthError
import com.example.ecommercenativec19.feature.auth.domain.AuthField
import com.example.ecommercenativec19.feature.auth.domain.FieldError

/** State of the Sign up screen. See [com.example.ecommercenativec19.feature.signin.SignInUiState] for why [signedUp] is state. */
data class SignUpUiState(
    val fullName: String = "",
    val mobileNumber: String = "",
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val fieldErrors: Map<AuthField, FieldError> = emptyMap(),
    val isSubmitting: Boolean = false,
    val submitError: AuthError? = null,
    val signedUp: Boolean = false,
) {
    // The generated toString would print the typed password.
    override fun toString(): String =
        "SignUpUiState(fullName=$fullName, mobileNumber=$mobileNumber, email=$email, " +
            "passwordVisible=$passwordVisible, fieldErrors=$fieldErrors, isSubmitting=$isSubmitting, " +
            "submitError=$submitError, signedUp=$signedUp)"
}
