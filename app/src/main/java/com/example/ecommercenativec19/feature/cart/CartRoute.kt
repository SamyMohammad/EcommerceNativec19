package com.example.ecommercenativec19.feature.cart

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.fake.FakeData
import com.example.ecommercenativec19.fake.LocalFakeStore

/**
 * Cart entry point. [onBack] pops to the previous screen. The search icon, the cart icon (already on Cart) and
 * "Check Out" have no behavior: they are wired to no-ops here (payments are out of scope). Line prices and the total
 * are computed from the in-memory cart.
 */
@Composable
fun CartRoute(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val store = LocalFakeStore.current
    val lines = store.cart.mapNotNull { entry ->
        FakeData.product(entry.productId)?.let { product ->
            CartLineUi(
                id = product.id,
                title = product.title,
                imageRes = product.images.first(),
                linePrice = product.price * entry.quantity,
                quantity = entry.quantity,
            )
        }
    }
    CartScreen(
        state = CartUiState(
            items = if (lines.isEmpty()) LoadState.Empty else LoadState.Content(lines),
            totalPrice = lines.sumOf { it.linePrice },
        ),
        onBack = onBack,
        onIncrement = { store.changeQuantity(it, delta = 1) },
        onDecrement = { store.changeQuantity(it, delta = -1) },
        onRemove = store::removeFromCart,
        onSearch = {},
        onOpenCart = {},
        onCheckOut = {},
        onRetry = {},
        modifier = modifier,
    )
}
