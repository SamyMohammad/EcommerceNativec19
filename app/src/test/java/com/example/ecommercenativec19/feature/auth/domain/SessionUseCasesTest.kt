package com.example.ecommercenativec19.feature.auth.domain

import com.example.ecommercenativec19.core.session.FakeSessionRepository
import com.example.ecommercenativec19.core.session.SessionState
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

class SessionUseCasesTest {
    private val sessions = FakeSessionRepository(SessionState.SignedIn("jwt-1"))

    @Test
    fun `sign out clears the session`() = runTest {
        SignOutUseCase(sessions)()

        assertEquals(SessionState.SignedOut, sessions.state.value)
    }

    @Test
    fun `observe session follows the repository`() {
        val observed = ObserveSessionUseCase(sessions)()

        sessions.set(SessionState.SignedOut)

        assertEquals(SessionState.SignedOut, observed.value)
    }
}
