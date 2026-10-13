package com.example.ecommercenativec19.core.common

/**
 * State of one independently loading region. A single sealed type per region makes contradictory combinations
 * (for example "Loading with an error") unrepresentable, unlike a status enum next to parallel data fields.
 */
sealed interface LoadState<out T> {
    data object Loading : LoadState<Nothing>
    data class Content<T>(val data: T) : LoadState<T>

    /** The load succeeded but there is nothing to show. */
    data object Empty : LoadState<Nothing>
    data class Error(val error: AppError) : LoadState<Nothing>
}
