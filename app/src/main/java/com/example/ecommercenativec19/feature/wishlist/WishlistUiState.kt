package com.example.ecommercenativec19.feature.wishlist

import androidx.annotation.DrawableRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.core.ui.FigmaImageCrop
import com.example.ecommercenativec19.core.ui.ProductVariantDot

/** One wishlist row. Text arrives formatted. [isFavorite] is the local heart state. */
data class WishlistItemUi(
    val id: String,
    val title: String,
    val price: String,
    val oldPrice: String,
    @param:DrawableRes val image: Int,
    /** Null means a plain cover fill. */
    val crop: FigmaImageCrop?,
    val dot: ProductVariantDot,
    val variantLabel: String,
    val isFavorite: Boolean = true,
    /** Extra space above this row on top of the list gap; the design leaves 37dp (not 24) before the third row. */
    val extraGapBefore: Dp = 0.dp,
)

data class WishlistUiState(
    val query: String = "",
    val items: List<WishlistItemUi> = emptyList(),
)
