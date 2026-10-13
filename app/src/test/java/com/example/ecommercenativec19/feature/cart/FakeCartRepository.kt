package com.example.ecommercenativec19.feature.cart

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.feature.cart.domain.Cart
import com.example.ecommercenativec19.feature.cart.domain.CartLine
import com.example.ecommercenativec19.feature.cart.domain.CartRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Hand-written fake shared by the ViewModel tests. It records calls and, like the real repository, applies a count
 * change to [cart] only when it succeeds (the optimistic step and its rollback are the repository's own tests).
 */
class FakeCartRepository : CartRepository {
    private val _cart = MutableStateFlow<Cart?>(null)
    override val cart: StateFlow<Cart?> = _cart

    var refreshResult: AppResult<Unit> = AppResult.Success(Unit)
    var mutationError: AppError? = null

    var refreshCalls = 0
    val addCalls = mutableListOf<Pair<String, Int>>()
    val updateCalls = mutableListOf<Pair<String, Int>>()
    val removeCalls = mutableListOf<String>()

    fun setCart(vararg lines: CartLine, totalPrice: Int = lines.sumOf { it.linePrice }) {
        _cart.value = Cart(lines.toList(), totalPrice)
    }

    fun clear() {
        _cart.value = null
    }

    override suspend fun refresh(): AppResult<Unit> {
        refreshCalls++
        return refreshResult
    }

    override suspend fun addProduct(productId: String, quantity: Int): AppResult<Unit> {
        addCalls.add(productId to quantity)
        return mutationError?.let { AppResult.Failure(it) } ?: AppResult.Success(Unit)
    }

    override suspend fun updateCount(productId: String, count: Int): AppResult<Unit> {
        updateCalls.add(productId to count)
        mutationError?.let { return AppResult.Failure(it) }
        _cart.value = _cart.value?.let { cart ->
            val lines = cart.items.map { if (it.productId == productId) it.copy(count = count) else it }
            Cart(lines, lines.sumOf { it.linePrice })
        }
        return AppResult.Success(Unit)
    }

    override suspend fun remove(productId: String): AppResult<Unit> {
        removeCalls.add(productId)
        mutationError?.let { return AppResult.Failure(it) }
        _cart.value = _cart.value?.let { cart ->
            val lines = cart.items.filterNot { it.productId == productId }
            Cart(lines, lines.sumOf { it.linePrice })
        }
        return AppResult.Success(Unit)
    }
}
