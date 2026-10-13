package com.example.ecommercenativec19.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource

/** Figma image-fill crop transform (see [FigmaCroppedImage]); all values are fractions of the target box. */
data class FigmaImageCrop(
    val widthFraction: Float = 1f,
    val heightFraction: Float = 1f,
    val offsetXFraction: Float = 0f,
    val offsetYFraction: Float = 0f,
)

/**
 * Draws a drawable either as a plain cover-fit image ([crop] == null, `object-cover` in the reference code) or with
 * the design's crop transform. [modifier] must give the box a bounded size.
 */
@Composable
fun FigmaCropImage(
    @DrawableRes image: Int,
    crop: FigmaImageCrop?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null,
) {
    // Fractional placement, like the design (browser) does: Compose would round the offsets to whole pixels.
    if (crop == null) {
        CoverImage(painter = painterResource(image), contentDescription = contentDescription, modifier = modifier)
    } else {
        FractionalCropImage(
            painter = painterResource(image),
            contentDescription = contentDescription,
            modifier = modifier,
            widthFraction = crop.widthFraction,
            heightFraction = crop.heightFraction,
            offsetXFraction = crop.offsetXFraction,
            offsetYFraction = crop.offsetYFraction,
        )
    }
}
