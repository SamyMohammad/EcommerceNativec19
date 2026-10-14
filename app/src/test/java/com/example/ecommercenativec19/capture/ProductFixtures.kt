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

/** Screenshot fixtures for the product screens. Pictures are the bundled drawables, so no loader is needed. */
internal object ProductFixtures {
    private const val Title = "Nike Air Jordon - Nike shoes flexible for women"

    private val coverDrawables = listOf(
        R.drawable.img_frame68_80e9b516,
        R.drawable.img_frame73_b6c21927,
        R.drawable.img_frame69_80e90f22,
        R.drawable.img_frame71_25193b09,
        R.drawable.img_frame70_3f7a88e3,
        R.drawable.img_frame72_d0f73343,
    )
    private val detailsImage = R.drawable.img_property1default1_995b0786
    private val cartImage = R.drawable.img_frame69_80e90f22

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

    private val prices = listOf(1200 to 2000, 1100 to 1500, 1000 to 1300, 1500 to 2000, 1800 to 2100, 1900 to 2500)
    private val ratings = listOf(4.6, 4.5, 4.8, 4.8, 4.2, 3.8)

    val productList = ProductListUiState(
        products = LoadState.Content(
            coverDrawables.mapIndexed { index, drawable ->
                ProductCardUi(
                    id = "product-${index + 1}",
                    imageRes = drawable,
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
                images = List(3) { detailsImage },
            ),
        ),
    )

    /** Three rows of three cards; the last row is discounted like the design's smart watches. */
    val home = HomeUiState(
        categories = LoadState.Content(
            categoryDrawables.mapIndexed { index, (name, drawable) -> HomeCategoryUi("category-$index", name, drawable) },
        ),
        sections = HomeSection.entries.mapIndexed { row, section ->
            section to LoadState.Content(
                List(3) { column ->
                    val index = row * 3 + column
                    ProductCardUi(
                        id = "home-$index",
                        imageRes = homeCardDrawables[index],
                        title = "Home product ${index + 1}",
                        price = 1000 + index * 500,
                        oldPrice = if (row == 2) 2000 else null,
                        rating = 4.3 + (index % 3) * 0.25,
                    )
                },
            )
        }.toMap(),
    )

    /** The design's single row. */
    val cart = CartUiState(
        items = LoadState.Content(
            listOf(CartLineUi(id = "cart-1", title = "Nike Air Jordon", imageRes = cartImage, linePrice = 3500, quantity = 1)),
        ),
        totalPrice = 3500,
    )

    val wishlist = WishlistUiState(
        items = LoadState.Content(
            wishlistRows.mapIndexed { index, (title, prices, drawable) ->
                ProductCardUi(
                    id = "wishlist-$index",
                    imageRes = drawable,
                    title = title,
                    price = prices.first,
                    oldPrice = prices.second,
                    rating = 4.5,
                )
            },
        ),
    )
}
