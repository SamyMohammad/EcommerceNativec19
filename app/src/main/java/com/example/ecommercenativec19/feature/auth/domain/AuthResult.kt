package com.example.ecommercenativec19.feature.auth.domain

/** Result of a repository call. On [Success] the session token has already been stored. */
sealed interface AuthResult {
    data object Success : AuthResult
    data class Failure(val error: AuthError) : AuthResult
}
