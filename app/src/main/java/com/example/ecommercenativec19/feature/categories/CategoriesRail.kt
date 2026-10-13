package com.example.ecommercenativec19.feature.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.ZeroCornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.core.ui.LineBoxText
import com.example.ecommercenativec19.core.ui.textLineBox

/** Left category rail: the API's categories, or a spinner / retry / empty note while they are not available. */
@Composable
internal fun CategoriesRail(
    state: LoadState<List<RailCategory>>,
    onCategorySelected: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    // Rounded top-start / bottom corners, square top-end (as in the design).
    val shape = MaterialTheme.shapes.small.copy(topEnd = ZeroCornerSize)
    StatusPane(
        state = state,
        onRetry = onRetry,
        modifier = modifier
            .clip(shape)
            .background(MaterialTheme.colorScheme.surfaceVariant)
            .border(1.dp, MaterialTheme.colorScheme.outline, shape),
    ) { categories ->
        Column(modifier = Modifier.verticalScroll(rememberScrollState()).selectableGroup()) {
            categories.forEach { category ->
                if (category.isSelected) {
                    SelectedRailItem(category.label, onClick = { onCategorySelected(category.id) })
                } else {
                    RailItem(category.label, onClick = { onCategorySelected(category.id) })
                }
            }
        }
    }
}

@Composable
private fun SelectedRailItem(label: String, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = SelectedRowHeight)
            .background(MaterialTheme.colorScheme.surface)
            .selectable(selected = true, role = Role.Tab, onClick = onClick),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .padding(start = IndicatorStart)
                .size(width = IndicatorWidth, height = IndicatorHeight)
                .background(MaterialTheme.colorScheme.primary, MaterialTheme.shapes.large),
        )
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurface,
            modifier = Modifier.padding(start = SelectedTextStart, end = ItemEnd).textLineBox(MaterialTheme.typography.labelLarge),
        )
    }
}

@Composable
private fun RailItem(label: String, onClick: () -> Unit) {
    LineBoxText(
        text = label,
        style = MaterialTheme.typography.labelLarge,
        color = MaterialTheme.colorScheme.onSurface,
        modifier = Modifier
            .fillMaxWidth()
            .selectable(selected = false, role = Role.Tab, onClick = onClick)
            .padding(start = ItemStart, end = ItemEnd, top = ItemTop, bottom = ItemBottom),
    )
}

/** Selected entry: white 82dp row with a 7 x 72 primary indicator at x 5 and text at x 17. */
private val SelectedRowHeight = 82.dp
private val IndicatorStart = 5.dp
private val IndicatorWidth = 7.dp
private val IndicatorHeight = 72.dp
private val SelectedTextStart = 17.dp

/** Other entries: text 7dp from the start, 16dp below the row top, rows 66dp apart (16 + 18 line + 32). */
private val ItemStart = 7.dp
private val ItemEnd = 4.dp
private val ItemTop = 16.dp
private val ItemBottom = 32.dp
