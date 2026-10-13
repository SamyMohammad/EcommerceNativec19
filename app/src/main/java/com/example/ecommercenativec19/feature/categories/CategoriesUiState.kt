package com.example.ecommercenativec19.feature.categories

/** Load state of one independently loading region (the rail or the sub-category grid). */
enum class LoadStatus { Loading, Error, Empty, Content }

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
    val railStatus: LoadStatus = LoadStatus.Loading,
    val railCategories: List<RailCategory> = emptyList(),
    val contentStatus: LoadStatus = LoadStatus.Loading,
    val selectedTitle: String? = null,
    val subCategories: List<SubCategoryItem> = emptyList(),
)
