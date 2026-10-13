package com.example.ecommercenativec19.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay

/**
 * Splash entry point. The prototype (interactions.json, node 1:2) navigates to Sign in AFTER_TIMEOUT 0.4s, so
 * [onFinished] is invoked once after [SPLASH_DELAY_MILLIS].
 */
@Composable
fun SplashRoute(
    onFinished: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val currentOnFinished by rememberUpdatedState(onFinished)
    LaunchedEffect(Unit) {
        delay(SPLASH_DELAY_MILLIS)
        currentOnFinished()
    }
    SplashScreen(modifier)
}

/** `AFTER_TIMEOUT` of the prototype reaction on Splash Screen (0.4 s). */
private const val SPLASH_DELAY_MILLIS = 400L
