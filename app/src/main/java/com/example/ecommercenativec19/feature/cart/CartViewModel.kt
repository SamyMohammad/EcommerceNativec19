package com.example.ecommercenativec19.feature.cart

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Local UI state of the Cart (no persistence, no backend). Quantity changes and removals only edit the list;
 * the minimum quantity of 1 is the only limit (the design defines none, 0 would be an unspecified state).
 */
class CartViewModel(
    initial: CartUiState = CartUiState(CartSampleData.items, CartSampleData.TOTAL_PRICE),
) : ViewModel() {
    private val _state = MutableStateFlow(initial)
    val state: StateFlow<CartUiState> = _state.asStateFlow()

    fun onIncrement(id: String) = updateItem(id) { it.copy(quantity = it.quantity + 1) }

    fun onDecrement(id: String) = updateItem(id) { it.copy(quantity = (it.quantity - 1).coerceAtLeast(1)) }

    fun onRemove(id: String) = _state.update { s -> s.copy(items = s.items.filterNot { it.id == id }) }

    private fun updateItem(id: String, transform: (CartItemUi) -> CartItemUi) =
        _state.update { s -> s.copy(items = s.items.map { if (it.id == id) transform(it) else it }) }
}
