package com.example.ecommercenativec19.feature.splash

import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.FigmaCroppedImage
import com.example.ecommercenativec19.core.ui.LightStatusBarIconsEffect
import com.example.ecommercenativec19.ui.theme.extendedColors

/**
 * Splash screen (Figma node 1:2): primary-color background, the "Route" logo image centered, and two blurred white
 * glows bleeding in from the top and bottom edges.
 *
 * Layout facts from the reference code: logo box 396 x 165 at x = 17 (so 17dp side margins and vertically
 * centered) showing a crop of a 2000 x 2000 image (height 240%, top -66.06%); each glow is a 730 x 757 blurred
 * ellipse (white, 40%, blur 75) centered horizontally, 373dp above the top edge and 383dp below the bottom edge.
 */
@Composable
fun SplashScreen(modifier: Modifier = Modifier) {
    LightStatusBarIconsEffect()
    Box(modifier = modifier.fillMaxSize().background(MaterialTheme.colorScheme.primary)) {
        SplashGlow(Modifier.align(Alignment.TopCenter).offset(y = -GlowOverflowTop))
        SplashGlow(Modifier.align(Alignment.BottomCenter).offset(y = GlowOverflowBottom))
        FigmaCroppedImage(
            painter = painterResource(R.drawable.img_24151328542545920446362786228006272036394703n1_91fa108e),
            contentDescription = stringResource(R.string.core_logo_description),
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxWidth()
                .padding(horizontal = LogoMargin)
                .aspectRatio(LogoWidth / LogoHeight),
            heightFraction = LogoHeightFraction,
            offsetYFraction = LogoOffsetYFraction,
        )
    }
}

/**
 * One blurred ellipse. The exported SVG ([R.drawable.ic_ellipse6_376b524d]) carries a Gaussian blur filter
 * (stdDeviation 75) that VectorDrawable cannot hold, so the blur is applied here. `Modifier.blur` needs
 * API 31; below that a radial fade approximating the same falloff is drawn instead (`extendedColors.splashGlow`).
 */
@Composable
private fun SplashGlow(modifier: Modifier = Modifier) {
    val glowColor = MaterialTheme.extendedColors.splashGlow
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
        Image(
            painter = painterResource(R.drawable.ic_ellipse6_376b524d),
            contentDescription = null,
            modifier = modifier
                .requiredSize(GlowWidth, GlowHeight)
                .blur(GlowBlur, BlurredEdgeTreatment.Unbounded),
        )
    } else {
        Box(
            modifier = modifier
                .requiredSize(GlowWidth, GlowHeight)
                .drawBehind {
                    drawRect(
                        brush = Brush.radialGradient(
                            0.00f to glowColor,
                            0.36f to glowColor.copy(alpha = glowColor.alpha * 0.84f),
                            0.55f to glowColor.copy(alpha = glowColor.alpha * 0.50f),
                            0.74f to glowColor.copy(alpha = glowColor.alpha * 0.16f),
                            0.92f to glowColor.copy(alpha = glowColor.alpha * 0.02f),
                            1.00f to Color.Transparent,
                            center = Offset(size.width / 2f, size.height / 2f),
                            radius = GlowFallbackRadius.toPx(),
                        ),
                    )
                },
        )
    }
}

private val GlowWidth = 730.dp
private val GlowHeight = 757.dp
private val GlowBlur = 75.dp

/** 730 x 757 glow minus the 430 x 457 node it is centered on: node top at -223 -> svg top at -373; bottom at +383. */
private val GlowOverflowTop = 373.dp
private val GlowOverflowBottom = 383.dp
private val GlowFallbackRadius = 400.dp

private val LogoMargin = 17.dp
private const val LogoWidth = 396f
private const val LogoHeight = 165f
private const val LogoHeightFraction = 2.4f
private const val LogoOffsetYFraction = -0.6606f
