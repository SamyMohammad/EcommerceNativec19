package com.example.ecommercenativec19.feature.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.core.ui.FigmaImageCrop

/**
 * A circular category tile of the Home categories grid. [linksToCategories]: the prototype opens Categories from it.
 * The design places a few tiles by hand: [columnGap] is the space before this tile's column (taken from the column's
 * top tile), [imageWidth] / [imageOffsetX] / [labelOffsetX] its photo width, photo offset and label offset.
 */
data class HomeCategory(
    val id: String,
    @param:StringRes val label: Int,
    @param:DrawableRes val image: Int,
    val linksToCategories: Boolean = false,
    val columnGap: Dp = 16.dp,
    val imageWidth: Dp? = null,
    val imageOffsetX: Dp = 0.dp,
    val labelOffsetX: Dp = 0.dp,
)

/**
 * Product card content. [price] / [oldPrice] are amounts only ("9000"); the currency wording is a string resource.
 * [rating] is the number shown in "Review (4.8)".
 */
data class HomeProduct(
    val id: String,
    @param:StringRes val title: Int,
    val price: String,
    val rating: String,
    @param:DrawableRes val image: Int,
    val oldPrice: String? = null,
    val imageCrop: FigmaImageCrop? = null,
)

/** Everything the stateless Home screen renders. */
data class HomeUiState(
    val searchQuery: String = "",
    /** Column-major: consecutive pairs form one column (top, bottom) of the two-row categories grid. */
    val categories: List<HomeCategory> = emptyList(),
    val applianceProducts: List<HomeProduct> = emptyList(),
    val newArrivalProducts: List<HomeProduct> = emptyList(),
    val smartWatchProducts: List<HomeProduct> = emptyList(),
    val favoriteIds: Set<String> = emptySet(),
)
