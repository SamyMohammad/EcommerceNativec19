package com.example.ecommercenativec19.feature.categories

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.selected
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.core.ui.LineBoxText
import com.example.ecommercenativec19.core.ui.textLineBox
import com.example.ecommercenativec19.ui.theme.generated.LocalAppTokens

/**
 * Left category rail. Entries are display-only: the prototype defines no reaction for them (the "Women's Fashion"
 * entry leads to a screen outside the ten implemented ones), and the design only has content for the selected
 * entry, so no rail entry is clickable.
 */
@Composable
internal fun CategoriesRail(
    categories: List<RailCategory>,
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
    Column(
        modifier = modifier
            .clip(shape)
            .background(tokens.colorSource22)
            .border(1.dp, tokens.colorSource14, shape)
            .verticalScroll(rememberScrollState()),
    ) {
        categories.forEach { category ->
            when {
                category.label == null -> Spacer(Modifier.height(RowHeight))
                category.isSelected -> SelectedRailItem(stringResource(category.label))
                else -> RailItem(stringResource(category.label))
            }
        }
    }
}

@Composable
private fun SelectedRailItem(label: String) {
    val tokens = LocalAppTokens.current
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .heightIn(min = SelectedRowHeight)
            .background(tokens.colorSource02)
            .semantics { selected = true },
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
private fun RailItem(label: String) {
    val tokens = LocalAppTokens.current
    LineBoxText(
        text = label,
        style = tokens.typographySource15,
        color = tokens.colorSource12,
        modifier = Modifier
            .fillMaxWidth()
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
private val RowHeight = 66.dp
