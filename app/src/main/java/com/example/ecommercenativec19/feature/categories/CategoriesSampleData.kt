package com.example.ecommercenativec19.feature.categories

import com.example.ecommercenativec19.core.common.UiState
import com.example.ecommercenativec19.feature.categories.domain.Category
import com.example.ecommercenativec19.feature.categories.domain.SubCategory

/** Static content for @Preview and screenshot tests; the running app loads real data from the API. */
internal object CategoriesSampleData {
    val rail = listOf(
        Category("mens_fashion", "Men’s Fashion", imageUrl = null),
        Category("womens_fashion", "Women’s Fashion", imageUrl = null),
        Category("skincare", "Skincare", imageUrl = null),
        Category("beauty", "Beauty", imageUrl = null),
        Category("cameras", "Cameras", imageUrl = null),
        Category("laptops", "Laptops & Electronics", imageUrl = null),
        Category("baby_toys", "Baby & Toys", imageUrl = null),
    )

    /** Row-major, three per row. */
    val subCategories = listOf(
        SubCategory("tshirts", "T-shirts"),
        SubCategory("shorts", "Shorts"),
        SubCategory("jeans", "Jeans"),
        SubCategory("pants", "Pants"),
        SubCategory("footwear", "Footwear"),
        SubCategory("suits", "Suits"),
        SubCategory("watches", "Watches"),
        SubCategory("bags", "Bags"),
        SubCategory("eyewears", "Eyewears"),
    )

    val state = CategoriesUiState(
        categories = UiState.Success(rail),
        selectedCategoryId = "mens_fashion",
        selectedTitle = "Men’s Fashion",
        subCategories = UiState.Success(subCategories),
    )
}
