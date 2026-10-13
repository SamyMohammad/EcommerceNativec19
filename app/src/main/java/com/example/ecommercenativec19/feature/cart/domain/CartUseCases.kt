package com.example.ecommercenativec19.feature.cart.domain

import com.example.ecommercenativec19.core.common.AppResult
import kotlinx.coroutines.flow.StateFlow

class ObserveCartUseCase(private val repository: CartRepository) {
    operator fun invoke(): StateFlow<Cart?> = repository.cart
}

class RefreshCartUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(): AppResult<Unit> = repository.refresh()
}

class AddToCartUseCase(private val repository: CartRepository) {
    /** A [quantity] below 1 adds nothing and reports success. */
    suspend operator fun invoke(productId: String, quantity: Int = 1): AppResult<Unit> =
        if (quantity < 1) AppResult.Success(Unit) else repository.addProduct(productId, quantity)
}

/** A count below 1 means "none left": the API cannot hold a zero-count line, so the product is removed instead. */
class UpdateCartItemCountUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(productId: String, count: Int): AppResult<Unit> =
        if (count < 1) repository.remove(productId) else repository.updateCount(productId, count)
}

class RemoveFromCartUseCase(private val repository: CartRepository) {
    suspend operator fun invoke(productId: String): AppResult<Unit> = repository.remove(productId)
}
