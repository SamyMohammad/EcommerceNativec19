package com.example.ecommercenativec19.feature.auth.domain

/** What a sign-in / sign-up use case reports to the ViewModel. */
sealed interface AuthOutcome {
    data object Success : AuthOutcome

    /** Nothing was sent: these fields failed the client-side rules. */
    data class Invalid(val errors: Map<AuthField, FieldError>) : AuthOutcome

    data class Failed(val error: AuthError) : AuthOutcome
}
