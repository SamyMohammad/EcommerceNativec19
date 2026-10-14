package com.example.ecommercenativec19.feature.splash

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import com.example.ecommercenativec19.fake.LocalFakeStore
import kotlinx.coroutines.delay

/**
 * Splash entry point. The prototype (interactions.json, node 1:2) navigates to Sign in AFTER_TIMEOUT 0.4s; when the
 * in-memory store is already signed in the app goes to Home instead. Exactly one of [onSignedIn] / [onSignedOut] is
 * invoked, once, after [SPLASH_DELAY_MILLIS].
 */
@Composable
fun SplashRoute(
    onSignedIn: () -> Unit,
    onSignedOut: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val store = LocalFakeStore.current
    val currentOnSignedIn by rememberUpdatedState(onSignedIn)
    val currentOnSignedOut by rememberUpdatedState(onSignedOut)
    LaunchedEffect(Unit) {
        delay(SPLASH_DELAY_MILLIS)
        if (store.signedIn) currentOnSignedIn() else currentOnSignedOut()
    }
    SplashScreen(modifier)
}

/** `AFTER_TIMEOUT` of the prototype reaction on Splash Screen (0.4 s). */
private const val SPLASH_DELAY_MILLIS = 400L
