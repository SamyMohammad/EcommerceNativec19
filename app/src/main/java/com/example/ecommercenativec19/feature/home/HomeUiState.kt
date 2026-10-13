package com.example.ecommercenativec19.feature.home

import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.feature.products.ui.ProductCardUi

/** A circular tile of the Home categories grid. */
data class HomeCategoryUi(
    val id: String,
    val name: String,
    val imageUrl: String?,
)

/**
 * The product rows below the categories, in display order. The API has no "home feed" endpoint, so each row is the
 * same product list under a different ranking (see docs/api/home.md).
 */
enum class HomeSection { BestSellers, NewArrival, TopRated }

/**
 * Everything the stateless Home screen renders. The categories grid and every product row load independently, so
 * one failing request shows its own retry and leaves the other regions alone.
 */
data class HomeUiState(
    val searchQuery: String = "",
    val categories: LoadState<List<HomeCategoryUi>> = LoadState.Loading,
    val sections: Map<HomeSection, LoadState<List<ProductCardUi>>> = HomeSection.entries.associateWith { LoadState.Loading },
    /** Mirror of the shared wishlist cache; kept apart from [sections] so reloads do not wipe the hearts. */
    val favoriteIds: Set<String> = emptySet(),
)
