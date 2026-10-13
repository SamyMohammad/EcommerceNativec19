package com.example.ecommercenativec19.feature.productlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.core.ui.CoverImage
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.ui.ProductCard
import com.example.ecommercenativec19.core.ui.SearchHeader

/** Stateless Product list: search header, two-column grid of [ProductCard]s. */
@Composable
fun ProductListScreen(
    state: ProductListUiState,
    onQueryChange: (String) -> Unit,
    onCartClick: () -> Unit,
    onProductClick: (String) -> Unit,
    onFavoriteClick: (String) -> Unit,
    onAddClick: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { innerPadding ->
        Column(Modifier.fillMaxSize().padding(innerPadding)) {
            SearchHeader(query = state.query, onQueryChange = onQueryChange, onCartClick = onCartClick)
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
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
                items(state.products, key = { it.id }) { product ->
                    ProductCard(
                        title = product.title,
                        price = product.price,
                        oldPrice = product.oldPrice,
                        rating = product.rating,
                        isFavorite = product.isFavorite,
                        onFavoriteClick = { onFavoriteClick(product.id) },
                        onAddClick = { onAddClick(product.id) },
                        onClick = { onProductClick(product.id) },
                        // Design card height is 237; used as a minimum so larger fonts can grow the card.
                        modifier = Modifier.heightIn(min = CardMinHeight),
                        image = {
                            CoverImage(painterResource(product.imageRes), null, Modifier.fillMaxSize())
                        },
                    )
                }
            }
        }
    }
}

/** Search header bottom (y 141) to first card (y 165). */
private val GridTopGap = 24.dp

/** Gap between cards, both directions (x 207 -> 223, y 402 -> 418). */
private val GridGap = 16.dp
private val CardMinHeight = 237.dp
