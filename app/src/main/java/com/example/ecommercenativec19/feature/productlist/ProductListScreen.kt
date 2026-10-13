package com.example.ecommercenativec19.feature.productlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.AppButton
import com.example.ecommercenativec19.core.ui.AppButtonStyle
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.ui.ProductCard
import com.example.ecommercenativec19.core.ui.ProductImage
import com.example.ecommercenativec19.core.ui.SearchHeader
import com.example.ecommercenativec19.core.ui.StatusPane
import com.example.ecommercenativec19.core.ui.formatOldPrice
import com.example.ecommercenativec19.core.ui.formatPrice
import com.example.ecommercenativec19.core.ui.formatRating
import com.example.ecommercenativec19.feature.products.ui.ProductCardUi

/** Stateless Product list: search header, two-column grid of [ProductCard]s. */
@Composable
fun ProductListScreen(
    state: ProductListUiState,
    onQueryChange: (String) -> Unit,
    onCartClick: () -> Unit,
    onProductClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onAddClick: (String) -> Unit,
    onRetry: () -> Unit,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding)) {
            SearchHeader(query = state.query, onQueryChange = onQueryChange, onCartClick = onCartClick)
            StatusPane(
                state = state.products,
                onRetry = onRetry,
                emptyText = stringResource(R.string.productlist_empty),
                modifier = Modifier.weight(1f).fillMaxWidth(),
            ) { products ->
                ProductGrid(
                    products = products,
                    favoriteIds = state.favoriteIds,
                    loadMore = state.loadMore,
                    onProductClick = onProductClick,
                    onFavoriteClick = onFavoriteClick,
                    onAddClick = onAddClick,
                    onLoadMore = onLoadMore,
                )
            }
        }
    }
}

@Composable
private fun ProductGrid(
    products: List<ProductCardUi>,
    favoriteIds: Set<String>,
    loadMore: LoadMoreState,
    onProductClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onAddClick: (String) -> Unit,
    onLoadMore: () -> Unit,
) {
    val gridState = rememberLazyGridState()
    val nearEnd by remember {
        derivedStateOf {
            val info = gridState.layoutInfo
            val lastVisible = info.visibleItemsInfo.lastOrNull()?.index ?: -1
            info.totalItemsCount > 0 && lastVisible >= info.totalItemsCount - 1 - LoadMoreLookahead
        }
    }
    // Keyed by the item count too: after a page arrives the list may still be near its end and needs another page.
    // A failed page changes neither key, so it is not retried in a loop; the footer's button retries.
    LaunchedEffect(nearEnd, products.size) {
        if (nearEnd) onLoadMore()
    }
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        state = gridState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = AppDimens.ScreenHorizontal,
            end = AppDimens.ScreenHorizontal,
            top = GridTopGap,
            bottom = GridGap,
        ),
        horizontalArrangement = Arrangement.spacedBy(GridGap),
        verticalArrangement = Arrangement.spacedBy(GridGap),
    ) {
        items(products, key = { it.id }, contentType = { "product" }) { product ->
            ProductCard(
                title = product.title,
                price = formatPrice(product.price),
                oldPrice = product.oldPrice?.let { formatOldPrice(it) },
                rating = formatRating(product.rating),
                isFavorite = product.id in favoriteIds,
                onFavoriteClick = { onFavoriteClick(product.id) },
                onAddClick = { onAddClick(product.id) },
                onClick = { onProductClick(product.id) },
                // Design card height is 237; used as a minimum so larger fonts can grow the card.
                modifier = Modifier.heightIn(min = CardMinHeight),
                image = { ProductImage(url = product.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize()) },
            )
        }
        if (loadMore != LoadMoreState.Idle) {
            item(key = "load-more", span = { GridItemSpan(maxLineSpan) }, contentType = "load-more") {
                LoadMoreFooter(loadMore, onRetry = onLoadMore)
            }
        }
    }
}

@Composable
private fun LoadMoreFooter(state: LoadMoreState, onRetry: () -> Unit) {
    Box(Modifier.fillMaxWidth().padding(vertical = GridGap), contentAlignment = Alignment.Center) {
        when (state) {
            LoadMoreState.Loading -> CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            LoadMoreState.Error -> AppButton(
                text = stringResource(R.string.core_retry),
                onClick = onRetry,
                style = AppButtonStyle.PrimarySmall,
            )
            LoadMoreState.Idle -> Unit
        }
    }
}

/** Search header bottom (y 141) to first card (y 165). */
private val GridTopGap = 24.dp

/** Gap between cards, both directions (x 207 -> 223, y 402 -> 418). */
private val GridGap = 16.dp
private val CardMinHeight = 237.dp

/** Start loading the next page when this many items (about three rows) remain below the viewport. */
private const val LoadMoreLookahead = 5
