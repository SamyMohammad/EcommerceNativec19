package com.example.ecommercenativec19.feature.productdetails

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.fake.FakeData
import com.example.ecommercenativec19.fake.LocalFakeStore

/**
 * Product details entry point. Back -> [onBack]; cart icon -> [onOpenCart]; "Add to cart" adds the selected quantity
 * to the in-memory cart, then calls [onAddToCart] (the prototype's transition to Cart).
 */
@Composable
fun ProductDetailsRoute(
    productId: String,
    onBack: () -> Unit,
    onOpenCart: () -> Unit,
    onAddToCart: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val store = LocalFakeStore.current
    var quantity by remember(productId) { mutableIntStateOf(1) }
    val product = remember(productId) {
        FakeData.product(productId)?.let {
            ProductDetailsUi(
                id = it.id,
                title = it.title,
                unitPrice = it.price,
                soldCount = it.soldCount,
                rating = it.rating,
                ratingCount = it.ratingCount,
                description = it.description,
                images = it.images,
            )
        }
    }
    ProductDetailsScreen(
        state = ProductDetailsUiState(
            product = product?.let { LoadState.Content(it) } ?: LoadState.Empty,
            quantity = quantity,
            isFavorite = productId in store.favoriteIds,
        ),
        onBack = onBack,
        onCartClick = onOpenCart,
        onAddToCart = {
            store.addToCart(productId, quantity)
            onAddToCart()
        },
        onFavoriteClick = { store.toggleFavorite(productId) },
        onIncrementQuantity = { quantity++ },
        // The design shows no limits; the quantity just never drops below 1 so the pill cannot show 0 or negatives.
        onDecrementQuantity = { quantity = (quantity - 1).coerceAtLeast(1) },
        onRetry = {},
        modifier = modifier,
    )
}
