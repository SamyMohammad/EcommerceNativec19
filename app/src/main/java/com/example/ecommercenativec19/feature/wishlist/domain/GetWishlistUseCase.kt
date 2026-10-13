package com.example.ecommercenativec19.feature.wishlist.domain

import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.feature.products.domain.Product

class GetWishlistUseCase(private val repository: WishlistRepository) {
    suspend operator fun invoke(): AppResult<List<Product>> = repository.refresh()
}
