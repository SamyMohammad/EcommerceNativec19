package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R

/**
 * Primary-colored 122 x 42 pill with minus, the quantity (Medium 18, white) and plus. Used by Product details and
 * Cart. Stateless: limits (min/max quantity) are the caller's decision; the design defines none.
 */
@Composable
fun QuantitySelector(
    quantity: Int,
    onIncrement: () -> Unit,
    onDecrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier
            .size(width = 122.dp, height = 42.dp)
            .clip(MaterialTheme.shapes.large)
            .background(MaterialTheme.colorScheme.primary)
            .padding(horizontal = EdgePadding),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        AppIconButton(
            painter = painterResource(R.drawable.ic_group_7ca8f3d9),
            contentDescription = stringResource(R.string.core_quantity_decrease),
            onClick = onDecrement,
            modifier = Modifier.fillMaxHeight(),
            iconSize = ControlIconSize,
        )
        Text(
            text = quantity.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.onPrimary,
            textAlign = TextAlign.Center,
            modifier = Modifier.textLineBox(MaterialTheme.typography.titleMedium),
        )
        AppIconButton(
            painter = painterResource(R.drawable.ic_icon_plus_circle_6a221117),
            contentDescription = stringResource(R.string.core_quantity_increase),
            onClick = onIncrement,
            modifier = Modifier.fillMaxHeight(),
            iconSize = ControlIconSize,
        )
    }
}

/** The 20dp minus/plus icon boxes (exported drawables are 22dp with stroke overflow). */
private val ControlIconSize = 22.dp

/** 48dp targets centered on the design's icon centers at x = 26 and x = 96 of the 122dp pill. */
private val EdgePadding = 2.dp
