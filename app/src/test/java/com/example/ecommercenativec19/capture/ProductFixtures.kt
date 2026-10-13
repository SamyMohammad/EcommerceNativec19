package com.example.ecommercenativec19.capture

import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.feature.cart.CartLineUi
import com.example.ecommercenativec19.feature.cart.CartUiState
import com.example.ecommercenativec19.feature.home.HomeCategoryUi
import com.example.ecommercenativec19.feature.home.HomeSection
import com.example.ecommercenativec19.feature.home.HomeUiState
import com.example.ecommercenativec19.feature.productdetails.ProductDetailsUi
import com.example.ecommercenativec19.feature.productdetails.ProductDetailsUiState
import com.example.ecommercenativec19.feature.products.ui.ProductCardUi
import com.example.ecommercenativec19.feature.productlist.ProductListUiState
import com.example.ecommercenativec19.feature.wishlist.WishlistUiState

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
    private const val CartImage = "fixture://cart-1"

    private val categoryDrawables = listOf(
        "Women's Fashion" to R.drawable.img_ellipse14_74a31094,
        "Beauty & Health" to R.drawable.img_ellipse18_6ffae414,
        "Men's Fashion" to R.drawable.img_ellipse15_bba3a547,
        "Music" to R.drawable.img_ellipse19_89f52566,
        "Electronics" to R.drawable.img_ellipse16_37eb25ff,
        "Mobiles" to R.drawable.img_ellipse20_11cc609a,
        "Baby & Toys" to R.drawable.img_ellipse17_185ed824,
        "SuperMarket" to R.drawable.img_ellipse21_712ed5ff,
    )

    private val homeCardDrawables = listOf(
        R.drawable.img_frame9_2ef89760, R.drawable.img_frame11_5082f499, R.drawable.img_frame10_e9353cb3,
        R.drawable.img_frame12_d949f599, R.drawable.img_frame13_d6d4d7c2, R.drawable.img_frame14_513d4efb,
        R.drawable.img_frame15_18681980, R.drawable.img_frame17_e0109d22, R.drawable.img_frame18_eb31deae,
    )

    private val wishlistRows = listOf(
        Triple("Nike Air Jordon", 1200 to 1500, R.drawable.img_frame65_2ac7cd2d),
        Triple("Tall Cotton Dress", 600 to 750, R.drawable.img_frame66_4cff3886),
        Triple("GUESS Women’s", 1200 to 1500, R.drawable.img_frame67_70608138),
    )

    private fun wishlistUrl(index: Int) = "fixture://wishlist-$index"
    private fun categoryUrl(name: String) = "fixture://category/$name"
    private fun homeCardUrl(index: Int) = "fixture://home-card-$index"

    private val drawables: Map<String, Int> =
        coverDrawables.toMap() + (DetailsImage to R.drawable.img_property1default1_995b0786) +
            (CartImage to R.drawable.img_frame69_80e90f22) +
            categoryDrawables.associate { (name, drawable) -> categoryUrl(name) to drawable } +
            homeCardDrawables.mapIndexed { index, drawable -> homeCardUrl(index) to drawable } +
            wishlistRows.mapIndexed { index, (_, _, drawable) -> wishlistUrl(index) to drawable }

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

    /** Three rows of three cards; the last row is discounted like the design's smart watches. */
    val home = HomeUiState(
        categories = LoadState.Content(
            categoryDrawables.mapIndexed { index, (name, _) -> HomeCategoryUi("category-$index", name, categoryUrl(name)) },
        ),
        sections = HomeSection.entries.mapIndexed { row, section ->
            section to LoadState.Content(
                List(3) { column ->
                    val index = row * 3 + column
                    ProductCardUi(
                        id = "home-$index",
                        imageUrl = homeCardUrl(index),
                        title = "Home product ${index + 1}",
                        price = 1000 + index * 500,
                        oldPrice = if (row == 2) 2000 else null,
                        rating = 4.3 + (index % 3) * 0.25,
                    )
                },
            )
        }.toMap(),
    )

    /** The design's single row; the total is what the server would send for it. */
    val cart = CartUiState(
        items = LoadState.Content(
            listOf(CartLineUi(id = "cart-1", title = "Nike Air Jordon", imageUrl = CartImage, linePrice = 3500, quantity = 1)),
        ),
        totalPrice = 3500,
    )

    val wishlist = WishlistUiState(
        items = LoadState.Content(
            wishlistRows.mapIndexed { index, (title, prices, _) ->
                ProductCardUi(
                    id = "wishlist-$index",
                    imageUrl = wishlistUrl(index),
                    title = title,
                    price = prices.first,
                    oldPrice = prices.second,
                    rating = 4.5,
                )
            },
        ),
    )
}
