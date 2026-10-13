package com.example.ecommercenativec19.core.common

sealed interface AppResult<out T> {
    data class Success<T>(val data: T) : AppResult<T>
    data class Failure(val error: AppError) : AppResult<Nothing>
}

sealed interface AppError {
    data object Network : AppError
    data class Server(val code: Int) : AppError
    /** The response arrived but could not be decoded. */
    data object Parsing : AppError
    data object Unknown : AppError
}
