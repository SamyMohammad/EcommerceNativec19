package com.example.ecommercenativec19.feature.categories

import com.example.ecommercenativec19.core.common.LoadState

/** One left-rail entry. */
data class RailCategory(
    val id: String,
    val label: String,
    val isSelected: Boolean = false,
)

/** A sub-category tile of the selected category. */
data class SubCategoryItem(
    val id: String,
    val label: String,
)

/** Everything the stateless Categories screen renders. Rail and grid load and fail independently. */
data class CategoriesUiState(
    val searchQuery: String = "",
    val categories: LoadState<List<RailCategory>> = LoadState.Loading,
    val selectedTitle: String? = null,
    val subCategories: LoadState<List<SubCategoryItem>> = LoadState.Loading,
)
