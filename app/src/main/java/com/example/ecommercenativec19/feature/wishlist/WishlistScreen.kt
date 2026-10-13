package com.example.ecommercenativec19.feature.wishlist

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.AppButton
import com.example.ecommercenativec19.core.ui.AppButtonStyle
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.ui.FavoriteButton
import com.example.ecommercenativec19.core.ui.FigmaCropImage
import com.example.ecommercenativec19.core.ui.ProductRowCard
import com.example.ecommercenativec19.core.ui.SearchHeader
import com.example.ecommercenativec19.core.ui.tokenColor

/** Stateless Wishlist: search header, product rows with favorite heart and "Add to Cart", bottom bar. */
@Composable
fun WishlistScreen(
    state: WishlistUiState,
    onQueryChange: (String) -> Unit,
    onToggleFavorite: (String) -> Unit,
    onAddToCart: (String) -> Unit,
    onOpenCart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Scaffold(
        modifier = modifier,
        containerColor = MaterialTheme.colorScheme.background,
        contentWindowInsets = WindowInsets(0, 0, 0, 0),
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(bottom = RowGap),
            verticalArrangement = Arrangement.spacedBy(RowGap),
        ) {
            item {
                // The header carries its own 16dp margins and status-bar inset.
                SearchHeader(
                    query = state.query,
                    onQueryChange = onQueryChange,
                    onCartClick = onOpenCart,
                )
            }
            if (state.items.isEmpty()) {
                item {
                    // Deviation: the design shows no empty state; this is a minimal accessible one.
                    Box(Modifier.fillMaxWidth().padding(horizontal = AppDimens.ScreenHorizontal), contentAlignment = Alignment.Center) {
                        Text(
                            text = stringResource(R.string.wishlist_empty),
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )
                    }
                }
            }
            items(state.items, key = { it.id }) { item ->
                WishlistRow(item, onToggleFavorite, onAddToCart)
            }
        }
    }
}

@Composable
private fun WishlistRow(
    item: WishlistItemUi,
    onToggleFavorite: (String) -> Unit,
    onAddToCart: (String) -> Unit,
) {
    val addToCartDescription = stringResource(R.string.wishlist_add_to_cart_description, item.title)
    ProductRowCard(
        modifier = Modifier.padding(start = AppDimens.ScreenHorizontal, end = AppDimens.ScreenHorizontal, top = item.extraGapBefore),
        title = item.title,
        price = item.price,
        oldPrice = item.oldPrice,
        colorDot = item.dot.tokenColor(),
        variantColor = MaterialTheme.colorScheme.onSurface,
        variantLabel = item.variantLabel,
        image = {
            FigmaCropImage(painterResource(item.image), null, item.crop, Modifier.fillMaxSize())
        },
        // The design places the heart and the button 16dp from the end (the row's corner slot gives 8).
        topEnd = {
            FavoriteButton(
                isFavorite = item.isFavorite,
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
    )
}

private val RowGap = 24.dp
private val SlotExtraEnd = 8.dp
private val AddToCartWidth = 100.dp

