package com.example.ecommercenativec19.feature.auth.domain

import com.example.ecommercenativec19.core.common.AppError

/**
 * Failures of sign in / sign up. Only outcomes the UI words differently get their own case; every transport or
 * unexpected failure stays an [AppError] inside [Other]. A separate type (not new [AppError] cases) because
 * `AppError` is shared by every feature and these three only mean something for credentials.
 */
sealed interface AuthError {
    /** Wrong email or password. The server deliberately does not say which. */
    data object InvalidCredentials : AuthError

    data object EmailAlreadyUsed : AuthError

    /** The server rejected a field the client-side check let through; [message] is the server's own text. */
    data class Validation(val message: String) : AuthError

    data class Other(val error: AppError) : AuthError
}
