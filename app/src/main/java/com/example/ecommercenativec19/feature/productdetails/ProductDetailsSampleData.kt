package com.example.ecommercenativec19.feature.productdetails

import com.example.ecommercenativec19.R

/**
 * DESIGN PLACEHOLDER content, copied from the Figma frame "Product Details" (620:1083). There is no data contract
 * or backend yet: every product id shows this same single product, and the carousel repeats the one exported
 * image for its three pages (the design shows three indicators but exports only one photo). Replace once a data
 * contract exists.
 */
internal object ProductDetailsSampleData {
    fun forProductId(productId: String): ProductDetailsUiState = ProductDetailsUiState(
        productId = productId,
        title = "Nike Air Jordon",
        price = "EGP 3,500",
        soldLabel = "3,230 Sold",
        ratingLabel = "4.8 (7,500)",
        description = "Nike is a multinational corporation that designs, develops, and sells athletic footwear " +
            ",apparel, and accessories......",
        images = List(3) { R.drawable.img_property1default1_995b0786 },
        sizes = listOf("38", "39", "40", "41", "42"),
        colors = ProductColor.entries,
        totalPrice = "EGP 3,500",
        selectedSize = "40",
        selectedColor = ProductColor.Red,
    )
}
