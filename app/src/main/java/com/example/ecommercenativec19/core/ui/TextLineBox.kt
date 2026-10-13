package com.example.ecommercenativec19.core.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle

/**
 * Gives a single-line `Text` the design's line box: exactly [TextStyle.lineHeight] tall (18sp for the Poppins styles)
 * with the glyphs centered in it, like the design tool's `line-height`. Where the text engine keeps the font's natural
 * line height (about 1.4 x the font size) the text would otherwise measure taller than the design's box and push
 * every following element down. The box scales with the font scale because the line height is in sp; the glyphs may
 * overflow it (they are not clipped). Use only for single-line text.
 */
@Composable
fun Modifier.textLineBox(style: TextStyle): Modifier {
    val density = LocalDensity.current
    val lineHeight = style.lineHeight
    if (!lineHeight.isSp) return this
    val box = remember(density, lineHeight) { with(density) { lineHeight.toDp() } }
    return this.layout { measurable, constraints ->
        val placeable = measurable.measure(constraints.copy(minHeight = 0, maxHeight = androidx.compose.ui.unit.Constraints.Infinity))
        val boxPx = box.roundToPx()
        layout(placeable.width, boxPx) {
            // Half-leading is floored (an odd difference puts the glyphs on the upper pixel, as the design tool does).
            placeable.placeRelative(0, Math.floorDiv(boxPx - placeable.height, 2))
        }
    }
}
