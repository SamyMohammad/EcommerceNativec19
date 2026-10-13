package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Image shape + label size of the two category tiles in the design. */
enum class CategoryTileStyle(internal val imageSize: Dp) {
    /** Home: 100dp circular photo, Regular 14 label (up to two lines). */
    Circle(100.dp),

    /** Categories: 70dp square photo, radius 5, 1dp outline, Regular 12 label. */
    Square(70.dp),
}

/**
 * Category image with its label underneath (8dp gap), centered. The tile is as wide as its image; long labels wrap
 * to two lines (ellipsized beyond that). [image] fills the image area (`Image(contentScale = Crop)` / [FigmaCropImage]).
 * Clickable (button role) only when [onClick] is non-null.
 *
 * A non-null [labelStart] start-aligns the label that far from the tile's start edge (the Categories frame places
 * each label by its left edge) instead of centering it.
 */
@Composable
fun CategoryTile(
    label: String,
    image: @Composable BoxScope.() -> Unit,
    modifier: Modifier = Modifier,
    style: CategoryTileStyle = CategoryTileStyle.Circle,
    onClick: (() -> Unit)? = null,
    labelStart: Dp? = null,
) {
    val imageShape = when (style) {
        CategoryTileStyle.Circle -> CircleShape
        CategoryTileStyle.Square -> MaterialTheme.shapes.extraSmall
    }
    val clickModifier = if (onClick != null) Modifier.clickable(role = Role.Button, onClick = onClick) else Modifier
    Column(
        modifier = modifier.width(style.imageSize).then(clickModifier),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .size(style.imageSize)
                .clip(imageShape)
                .then(
                    if (style == CategoryTileStyle.Square) {
                        Modifier.border(1.dp, MaterialTheme.colorScheme.outline, imageShape)
                    } else {
                        Modifier
                    },
                ),
            content = image,
        )
        Spacer(Modifier.height(8.dp))
        val labelStyle = if (style == CategoryTileStyle.Circle) MaterialTheme.typography.bodyMedium else MaterialTheme.typography.bodySmall
        if (labelStart != null) {
            LineBoxText(
                text = label,
                style = labelStyle,
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth().padding(start = labelStart),
            )
        } else {
            // API names are single strings, so the label wraps on its own instead of using explicit line breaks.
            Text(
                text = label,
                style = labelStyle,
                color = MaterialTheme.colorScheme.onSurface,
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}
