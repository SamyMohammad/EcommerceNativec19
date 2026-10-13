package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.Image
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Constraints
import androidx.compose.runtime.Composable
import kotlin.math.roundToInt

/**
 * Reproduces a Figma image fill with a crop transform (the `h-[240%] top-[-66.06%]` pattern in the reference code).
 *
 * The image is stretched to `widthFraction x heightFraction` of this box, placed at
 * (`offsetXFraction`, `offsetYFraction`) of the box size, and everything outside the box is clipped. The box needs
 * a bounded size (give it `Modifier.size(...)`, `fillMaxWidth().aspectRatio(...)` ...). Apply any rounded
 * `clip(...)` to [modifier] before/after as the design shows.
 *
 * Example: `h-[240%] left-0 top-[-66.06%] w-full` -> `heightFraction = 2.4f, offsetYFraction = -0.6606f`.
 */
@Composable
fun FigmaCroppedImage(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    widthFraction: Float = 1f,
    heightFraction: Float = 1f,
    offsetXFraction: Float = 0f,
    offsetYFraction: Float = 0f,
) {
    Layout(
        content = {
            Image(
                painter = painter,
                contentDescription = contentDescription,
                contentScale = ContentScale.FillBounds,
            )
        },
        modifier = modifier.clipToBounds(),
    ) { measurables, constraints ->
        val boxWidth = constraints.maxWidth
        val boxHeight = constraints.maxHeight
        val placeable = measurables.first().measure(
            Constraints.fixed(
                (boxWidth * widthFraction).roundToInt(),
                (boxHeight * heightFraction).roundToInt(),
            ),
        )
        layout(boxWidth, boxHeight) {
            placeable.place(
                (boxWidth * offsetXFraction).roundToInt(),
                (boxHeight * offsetYFraction).roundToInt(),
            )
        }
    }
}
