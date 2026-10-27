package com.example.ecommercenativec19.feature.wishlist.domain

import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

class ObserveFavoriteIdsUseCase @Inject constructor(private val repository: WishlistRepository) {
    operator fun invoke(): StateFlow<Set<String>> = repository.favoriteIds
}
