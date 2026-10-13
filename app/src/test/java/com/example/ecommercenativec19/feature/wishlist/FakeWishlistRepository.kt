package com.example.ecommercenativec19.feature.wishlist

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.feature.products.domain.Product
import com.example.ecommercenativec19.feature.wishlist.domain.WishlistRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Hand-written fake shared by the ViewModel tests. Like the real repository, a successful [toggle] flips the id in
 * [favoriteIds] and a failed one leaves it as it was (the rollback has already happened by the time it returns).
 */
class FakeWishlistRepository : WishlistRepository {
    private val _favoriteIds = MutableStateFlow<Set<String>>(emptySet())
    override val favoriteIds: StateFlow<Set<String>> = _favoriteIds

    /** On success the favorite ids are replaced by the ids of these products, like the real repository does. */
    var refreshResult: AppResult<List<Product>> = AppResult.Success(emptyList())
    var toggleError: AppError? = null

    var refreshCalls = 0
    val toggleCalls = mutableListOf<String>()

    fun setIds(ids: Set<String>) {
        _favoriteIds.value = ids
    }

    override suspend fun refresh(): AppResult<List<Product>> {
        refreshCalls++
        (refreshResult as? AppResult.Success)?.let { success -> _favoriteIds.value = success.data.map { it.id }.toSet() }
        return refreshResult
    }

    override suspend fun toggle(productId: String): AppResult<Unit> {
        toggleCalls.add(productId)
        toggleError?.let { return AppResult.Failure(it) }
        _favoriteIds.value = if (productId in _favoriteIds.value) _favoriteIds.value - productId else _favoriteIds.value + productId
        return AppResult.Success(Unit)
    }
}
