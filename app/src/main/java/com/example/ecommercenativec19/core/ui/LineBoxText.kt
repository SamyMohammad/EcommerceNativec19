package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow

/**
 * Text whose explicit `\n`-separated lines are each laid out in the design's line box (see [textLineBox]), so
 * stacked lines are exactly `lineHeight` apart. Every line is a single line (no wrapping, ellipsized when too wide).
 */
@Composable
fun LineBoxText(
    text: String,
    style: TextStyle,
    color: Color,
    modifier: Modifier = Modifier,
    textAlign: TextAlign = TextAlign.Start,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = if (textAlign == TextAlign.Center) Alignment.CenterHorizontally else Alignment.Start,
    ) {
        text.split('\n').forEach { line ->
            Text(
                text = line,
                style = style,
                color = color,
                textAlign = textAlign,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.textLineBox(style),
            )
        }
    }
}
