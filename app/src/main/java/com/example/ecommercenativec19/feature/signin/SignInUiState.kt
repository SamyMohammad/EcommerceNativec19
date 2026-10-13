package com.example.ecommercenativec19.feature.signin

import com.example.ecommercenativec19.feature.auth.domain.AuthError
import com.example.ecommercenativec19.feature.auth.domain.AuthField
import com.example.ecommercenativec19.feature.auth.domain.FieldError

/**
 * State of the Sign in screen. [signedIn] is state, not a one-shot event: the route reacts to it by leaving the
 * screen, which removes this ViewModel, so there is nothing to "consume" and no event can be lost on rotation.
 */
data class SignInUiState(
    val email: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val fieldErrors: Map<AuthField, FieldError> = emptyMap(),
    val isSubmitting: Boolean = false,
    val submitError: AuthError? = null,
    val signedIn: Boolean = false,
) {
    // The generated toString would print the typed password.
    override fun toString(): String =
        "SignInUiState(email=$email, passwordVisible=$passwordVisible, fieldErrors=$fieldErrors, " +
            "isSubmitting=$isSubmitting, submitError=$submitError, signedIn=$signedIn)"
}
