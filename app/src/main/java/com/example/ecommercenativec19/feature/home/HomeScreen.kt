package com.example.ecommercenativec19.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.ui.ProductCardRatingPosition
import com.example.ecommercenativec19.core.ui.SearchHeader

/**
 * Stateless Home screen: fixed logo/search/cart header and bottom bar around a vertically scrolling body
 * (promo banner, categories, Home Appliance, footwear banner, New Arrival, Smart Watch).
 */
@Composable
fun HomeScreen(
    state: HomeUiState,
    onQueryChange: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onOpenCart: () -> Unit,
    onOpenCategories: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.background)) {
        SearchHeader(
            query = state.searchQuery,
            onQueryChange = onQueryChange,
            onCartClick = onOpenCart,
            logoToSearchGap = HomeLogoToSearchGap,
            // The home frame puts the logo and the field at x 17 and the cart icon at x 389 (the others: 16 / 390).
            horizontalInset = 17.dp,
            searchToCartGap = 24.dp,
        )
        LazyColumn(
            modifier = Modifier.weight(1f).fillMaxWidth(),
            contentPadding = PaddingValues(top = BannerTopGap, bottom = BottomContentGap),
        ) {
            item(key = "promo", contentType = "banner") { PromoBanner(Modifier.padding(horizontal = AppDimens.ScreenHorizontal)) }
            item(key = "categories-header", contentType = "header") {
                HomeSectionHeader(
                    title = stringResource(R.string.home_section_categories),
                    trailing = stringResource(R.string.home_view_all),
                )
            }
            item(key = "categories", contentType = "categories") { HomeCategoriesGrid(state.categories, onOpenCategories) }
            item(key = "appliance-header", contentType = "header") { HomeSectionHeader(stringResource(R.string.home_section_appliance)) }
            item(key = "appliance", contentType = "products") {
                HomeProductRow(
                    products = state.applianceProducts,
                    favoriteIds = state.favoriteIds,
                    onFavoriteClick = onFavoriteClick,
                    ratingPosition = ProductCardRatingPosition.AbovePrice,
                    minCardHeight = CompactCardHeight,
                )
            }
            item(key = "footwear", contentType = "banner") {
                FootwearBanner(
                    Modifier.padding(
                        top = SectionGap,
                        start = AppDimens.ScreenHorizontal,
                        end = AppDimens.ScreenHorizontal,
                    ),
                )
            }
            item(key = "new-arrival-header", contentType = "header") { HomeSectionHeader(stringResource(R.string.home_section_new_arrival)) }
            item(key = "new-arrival", contentType = "products") {
                HomeProductRow(
                    products = state.newArrivalProducts,
                    favoriteIds = state.favoriteIds,
                    onFavoriteClick = onFavoriteClick,
                    ratingPosition = ProductCardRatingPosition.AbovePrice,
                    minCardHeight = CompactCardHeight,
                )
            }
            item(key = "smart-watch-header", contentType = "header") { HomeSectionHeader(stringResource(R.string.home_section_smart_watch)) }
            item(key = "smart-watch", contentType = "products") {
                HomeProductRow(
                    products = state.smartWatchProducts,
                    favoriteIds = state.favoriteIds,
                    onFavoriteClick = onFavoriteClick,
                    ratingPosition = ProductCardRatingPosition.BelowPrice,
                    minCardHeight = SmartWatchCardHeight,
                )
            }
        }
    }
}

/** Home's search field sits at y 90 (one dp higher than on the other screens): logo bottom 72 + 18. */
private val HomeLogoToSearchGap = 18.dp

/** Search field bottom (y 140) to banner top (y 156). */
private val BannerTopGap = 16.dp
private val BottomContentGap = 24.dp

/** Home product card heights in the design (without / with old price). */
internal val CompactCardHeight = 211.dp
internal val SmartWatchCardHeight = 235.dp

/** Vertical gap between a block and the next section title (y 200 -> 224, 544 -> 568 ...). */
internal val SectionGap = 24.dp
