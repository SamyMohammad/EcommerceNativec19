package com.example.ecommercenativec19.feature.productdetails

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.AppDimens
import com.example.ecommercenativec19.core.ui.textLineBox
import com.example.ecommercenativec19.ui.theme.generated.AppTokens
import com.example.ecommercenativec19.ui.theme.generated.LocalAppTokens

/**
 * Row of size choices: the selected size is a 35dp primary circle with white text, the others plain text.
 * Each choice is a radio target around the 35dp circle: 43dp wide (the design pitch, so the circles sit at x = 16, 59,
 * 102 ...) and 48dp tall.
 */
@Composable
internal fun ProductSizeOptions(
    sizes: List<String>,
    selectedSize: String,
    onSizeSelected: (String) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalAppTokens.current
    Row(modifier = modifier.padding(start = OptionsStartPadding)) {
        sizes.forEach { size ->
            val selected = size == selectedSize
            val description = stringResource(R.string.details_size_option, size)
            Box(
                modifier = Modifier
                    .size(width = OptionPitch, height = AppDimens.MinTouchTarget)
                    .semantics { contentDescription = description }
                    .selectable(selected = selected, role = Role.RadioButton, onClick = { onSizeSelected(size) }),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(OptionCircleSize)
                        .then(if (selected) Modifier.background(tokens.colorSource00, CircleShape) else Modifier),
                    contentAlignment = Alignment.Center,
                ) {
                    Text(
                        text = size,
                        style = tokens.typographySource07,
                        color = if (selected) tokens.colorSource02 else tokens.colorSource12,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.textLineBox(tokens.typographySource07),
                    )
                }
            }
        }
    }
}

/** Row of 35dp color swatches; the selected one shows a white check mark. */
@Composable
internal fun ProductColorOptions(
    colors: List<ProductColor>,
    selectedColor: ProductColor,
    onColorSelected: (ProductColor) -> Unit,
    modifier: Modifier = Modifier,
) {
    val tokens = LocalAppTokens.current
    Row(modifier = modifier.padding(start = OptionsStartPadding)) {
        colors.forEach { color ->
            val selected = color == selectedColor
            val description = stringResource(color.label())
            Box(
                modifier = Modifier
                    .size(width = OptionPitch, height = AppDimens.MinTouchTarget)
                    .semantics { contentDescription = description }
                    .selectable(selected = selected, role = Role.RadioButton, onClick = { onColorSelected(color) }),
                contentAlignment = Alignment.Center,
            ) {
                Box(
                    modifier = Modifier
                        .size(OptionCircleSize)
                        .background(color.swatch(tokens), CircleShape),
                    contentAlignment = Alignment.Center,
                ) {
                    if (selected) {
                        Image(
                            painter = painterResource(R.drawable.ic_right1_ebb623ae),
                            contentDescription = null,
                            modifier = Modifier.size(CheckSize),
                        )
                    }
                }
            }
        }
    }
}

private fun ProductColor.swatch(tokens: AppTokens): Color = when (this) {
    ProductColor.Black -> tokens.colorSource24
    ProductColor.Red -> tokens.colorSource25
    ProductColor.Blue -> tokens.colorSource26
    ProductColor.Green -> tokens.colorSource27
    ProductColor.Coral -> tokens.colorSource28
}

private fun ProductColor.label(): Int = when (this) {
    ProductColor.Black -> R.string.details_color_black
    ProductColor.Red -> R.string.details_color_red
    ProductColor.Blue -> R.string.details_color_blue
    ProductColor.Green -> R.string.details_color_green
    ProductColor.Coral -> R.string.details_color_coral
}

private val OptionCircleSize = 35.dp
private val CheckSize = 20.dp

/** Design pitch of the size and color circles (35dp circle + 8dp gap). */
private val OptionPitch = 43.dp

/** Keeps the first 35dp circle on the 16dp screen margin inside its target. */
private val OptionsStartPadding = AppDimens.ScreenHorizontal - (OptionPitch - OptionCircleSize) / 2
