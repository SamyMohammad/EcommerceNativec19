package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.Constraints
import kotlin.math.roundToInt

/**
 * Figma image-fill crop transform (the `h-[240%] left-0 top-[-66.06%] w-full` pattern in the reference code); all
 * values are fractions of the target box. Example: that pattern is `heightFraction = 2.4f, offsetYFraction = -0.6606f`.
 */
data class FigmaImageCrop(
    val widthFraction: Float = 1f,
    val heightFraction: Float = 1f,
    val offsetXFraction: Float = 0f,
    val offsetYFraction: Float = 0f,
)

/**
 * Draws [painter] either as a plain cover-fit image ([crop] == null, `object-cover` in the reference code) or with
 * the design's crop transform: stretched to `widthFraction x heightFraction` of the box, placed at
 * (`offsetXFraction`, `offsetYFraction`) of the box size and clipped to the box. [modifier] must give the box a
 * bounded size (`Modifier.size(...)`, `fillMaxWidth().aspectRatio(...)` ...).
 *
 * By default placement is fractional, like the design (browser) does. [snapToPixels] rounds the stretched size and
 * the offset to whole pixels instead; only the splash logo is captured that way.
 */
@Composable
fun FigmaCropImage(
    painter: Painter,
    contentDescription: String?,
    crop: FigmaImageCrop?,
    modifier: Modifier = Modifier,
    snapToPixels: Boolean = false,
) {
    when {
        crop == null -> CoverImage(painter, contentDescription, modifier)
        snapToPixels -> PixelSnappedCrop(painter, contentDescription, crop, modifier)
        else -> FractionalCrop(painter, contentDescription, crop, modifier)
    }
}

@Composable
private fun FractionalCrop(
    painter: Painter,
    contentDescription: String?,
    crop: FigmaImageCrop,
    modifier: Modifier,
) {
    val semanticsModifier = if (contentDescription != null) {
        Modifier.semantics {
            this.contentDescription = contentDescription
            role = Role.Image
        }
    } else {
        Modifier
    }
    Spacer(
        modifier
            .then(semanticsModifier)
            .clipToBounds()
            .drawBehind {
                translate(size.width * crop.offsetXFraction, size.height * crop.offsetYFraction) {
                    with(painter) { draw(Size(size.width * crop.widthFraction, size.height * crop.heightFraction)) }
                }
            },
    )
}

@Composable
private fun PixelSnappedCrop(
    painter: Painter,
    contentDescription: String?,
    crop: FigmaImageCrop,
    modifier: Modifier,
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
                (boxWidth * crop.widthFraction).roundToInt(),
                (boxHeight * crop.heightFraction).roundToInt(),
            ),
        )
        layout(boxWidth, boxHeight) {
            placeable.place(
                (boxWidth * crop.offsetXFraction).roundToInt(),
                (boxHeight * crop.offsetYFraction).roundToInt(),
            )
        }
    }
}
