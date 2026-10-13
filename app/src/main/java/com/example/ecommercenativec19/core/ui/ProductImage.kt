package com.example.ecommercenativec19.core.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage

/**
 * A remote product photo, cropped to fill its bounds. A neutral block shows while loading, on failure and when
 * [url] is null, so a slow or broken image never leaves a hole in the card.
 */
@Composable
fun ProductImage(
    url: String?,
    contentDescription: String?,
    modifier: Modifier = Modifier,
) {
    val neutral = MaterialTheme.colorScheme.surfaceVariant
    val placeholder = remember(neutral) { ColorPainter(neutral) }
    AsyncImage(
        model = url,
        contentDescription = contentDescription,
        modifier = modifier,
        placeholder = placeholder,
        error = placeholder,
        fallback = placeholder,
        contentScale = ContentScale.Crop,
    )
}
