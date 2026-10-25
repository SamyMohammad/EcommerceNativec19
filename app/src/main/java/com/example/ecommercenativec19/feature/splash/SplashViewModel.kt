package com.example.ecommercenativec19.feature.splash

import androidx.lifecycle.ViewModel
import com.example.ecommercenativec19.core.session.SessionState
import com.example.ecommercenativec19.feature.auth.domain.ObserveSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.first

enum class SplashDestination { Home, SignIn }

@HiltViewModel
class SplashViewModel @Inject constructor(private val observeSession: ObserveSessionUseCase) : ViewModel() {
    /** Waits for the stored token to finish loading, then decides where the app starts. */
    suspend fun awaitDestination(): SplashDestination =
        when (observeSession().first { it !is SessionState.Loading }) {
            is SessionState.SignedIn -> SplashDestination.Home
            else -> SplashDestination.SignIn
        }
}
