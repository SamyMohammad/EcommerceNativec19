package com.example.ecommercenativec19.feature.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.core.ui.SearchHeader
import com.example.ecommercenativec19.core.ui.StatusPane

/**
 * Stateless Home screen: fixed logo/search/cart header and bottom bar around a vertically scrolling body
 * (promo banner, categories, then the product rows with the footwear banner after the first one).
 */
@Composable
fun HomeScreen(
    state: HomeUiState,
    onQueryChange: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onAddToCart: (String) -> Unit,
    onProductClick: (String) -> Unit,
    onOpenCart: () -> Unit,
    onOpenCategories: () -> Unit,
    onRetryCategories: () -> Unit,
    onRetrySection: (HomeSection) -> Unit,
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
            item(key = "categories", contentType = "categories") {
                StatusPane(
                    state = state.categories,
                    onRetry = onRetryCategories,
                    emptyText = stringResource(R.string.home_categories_empty),
                    modifier = Modifier.fillMaxWidth().heightIn(min = CategoriesRegionHeight),
                ) { categories -> HomeCategoriesGrid(categories, onOpenCategories) }
            }
            // A section that loaded but has no products is dropped entirely, header included.
            state.sections.filterValues { it != LoadState.Empty }.entries.forEachIndexed { index, (section, products) ->
                item(key = "${section.name}-header", contentType = "header") { HomeSectionHeader(stringResource(section.title())) }
                item(key = section.name, contentType = "products") {
                    StatusPane(
                        state = products,
                        onRetry = { onRetrySection(section) },
                        emptyText = "",
                        modifier = Modifier.fillMaxWidth().height(HomeCardHeight),
                    ) { cards ->
                        HomeProductRow(
                            products = cards,
                            favoriteIds = state.favoriteIds,
                            onProductClick = onProductClick,
                            onFavoriteClick = onFavoriteClick,
                            onAddClick = onAddToCart,
                        )
                    }
                }
                if (index == 0) {
                    item(key = "footwear", contentType = "banner") {
                        FootwearBanner(
                            Modifier.padding(
                                top = SectionGap,
                                start = AppDimens.ScreenHorizontal,
                                end = AppDimens.ScreenHorizontal,
                            ),
                        )
                    }
                }
            }
        }
    }
}

@StringRes
private fun HomeSection.title() = when (this) {
    HomeSection.BestSellers -> R.string.home_section_best_sellers
    HomeSection.NewArrival -> R.string.home_section_new_arrival
    HomeSection.TopRated -> R.string.home_section_top_rated
}

/** Home's search field sits at y 90 (one dp higher than on the other screens): logo bottom 72 + 18. */
private val HomeLogoToSearchGap = 18.dp

/** Search field bottom (y 140) to banner top (y 156). */
private val BannerTopGap = 16.dp
private val BottomContentGap = 24.dp

/** Home product card height in the design for a card with an old price (235; 211 without). */
internal val HomeCardHeight = 235.dp

/** Two tile rows: 146 (two-line label) + 16 gap + about 146. */
private val CategoriesRegionHeight = 308.dp

/** Vertical gap between a block and the next section title (y 200 -> 224, 544 -> 568 ...). */
internal val SectionGap = 24.dp
