package com.example.ecommercenativec19.core.common

/** Failure kinds a [LoadState.Error] region can show. Nothing in the UI-only build produces them yet. */
sealed interface AppError {
    data object Network : AppError
    data class Server(val code: Int) : AppError
    /** The response arrived but could not be decoded. */
    data object Parsing : AppError
    data object Unknown : AppError
}
