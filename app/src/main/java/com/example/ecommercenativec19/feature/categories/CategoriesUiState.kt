package com.example.ecommercenativec19.feature.categories

import com.example.ecommercenativec19.core.common.UiState
import com.example.ecommercenativec19.feature.categories.domain.Category
import com.example.ecommercenativec19.feature.categories.domain.SubCategory

/** Everything the stateless Categories screen renders. Rail and grid load and fail independently. */
data class CategoriesUiState(
    val searchQuery: String = "",
    val categories: UiState<List<Category>> = UiState.Loading,
    val selectedCategoryId: String? = null,
    val selectedTitle: String? = null,
    val subCategories: UiState<List<SubCategory>> = UiState.Loading,
)
