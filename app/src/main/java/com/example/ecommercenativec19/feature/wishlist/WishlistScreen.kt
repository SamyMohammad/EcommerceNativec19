package com.example.ecommercenativec19.feature.wishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.AppButton
import com.example.ecommercenativec19.core.ui.AppButtonStyle
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.ui.FavoriteButton
import com.example.ecommercenativec19.core.ui.ProductImage
import com.example.ecommercenativec19.core.ui.ProductRowCard
import com.example.ecommercenativec19.core.ui.SearchHeader
import com.example.ecommercenativec19.core.ui.StatusPane
import com.example.ecommercenativec19.core.ui.formatOldPrice
import com.example.ecommercenativec19.core.ui.formatPrice
import com.example.ecommercenativec19.feature.products.ui.ProductCardUi

/** Stateless Wishlist: search header, product rows with favorite heart and "Add to Cart". */
@Composable
fun WishlistScreen(
    state: WishlistUiState,
    onQueryChange: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onProductClick: (String) -> Unit,
    onAddToCart: (String) -> Unit,
    onOpenCart: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            // The header carries its own 16dp margins and status-bar inset.
            SearchHeader(query = state.query, onQueryChange = onQueryChange, onCartClick = onOpenCart)
            StatusPane(
                state = state.items,
                onRetry = onRetry,
                emptyText = stringResource(R.string.wishlist_empty),
                modifier = Modifier.weight(1f).fillMaxWidth(),
            ) { items ->
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(top = RowGap, bottom = RowGap),
                    verticalArrangement = Arrangement.spacedBy(RowGap),
                ) {
                    items(items, key = { it.id }) { item ->
                        WishlistRow(item, onToggleFavorite, onProductClick, onAddToCart)
                    }
                }
            }
        }
    }
}

@Composable
private fun WishlistRow(
    item: ProductCardUi,
    onToggleFavorite: (String) -> Unit,
    onProductClick: (String) -> Unit,
    onAddToCart: (String) -> Unit,
) {
    val addToCartDescription = stringResource(R.string.wishlist_add_to_cart_description, item.title)
    ProductRowCard(
        modifier = Modifier.padding(horizontal = AppDimens.ScreenHorizontal),
        title = item.title,
        price = formatPrice(item.price),
        oldPrice = item.oldPrice?.let { formatOldPrice(it) },
        image = { ProductImage(imageRes = item.imageRes, contentDescription = null, modifier = Modifier.fillMaxSize()) },
        // The design places the heart and the button 16dp from the end (the row's corner slot gives 8).
        topEnd = {
            FavoriteButton(
                isFavorite = true,
                onClick = { onToggleFavorite(item.id) },
                modifier = Modifier.padding(end = SlotExtraEnd),
            )
        },
        bottomEnd = {
            AppButton(
                text = stringResource(R.string.wishlist_add_to_cart),
                onClick = { onAddToCart(item.id) },
                style = AppButtonStyle.PrimaryCompact,
                modifier = Modifier
                    .padding(end = SlotExtraEnd)
                    .width(AddToCartWidth)
                    .semantics { contentDescription = addToCartDescription },
            )
        },
        onClick = { onProductClick(item.id) },
    )
}

private val RowGap = 24.dp
private val SlotExtraEnd = 8.dp
private val AddToCartWidth = 100.dp
