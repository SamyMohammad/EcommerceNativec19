package com.example.ecommercenativec19.feature.splash

import com.example.ecommercenativec19.core.session.FakeSessionRepository
import com.example.ecommercenativec19.core.session.SessionState
import com.example.ecommercenativec19.feature.auth.domain.ObserveSessionUseCase
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class SplashViewModelTest {
    private fun viewModel(sessions: FakeSessionRepository) = SplashViewModel(ObserveSessionUseCase(sessions))

    @Test
    fun `stored session goes to Home`() = runTest {
        val vm = viewModel(FakeSessionRepository(SessionState.SignedIn("jwt-1")))

        assertEquals(SplashDestination.Home, vm.awaitDestination())
    }

    @Test
    fun `no session goes to Sign in`() = runTest {
        val vm = viewModel(FakeSessionRepository(SessionState.SignedOut))

        assertEquals(SplashDestination.SignIn, vm.awaitDestination())
    }

    @Test
    fun `waits while the session is still loading`() = runTest {
        val sessions = FakeSessionRepository(SessionState.Loading)
        val destination = async { viewModel(sessions).awaitDestination() }

        advanceUntilIdle()
        assertFalse(destination.isCompleted)

        sessions.set(SessionState.SignedIn("jwt-1"))
        advanceUntilIdle()

        assertEquals(SplashDestination.Home, destination.await())
    }
}
