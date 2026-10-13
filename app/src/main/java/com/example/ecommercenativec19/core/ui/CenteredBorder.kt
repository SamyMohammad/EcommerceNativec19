package com.example.ecommercenativec19.core.ui

import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawOutline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp

/**
 * Border whose stroke is centered on the box edge (Figma's "center" stroke alignment): half of it lies outside the
 * box. `Modifier.border` draws the whole stroke inside, which is right for CSS borders but moves a centered Figma
 * stroke by half its width. Nothing is added to the layout size.
 */
fun Modifier.centeredBorder(width: Dp, color: Color, shape: Shape): Modifier = drawBehind {
    val outline = shape.createOutline(size, layoutDirection, this)
    drawOutline(outline, color, style = Stroke(width = width.toPx()))
}
