package com.example.ecommercenativec19.core.ui

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

/**
 * Makes the status bar icons light (white) while this composable is in the composition. For full-bleed dark
 * (primary color) screens: Splash, Sign in, Sign up.
 *
 * Screens cross-fade, so the outgoing and incoming screen are composed at the same time. The effect is therefore
 * reference counted: the original appearance is captured when the first screen asks for light icons and restored
 * only when the last one leaves.
 */
@Composable
fun LightStatusBarIconsEffect() {
    val view = LocalView.current
    if (view.isInEditMode) return
    DisposableEffect(view) {
        val window = view.context.findActivity()?.window
        val controller = window?.let { WindowCompat.getInsetsController(it, view) }
        if (controller != null) {
            if (activeCount == 0) originalLightStatusBars = controller.isAppearanceLightStatusBars
            activeCount++
            controller.isAppearanceLightStatusBars = false
        }
        onDispose {
            if (controller != null) {
                activeCount--
                if (activeCount == 0) controller.isAppearanceLightStatusBars = originalLightStatusBars
            }
        }
    }
}

// Composition runs on the main thread, so plain fields are enough.
private var activeCount = 0
private var originalLightStatusBars = true

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}
