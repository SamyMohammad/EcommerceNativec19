package com.example.ecommercenativec19.core.ui

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource

/**
 * A bundled product photo, cropped to fill its bounds. A neutral block shows when [imageRes] is null, so a product
 * without a picture never leaves a hole in the card.
 */
@Composable
fun ProductImage(
    @DrawableRes imageRes: Int?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    if (imageRes == null) {
        Box(modifier.background(MaterialTheme.colorScheme.surfaceVariant))
    } else {
        Image(
            painter = painterResource(imageRes),
            contentDescription = contentDescription,
            modifier = modifier,
            contentScale = ContentScale.Crop,
        )
    }
}
