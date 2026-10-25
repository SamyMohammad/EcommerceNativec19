package com.example.ecommercenativec19.feature.wishlist.domain

import com.example.ecommercenativec19.core.common.AppResult
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(private val repository: WishlistRepository) {
    suspend operator fun invoke(productId: String): AppResult<Unit> = repository.toggle(productId)
}
