package com.example.ecommercenativec19.feature.categories

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.unit.Dp
import com.example.ecommercenativec19.core.ui.FigmaImageCrop

/**
 * One left-rail entry. [isGap] marks the empty slot the design leaves between "Beauty" and "Cameras" (an entry
 * without a label); it only reserves one row of space.
 */
data class RailCategory(
    val id: String,
    @param:StringRes val label: Int?,
    val isSelected: Boolean = false,
)

/**
 * A sub-category tile of the selected category. [opensProductList]: the prototype links this tile to Product list.
 * [labelStart]: the design's x of the label's left edge inside the tile (null = centered).
 */
data class SubCategory(
    val id: String,
    @param:StringRes val label: Int,
    @param:DrawableRes val image: Int,
    val imageCrop: FigmaImageCrop? = null,
    val opensProductList: Boolean = false,
    val labelStart: Dp? = null,
)

/** Everything the stateless Categories screen renders (content of the selected rail entry, Men's Fashion). */
data class CategoriesUiState(
    val searchQuery: String = "",
    val railCategories: List<RailCategory> = emptyList(),
    @param:StringRes val selectedTitle: Int? = null,
    val subCategories: List<SubCategory> = emptyList(),
)
