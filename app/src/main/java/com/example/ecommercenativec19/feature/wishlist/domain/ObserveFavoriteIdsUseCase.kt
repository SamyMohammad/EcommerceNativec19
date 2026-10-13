package com.example.ecommercenativec19.feature.wishlist.domain

import kotlinx.coroutines.flow.StateFlow

class ObserveFavoriteIdsUseCase(private val repository: WishlistRepository) {
    operator fun invoke(): StateFlow<Set<String>> = repository.favoriteIds
}
