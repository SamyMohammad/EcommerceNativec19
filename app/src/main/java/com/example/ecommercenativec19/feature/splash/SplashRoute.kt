package com.example.ecommercenativec19.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

/**
 * Splash entry point. The prototype (interactions.json, node 1:2) navigates to Sign in AFTER_TIMEOUT 0.4s; with a
 * stored session the app goes to Home instead. Exactly one of [onSignedIn] / [onSignedOut] is invoked, once, after
 * [SPLASH_DELAY_MILLIS] and after the stored token has loaded.
 */
@Composable
fun SplashRoute(
    onSignedIn: () -> Unit,
    onSignedOut: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: SplashViewModel = koinViewModel(),
) {
    val currentOnSignedIn by rememberUpdatedState(onSignedIn)
    val currentOnSignedOut by rememberUpdatedState(onSignedOut)
    LaunchedEffect(Unit) {
        delay(SPLASH_DELAY_MILLIS)
        when (viewModel.awaitDestination()) {
            SplashDestination.Home -> currentOnSignedIn()
            SplashDestination.SignIn -> currentOnSignedOut()
        }
    }
    SplashScreen(modifier)
}

/** `AFTER_TIMEOUT` of the prototype reaction on Splash Screen (0.4 s). */
private const val SPLASH_DELAY_MILLIS = 400L
