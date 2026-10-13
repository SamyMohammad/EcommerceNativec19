package com.example.ecommercenativec19.core.session

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/** In-memory [SessionRepository] for tests of code that only needs the session contract. */
class FakeSessionRepository(initial: SessionState = SessionState.SignedOut) : SessionRepository {
    private val _state = MutableStateFlow(initial)
    override val state: StateFlow<SessionState> = _state

    var saveError: Exception? = null

    override suspend fun save(token: String) {
        saveError?.let { throw it }
        _state.value = SessionState.SignedIn(token)
    }

    override suspend fun clear() {
        _state.value = SessionState.SignedOut
    }

    override suspend fun invalidate(rejectedToken: String) {
        if ((_state.value as? SessionState.SignedIn)?.token == rejectedToken) clear()
    }

    fun set(state: SessionState) {
        _state.value = state
    }
}
