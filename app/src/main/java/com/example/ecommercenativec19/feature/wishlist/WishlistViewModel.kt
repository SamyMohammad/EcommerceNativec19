package com.example.ecommercenativec19.feature.wishlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.common.UiState
import com.example.ecommercenativec19.feature.cart.domain.AddToCartUseCase
import com.example.ecommercenativec19.feature.products.ui.ProductCardUi
import com.example.ecommercenativec19.feature.products.ui.toCardUi
import com.example.ecommercenativec19.feature.wishlist.domain.GetWishlistUseCase
import com.example.ecommercenativec19.feature.wishlist.domain.ObserveFavoriteIdsUseCase
import com.example.ecommercenativec19.feature.wishlist.domain.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Shows the products the server returned, filtered by the shared favorite ids. That makes the heart optimistic for
 * free: the id leaves the set the moment the user taps, the row disappears, and if the request fails the repository
 * puts the id back and the row reappears. The search text is UI-only (no search endpoint is used).
 */
@HiltViewModel
class WishlistViewModel @Inject constructor(
    private val getWishlist: GetWishlistUseCase,
    private val toggleFavorite: ToggleFavoriteUseCase,
    observeFavoriteIds: ObserveFavoriteIdsUseCase,
    private val addToCart: AddToCartUseCase,
) : ViewModel() {
    private val query = MutableStateFlow("")
    private val loaded = MutableStateFlow<UiState<List<ProductCardUi>>>(UiState.Loading)
    private var loadJob: Job? = null

    val state: StateFlow<WishlistUiState> = combine(query, loaded, observeFavoriteIds()) { query, loaded, favoriteIds ->
        WishlistUiState(query = query, items = loaded.onlyFavorites(favoriteIds))
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(StopTimeoutMillis), WishlistUiState())

    fun onQueryChange(value: String) {
        query.value = value
    }

    /**
     * Called each time the screen is shown: favorites added elsewhere since the last visit must appear. Existing rows
     * stay visible while it reloads, and a failed reload does not replace them with an error.
     */
    fun onEnter() = load(keepContent = true)

    fun onRetry() {
        if (loaded.value is UiState.Error) load(keepContent = false)
    }

    fun onToggleFavorite(productId: String) {
        // A failed request is rolled back by the repository, which re-shows the row; no extra error UI in this slice.
        viewModelScope.launch { toggleFavorite(productId) }
    }

    fun onAddToCart(productId: String) {
        // A failed add leaves the cart as it was; no extra error UI in this slice.
        viewModelScope.launch { addToCart(productId) }
    }

    private fun load(keepContent: Boolean) {
        loadJob?.cancel()
        val previous = loaded.value
        if (!(keepContent && previous is UiState.Success)) loaded.value = UiState.Loading
        loadJob = viewModelScope.launch {
            when (val result = getWishlist()) {
                is AppResult.Failure ->
                    if (!(keepContent && previous is UiState.Success)) loaded.value = UiState.Error(result.error)
                is AppResult.Success -> loaded.update {
                    UiState.Success(result.data.map { it.toCardUi() }.distinctBy { it.id })
                }
            }
        }
    }

    private companion object {
        const val StopTimeoutMillis = 5_000L
    }
}

private fun UiState<List<ProductCardUi>>.onlyFavorites(favoriteIds: Set<String>): UiState<List<ProductCardUi>> {
    if (this !is UiState.Success) return this
    val visible = data.filter { it.id in favoriteIds }
    return if (visible.isEmpty()) UiState.Empty else UiState.Success(visible)
}
