package com.example.ecommercenativec19.feature.categories

import com.example.ecommercenativec19.core.common.LoadState

/** Static content for @Preview and screenshot tests; the running app loads real data from the API. */
internal object CategoriesSampleData {
    val rail = listOf(
        RailCategory("mens_fashion", "Men’s Fashion", isSelected = true),
        RailCategory("womens_fashion", "Women’s Fashion"),
        RailCategory("skincare", "Skincare"),
        RailCategory("beauty", "Beauty"),
        RailCategory("cameras", "Cameras"),
        RailCategory("laptops", "Laptops & Electronics"),
        RailCategory("baby_toys", "Baby & Toys"),
    )

    /** Row-major, three per row. */
    val subCategories = listOf(
        SubCategoryItem("tshirts", "T-shirts"),
        SubCategoryItem("shorts", "Shorts"),
        SubCategoryItem("jeans", "Jeans"),
        SubCategoryItem("pants", "Pants"),
        SubCategoryItem("footwear", "Footwear"),
        SubCategoryItem("suits", "Suits"),
        SubCategoryItem("watches", "Watches"),
        SubCategoryItem("bags", "Bags"),
        SubCategoryItem("eyewears", "Eyewears"),
    )

    val state = CategoriesUiState(
        categories = LoadState.Content(rail),
        selectedTitle = "Men’s Fashion",
        subCategories = LoadState.Content(subCategories),
    )
}
