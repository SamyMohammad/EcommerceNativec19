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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.example.ecommercenativec19.ui.theme.generated.LocalAppTokens

/** Left category rail: the API's categories, or a spinner / retry / empty note while they are not available. */
@Composable
internal fun CategoriesRail(
    state: LoadState<List<RailCategory>>,
    onCategorySelected: (String) -> Unit,
    onRetry: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalAppTokens.current
    // Rounded top-start / bottom corners, square top-end (as in the design).
    val shape = RoundedCornerShape(
        topStart = tokens.radiusSource05,
        topEnd = tokens.radiusSource01,
        bottomEnd = tokens.radiusSource05,
        bottomStart = tokens.radiusSource05,
    )
    StatusPane(
        state = state,
        onRetry = onRetry,
        modifier = modifier
            .clip(shape)
            .background(tokens.colorSource22)
            .border(1.dp, tokens.colorSource14, shape),
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
    val tokens = LocalAppTokens.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = SelectedRowHeight)
            .background(tokens.colorSource02)
            .selectable(selected = true, role = Role.Tab, onClick = onClick),
        contentAlignment = Alignment.CenterStart,
    ) {
        Box(
            modifier = Modifier
                .padding(start = IndicatorStart)
                .size(width = IndicatorWidth, height = IndicatorHeight)
                .background(tokens.colorSource00, RoundedCornerShape(tokens.radiusSource00)),
        )
        Text(
            text = label,
            style = tokens.typographySource15,
            color = tokens.colorSource12,
            modifier = Modifier.padding(start = SelectedTextStart, end = ItemEnd).textLineBox(tokens.typographySource15),
        )
    }
}

@Composable
private fun RailItem(label: String, onClick: () -> Unit) {
    val tokens = LocalAppTokens.current
    LineBoxText(
        text = label,
        style = tokens.typographySource15,
        color = tokens.colorSource12,
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
