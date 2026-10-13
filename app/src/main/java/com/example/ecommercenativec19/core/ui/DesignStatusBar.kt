package com.example.ecommercenativec19.core.ui

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.max

/**
 * Reserves the top of the screen: at least the design's 44dp status bar band (every frame has its content start at
 * y = 44 or lower), or the real status bar inset when that is taller. On devices with a short status bar the content
 * therefore sits where the design puts it; with a taller inset it never overlaps the system bar.
 */
@Composable
fun Modifier.designStatusBarPadding(): Modifier =
    padding(top = max(WindowInsets.statusBars.asPaddingValues().calculateTopPadding(), AppDimens.StatusBarDesignHeight))
