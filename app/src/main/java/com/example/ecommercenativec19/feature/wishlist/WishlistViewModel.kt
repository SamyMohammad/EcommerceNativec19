package com.example.ecommercenativec19.feature.wishlist

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Local UI state of the Wishlist (no persistence, no backend, no search behavior). The heart toggles the row's
 * favorite flag only; the row stays so the toggle is reversible.
 */
class WishlistViewModel(
    initial: WishlistUiState = WishlistUiState(items = WishlistSampleData.items),
) : ViewModel() {
    private val _state = MutableStateFlow(initial)
    val state: StateFlow<WishlistUiState> = _state.asStateFlow()

    fun onQueryChange(query: String) = _state.update { it.copy(query = query) }

    fun onToggleFavorite(id: String) = _state.update { s ->
        s.copy(items = s.items.map { if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it })
    }
}
