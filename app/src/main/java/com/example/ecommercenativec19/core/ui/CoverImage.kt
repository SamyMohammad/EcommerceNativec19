package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.isSpecified
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import kotlin.math.max

/**
 * Cover-fit image (CSS `object-cover`, centered) like `ContentScale.Crop` + `Alignment.Center`, except that the
 * centering offset keeps its fractional part. Compose rounds that offset to whole pixels, which moves the image up
 * to half a pixel away from where the design (Figma / browser) places it whenever the scaled image and the box
 * differ by an odd number of pixels. [modifier] must give the box a bounded size.
 */
@Composable
fun CoverImage(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
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
                val intrinsic = painter.intrinsicSize
                if (!intrinsic.isSpecified || intrinsic.width <= 0f || intrinsic.height <= 0f) {
                    with(painter) { draw(size) }
                } else {
                    val scale = max(size.width / intrinsic.width, size.height / intrinsic.height)
                    val scaled = Size(intrinsic.width * scale, intrinsic.height * scale)
                    translate((size.width - scaled.width) / 2f, (size.height - scaled.height) / 2f) {
                        with(painter) { draw(scaled) }
                    }
                }
            },
    )
}

/**
 * Same placement as [FigmaCroppedImage] (the image is stretched to `widthFraction x heightFraction` of the box and
 * placed at (`offsetXFraction`, `offsetYFraction`) of the box size, clipped to the box) but without rounding the
 * size and offset to whole pixels, which is what the design (Figma / browser) does. [modifier] must give the box a
 * bounded size.
 */
@Composable
fun FractionalCropImage(
    painter: Painter,
    contentDescription: String?,
    modifier: Modifier = Modifier,
    widthFraction: Float = 1f,
    heightFraction: Float = 1f,
    offsetXFraction: Float = 0f,
    offsetYFraction: Float = 0f,
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
                translate(size.width * offsetXFraction, size.height * offsetYFraction) {
                    with(painter) { draw(Size(size.width * widthFraction, size.height * heightFraction)) }
                }
            },
    )
}
