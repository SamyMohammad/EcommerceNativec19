package com.example.ecommercenativec19.feature.home

import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.FigmaImageCrop

/**
 * DESIGN PLACEHOLDER CONTENT. These categories and products are copied from the Figma Home frame so the screen can
 * be shown; there is no data contract (API / repository) yet. Replace by real data when one exists.
 */
internal object HomeSampleData {
    /** Column-major: (Women, Beauty), (Men, Headphones), (Laptops, Skincare), (Baby, Cameras). */
    val categories = listOf(
        HomeCategory("womens_fashion", R.string.home_category_womens_fashion, R.drawable.img_ellipse14_74a31094),
        HomeCategory("beauty", R.string.home_category_beauty, R.drawable.img_ellipse18_6ffae414),
        HomeCategory(
            "mens_fashion", R.string.home_category_mens_fashion, R.drawable.img_ellipse15_bba3a547,
            linksToCategories = true,
        ),
        HomeCategory("headphones", R.string.home_category_headphones, R.drawable.img_ellipse19_89f52566),
        // Column 3 starts at x 236 (laptops photo at 232, skincare photo 96 wide); column 4 at x 348 with labels
        // centered 7.5 right of the photo; the cameras photo is 91 wide (all from the Figma Home frame).
        HomeCategory(
            "laptops", R.string.home_category_laptops, R.drawable.img_ellipse16_37eb25ff,
            columnGap = 20.dp, imageOffsetX = (-4).dp,
        ),
        HomeCategory("skincare", R.string.home_category_skincare, R.drawable.img_ellipse20_11cc609a, imageWidth = 96.dp),
        HomeCategory(
            "baby_toys", R.string.home_category_baby_toys, R.drawable.img_ellipse17_185ed824,
            columnGap = 12.dp, labelOffsetX = 7.5.dp,
        ),
        HomeCategory(
            "cameras", R.string.home_category_cameras, R.drawable.img_ellipse21_712ed5ff,
            imageWidth = 91.dp, labelOffsetX = 7.5.dp,
        ),
    )

    val applianceProducts = listOf(
        HomeProduct("washing_machine", R.string.home_product_washing_machine, "9000", "4.8", R.drawable.img_frame9_2ef89760),
        HomeProduct("new_cooker", R.string.home_product_new_cooker, "8000", "4.7", R.drawable.img_frame11_5082f499),
        HomeProduct(
            "steam_iron", R.string.home_product_steam_iron, "2000", "4.8", R.drawable.img_frame10_e9353cb3,
            imageCrop = FigmaImageCrop(heightFraction = 1.8139f, offsetXFraction = 0.004f, offsetYFraction = -0.3242f),
        ),
    )

    val newArrivalProducts = listOf(
        HomeProduct("makeup_palette", R.string.home_product_makeup_palette, "3000", "4.8", R.drawable.img_frame12_d949f599),
        HomeProduct("makeup_brushes", R.string.home_product_makeup_brushes, "1000", "4.8", R.drawable.img_frame13_d6d4d7c2),
        HomeProduct(
            "red_rouge", R.string.home_product_red_rouge, "200", "4.8", R.drawable.img_frame14_513d4efb,
            imageCrop = FigmaImageCrop(heightFraction = 1.6189f, offsetYFraction = 0.0082f),
        ),
    )

    val smartWatchProducts = listOf(
        HomeProduct(
            "watch_1", R.string.home_product_xiaomi_watch, "1,800", "4.3", R.drawable.img_frame15_18681980,
            oldPrice = "2000",
        ),
        HomeProduct(
            "watch_2", R.string.home_product_xiaomi_watch, "1,500", "4.8", R.drawable.img_frame17_e0109d22,
            oldPrice = "2000",
            imageCrop = FigmaImageCrop(heightFraction = 1.2789f, offsetYFraction = -0.0732f),
        ),
        HomeProduct(
            "watch_3", R.string.home_product_xiaomi_watch, "1000", "4.3", R.drawable.img_frame18_eb31deae,
            oldPrice = "2000",
        ),
    )
}
