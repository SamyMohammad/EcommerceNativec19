package com.example.ecommercenativec19.feature.wishlist

import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.ProductVariantDot

/**
 * DESIGN PLACEHOLDER content copied from the Figma Wishlist frame (node 731:1294). There is no wishlist data
 * contract yet; replace with real data when one exists.
 */
internal object WishlistSampleData {
    val items = listOf(
        WishlistItemUi(
            id = "nike-air-jordon",
            title = "Nike Air Jordon",
            price = "EGP 1,200",
            oldPrice = "EGP 1,500",
            image = R.drawable.img_frame65_2ac7cd2d,
            crop = null,
            dot = ProductVariantDot.Charcoal,
            variantLabel = "Black color",
        ),
        WishlistItemUi(
            id = "tall-cotton-dress",
            title = "Tall Cotton Dress",
            price = "EGP 600",
            oldPrice = "EGP 750",
            image = R.drawable.img_frame66_4cff3886,
            crop = ImageCrop(heightFraction = 1.284f, offsetXFraction = 0.006f, offsetYFraction = -0.178f),
            dot = ProductVariantDot.Amber,
            variantLabel = "Orange color",
        ),
        WishlistItemUi(
            id = "guess-womens",
            title = "GUESS Women’s",
            price = "EGP 1,200",
            oldPrice = "EGP 1,500",
            image = R.drawable.img_frame67_70608138,
            crop = ImageCrop(heightFraction = 1.4159f, offsetXFraction = -0.0056f, offsetYFraction = 0.0124f),
            dot = ProductVariantDot.Peach,
            variantLabel = "Gold Watch",
            extraGapBefore = 13.dp,
        ),
    )
}
