package com.example.ecommercenativec19.feature.cart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

/**
 * Cart entry point. [onBack] pops to the previous screen. The search icon, the cart icon (already on Cart) and
 * "Check Out" have no behavior: they are wired to no-ops here (no search or checkout contract; payments are out of scope).
 */
@Composable
fun CartRoute(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val viewModel: CartViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    // Runs on every visit, not just when the ViewModel is created: the cart may have changed since the last one.
    LaunchedEffect(viewModel) { viewModel.onEnter() }
    CartScreen(
        state = state,
        onBack = onBack,
        onIncrement = viewModel::onIncrement,
        onDecrement = viewModel::onDecrement,
        onRemove = viewModel::onRemove,
        onSearch = {},
        onOpenCart = {},
        onCheckOut = {},
        onRetry = viewModel::onRetry,
        modifier = modifier,
    )
}
