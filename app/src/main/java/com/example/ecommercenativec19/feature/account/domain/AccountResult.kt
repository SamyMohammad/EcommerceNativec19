package com.example.ecommercenativec19.feature.account.domain

import com.example.ecommercenativec19.feature.auth.domain.AuthError
import com.example.ecommercenativec19.feature.auth.domain.FieldError

/**
 * Result of a repository write. Failures reuse [AuthError]: the server's 400 text and the transport errors are the
 * same cases the sign-in screens already word, so the UI keeps one message mapping.
 */
sealed interface AccountResult<out T> {
    data class Success<T>(val data: T) : AccountResult<T>
    data class Failure(val error: AuthError) : AccountResult<Nothing>
}

/** What an Account use case reports to the ViewModel. */
sealed interface AccountOutcome<out T> {
    data class Success<T>(val data: T) : AccountOutcome<T>

    /** Nothing was sent: these fields failed the client-side rules. */
    data class Invalid(val errors: Map<AccountField, FieldError>) : AccountOutcome<Nothing>

    data class Failed(val error: AuthError) : AccountOutcome<Nothing>
}
