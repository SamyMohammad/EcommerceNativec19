package com.example.ecommercenativec19.feature.cart

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.AppButton
import com.example.ecommercenativec19.core.common.UiState
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.ui.ProductImage
import com.example.ecommercenativec19.core.ui.ProductRowCard
import com.example.ecommercenativec19.core.ui.QuantitySelector
import com.example.ecommercenativec19.core.ui.ScreenTitleBar
import com.example.ecommercenativec19.core.ui.StatusPane
import com.example.ecommercenativec19.core.ui.formatPrice
import com.example.ecommercenativec19.core.ui.textLineBox

/** Stateless Cart: title bar, product rows, and the footer with the server's total price and "Check Out". */
@Composable
fun CartScreen(
    state: CartUiState,
    onBack: () -> Unit,
    onIncrement: (String) -> Unit,
    onDecrement: (String) -> Unit,
    onRemove: (String) -> Unit,
    onSearch: () -> Unit,
    onOpenCart: () -> Unit,
    onCheckOut: () -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier.fillMaxSize()) {
        ScreenTitleBar(
            title = stringResource(R.string.cart_title),
            onBackClick = onBack,
            onSearchClick = onSearch,
            onCartClick = onOpenCart,
        )
        Spacer(Modifier.height(ListTopGap))
        StatusPane(
            state = state.items,
            onRetry = onRetry,
            emptyText = stringResource(R.string.cart_empty),
            modifier = Modifier.weight(1f).fillMaxWidth(),
        ) { items ->
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = AppDimens.ScreenHorizontal),
                verticalArrangement = Arrangement.spacedBy(RowGap),
            ) {
                items(items, key = { it.id }) { item ->
                    CartRow(item, onIncrement, onDecrement, onRemove)
                }
            }
        }
        if (state.items is UiState.Success) {
            CartFooter(state.totalPrice, onCheckOut)
        }
    }
}

@Composable
private fun CartRow(
    item: CartLineUi,
    onIncrement: (String) -> Unit,
    onDecrement: (String) -> Unit,
    onRemove: (String) -> Unit,
) {
    ProductRowCard(
        title = item.title,
        price = formatPrice(item.linePrice),
        titleTop = 16.dp,
        variantTop = 47.dp,
        priceTop = 81.dp,
        image = { ProductImage(url = item.imageUrl, contentDescription = null, modifier = Modifier.fillMaxSize()) },
        topEnd = {
            // 48dp target whose icon stays at the design position (top-end corner); the target grows inward.
            Box(
                modifier = Modifier
                    .size(AppDimens.MinTouchTarget)
                    .clickable(role = Role.Button) { onRemove(item.id) },
                contentAlignment = Alignment.TopEnd,
            ) {
                Image(
                    painter = painterResource(R.drawable.ic_delete1_8c0124d1),
                    contentDescription = stringResource(R.string.cart_delete_item, item.title),
                    modifier = Modifier.size(AppDimens.IconSize),
                )
            }
        },
        bottomEnd = {
            QuantitySelector(
                quantity = item.quantity,
                onIncrement = { onIncrement(item.id) },
                onDecrement = { onDecrement(item.id) },
            )
        },
    )
}

@Composable
private fun CartFooter(totalPrice: Int, onCheckOut: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = AppDimens.ScreenHorizontal)
            .padding(top = RowGap, bottom = FooterBottomMargin),
        horizontalArrangement = Arrangement.spacedBy(FooterGap),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Column(verticalArrangement = Arrangement.spacedBy(TotalGap)) {
            Text(
                text = stringResource(R.string.cart_total_price),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.textLineBox(MaterialTheme.typography.titleMedium),
            )
            Text(
                text = formatPrice(totalPrice),
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.textLineBox(MaterialTheme.typography.titleMedium),
            )
        }
        Spacer(Modifier.weight(1f))
        AppButton(
            text = stringResource(R.string.cart_check_out),
            onClick = onCheckOut,
            trailingIcon = painterResource(R.drawable.ic_arrow61_f7fe0b37),
            modifier = Modifier.widthIn(max = CheckOutMaxWidth),
        )
    }
}

/** Title bar bottom (y 44 + 48 = 92) to the first row (y 106) in the design. */
private val ListTopGap = 14.dp

/** Gap between product rows (and above the footer) used by the design: 24dp. */
private val RowGap = 24.dp

/** "Total price" label top (y 786) to the price top (y 816), minus the 18dp line. */
private val TotalGap = 12.dp
private val FooterGap = 16.dp

/** Check Out button is 270 wide in the design; it may shrink on narrow screens. */
private val CheckOutMaxWidth = 270.dp

/** Button bottom (y 834) to the frame bottom (932). */
private val FooterBottomMargin = 98.dp
