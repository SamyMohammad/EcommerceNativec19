package com.example.ecommercenativec19.feature.auth.domain

import com.example.ecommercenativec19.core.session.SessionRepository
import com.example.ecommercenativec19.core.session.SessionState
import kotlinx.coroutines.flow.StateFlow

/** Exposes the session to presentation without handing out the token-writing repository. */
class ObserveSessionUseCase(private val sessions: SessionRepository) {
    operator fun invoke(): StateFlow<SessionState> = sessions.state
}
