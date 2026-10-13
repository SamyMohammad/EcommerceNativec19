package com.example.ecommercenativec19.feature.cart

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.feature.cart.domain.CartLine
import com.example.ecommercenativec19.feature.cart.domain.ObserveCartUseCase
import com.example.ecommercenativec19.feature.cart.domain.RefreshCartUseCase
import com.example.ecommercenativec19.feature.cart.domain.RemoveFromCartUseCase
import com.example.ecommercenativec19.feature.cart.domain.UpdateCartItemCountUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Shows the shared cart cache. A cart that is already cached is shown at once on every visit and refreshed behind it
 * (a failed refresh then keeps what is there); only a screen with no cart at all shows Loading or the error.
 * Count changes go through the repository, which updates the cache optimistically and rolls back on failure, so this
 * ViewModel has no state of its own for them.
 */
class CartViewModel(
    private val observeCart: ObserveCartUseCase,
    private val refreshCart: RefreshCartUseCase,
    private val updateCount: UpdateCartItemCountUseCase,
    private val removeFromCart: RemoveFromCartUseCase,
) : ViewModel() {
    private val cartFlow = observeCart()

    /** Outcome of the latest refresh; only consulted while no cart is cached. */
    private val loadStatus = MutableStateFlow<LoadState<Unit>>(LoadState.Loading)
    private var loadJob: Job? = null

    val state: StateFlow<CartUiState> = combine(cartFlow, loadStatus) { cart, status ->
        when {
            cart == null -> CartUiState(items = if (status is LoadState.Error) status else LoadState.Loading)
            cart.items.isEmpty() -> CartUiState(items = LoadState.Empty)
            else -> CartUiState(items = LoadState.Content(cart.items.map { it.toUi() }), totalPrice = cart.totalPrice)
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(StopTimeoutMillis), CartUiState())

    /** Called each time the screen is shown: the cart may have changed elsewhere (another device, Details, Home). */
    fun onEnter() = load()

    fun onRetry() {
        if (loadStatus.value is LoadState.Error) {
            loadStatus.value = LoadState.Loading
            load()
        }
    }

    fun onIncrement(productId: String) = changeCount(productId, delta = 1)

    /** Going below 1 removes the line (the use case decides), so the quantity pill never shows 0. */
    fun onDecrement(productId: String) = changeCount(productId, delta = -1)

    fun onRemove(productId: String) {
        // A failed request is rolled back by the repository, which re-shows the row; no extra error UI in this slice.
        viewModelScope.launch { removeFromCart(productId) }
    }

    private fun changeCount(productId: String, delta: Int) {
        val current = cartFlow.value?.items?.firstOrNull { it.productId == productId }?.count ?: return
        viewModelScope.launch { updateCount(productId, current + delta) }
    }

    private fun load() {
        loadJob?.cancel()
        loadJob = viewModelScope.launch {
            loadStatus.value = when (val result = refreshCart()) {
                is AppResult.Failure -> LoadState.Error(result.error)
                is AppResult.Success -> LoadState.Content(Unit)
            }
        }
    }

    private companion object {
        const val StopTimeoutMillis = 5_000L
    }
}

private fun CartLine.toUi() = CartLineUi(
    id = productId,
    title = title,
    imageUrl = imageUrl,
    linePrice = linePrice,
    quantity = count,
)
