package com.example.ecommercenativec19.capture

import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.feature.productdetails.ProductDetailsUi
import com.example.ecommercenativec19.feature.productdetails.ProductDetailsUiState
import com.example.ecommercenativec19.feature.productlist.ProductCardUi
import com.example.ecommercenativec19.feature.productlist.ProductListUiState

/**
 * Screenshot fixtures for the product screens. Image URLs are fake; the capture test maps each one to a bundled
 * drawable through Coil's preview handler, so the PNGs show pictures without any network access.
 */
internal object ProductFixtures {
    private const val Title = "Nike Air Jordon - Nike shoes flexible for women"

    private val coverDrawables = listOf(
        "fixture://product-1" to R.drawable.img_frame68_80e9b516,
        "fixture://product-2" to R.drawable.img_frame73_b6c21927,
        "fixture://product-3" to R.drawable.img_frame69_80e90f22,
        "fixture://product-4" to R.drawable.img_frame71_25193b09,
        "fixture://product-5" to R.drawable.img_frame70_3f7a88e3,
        "fixture://product-6" to R.drawable.img_frame72_d0f73343,
    )
    private const val DetailsImage = "fixture://details"

    private val drawables: Map<String, Int> =
        coverDrawables.toMap() + (DetailsImage to R.drawable.img_property1default1_995b0786)

    fun drawableFor(url: String): Int = drawables.getValue(url)

    private val prices = listOf(1200 to 2000, 1100 to 1500, 1000 to 1300, 1500 to 2000, 1800 to 2100, 1900 to 2500)
    private val ratings = listOf(4.6, 4.5, 4.8, 4.8, 4.2, 3.8)

    val productList = ProductListUiState(
        products = LoadState.Content(
            coverDrawables.mapIndexed { index, (url, _) ->
                ProductCardUi(
                    id = "product-${index + 1}",
                    imageUrl = url,
                    title = Title,
                    price = prices[index].first,
                    oldPrice = prices[index].second,
                    rating = ratings[index],
                )
            },
        ),
    )

    val productDetails = ProductDetailsUiState(
        product = LoadState.Content(
            ProductDetailsUi(
                id = "product-1",
                title = "Nike Air Jordon",
                unitPrice = 3500,
                soldCount = 3230,
                rating = 4.8,
                ratingCount = 7500,
                description = "Nike is a multinational corporation that designs, develops, and sells athletic footwear " +
                    ",apparel, and accessories......",
                images = List(3) { DetailsImage },
            ),
        ),
    )
}
