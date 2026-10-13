package com.example.ecommercenativec19.feature.categories

import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.FigmaImageCrop

/**
 * DESIGN PLACEHOLDER CONTENT copied from the Figma Categories frame (Men's Fashion selected). There is no data
 * contract yet and the design shows content for the selected category only.
 */
internal object CategoriesSampleData {
    val rail = listOf(
        RailCategory("mens_fashion", R.string.categories_rail_mens_fashion, isSelected = true),
        RailCategory("womens_fashion", R.string.categories_rail_womens_fashion),
        RailCategory("skincare", R.string.categories_rail_skincare),
        RailCategory("beauty", R.string.categories_rail_beauty),
        RailCategory("gap", label = null),
        RailCategory("cameras", R.string.categories_rail_cameras),
        RailCategory("laptops", R.string.categories_rail_laptops),
        RailCategory("baby_toys", R.string.categories_rail_baby_toys),
    )

    /** Row-major, three per row. */
    val subCategories = listOf(
        SubCategory("tshirts", R.string.categories_sub_tshirts, R.drawable.img_frame47_d49b49b6, labelStart = 8.dp),
        SubCategory("shorts", R.string.categories_sub_shorts, R.drawable.img_frame48_2c1f745a, labelStart = 14.dp),
        SubCategory("jeans", R.string.categories_sub_jeans, R.drawable.img_frame49_7429ac9a, labelStart = 15.dp),
        SubCategory("pants", R.string.categories_sub_pants, R.drawable.img_frame50_c31d7f36, labelStart = 16.dp),
        SubCategory(
            "footwear", R.string.categories_sub_footwear, R.drawable.img_frame54_44a2aecd,
            opensProductList = true,
            labelStart = 9.dp,
        ),
        SubCategory("suits", R.string.categories_sub_suits, R.drawable.img_frame55_a1916367, labelStart = 22.dp),
        SubCategory("watches", R.string.categories_sub_watches, R.drawable.img_frame51_9db723c2, labelStart = 6.dp),
        SubCategory(
            "bags", R.string.categories_sub_bags, R.drawable.img_frame52_13766ca6,
            imageCrop = FigmaImageCrop(heightFraction = 1.3333f, offsetXFraction = 0.0094f, offsetYFraction = -0.0255f),
            labelStart = 18.dp,
        ),
        SubCategory(
            "eyewears", R.string.categories_sub_eyewears, R.drawable.img_frame53_0d23da9d,
            imageCrop = FigmaImageCrop(heightFraction = 1.5f, offsetXFraction = 0.0131f, offsetYFraction = -0.4068f),
            labelStart = 1.dp,
        ),
    )
}
