package com.example.ecommercenativec19.feature.cart

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

/**
 * Cart entry point. [onBack] leads to Home (prototype). The search icon, the cart icon (already on Cart) and
 * "Check Out" have no behavior in the prototype: they are wired to no-ops here (missing contracts).
 */
@Composable
fun CartRoute(onBack: () -> Unit, modifier: Modifier = Modifier) {
    val viewModel: CartViewModel = koinViewModel()
    val state by viewModel.state.collectAsStateWithLifecycle()
    CartScreen(
        state = state,
        onBack = onBack,
        onIncrement = viewModel::onIncrement,
        onDecrement = viewModel::onDecrement,
        onRemove = viewModel::onRemove,
        onSearch = {},
        onOpenCart = {},
        onCheckOut = {},
        modifier = modifier,
    )
}
