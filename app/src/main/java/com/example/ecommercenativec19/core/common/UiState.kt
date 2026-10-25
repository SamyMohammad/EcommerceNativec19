package com.example.ecommercenativec19.core.common

/**
 * State of one independently loading region. A single sealed type per region makes contradictory combinations
 * (for example "Loading with an error") unrepresentable, unlike a status enum next to parallel data fields.
 */
sealed interface UiState<out T> {
    data object Loading : UiState<Nothing>
    data class Success<T>(val data: T) : UiState<T>

    /** The load succeeded but there is nothing to show. */
    data object Empty : UiState<Nothing>
    data class Error(val error: AppError) : UiState<Nothing>
}
