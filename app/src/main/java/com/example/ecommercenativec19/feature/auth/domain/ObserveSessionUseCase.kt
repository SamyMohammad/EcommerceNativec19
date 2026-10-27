package com.example.ecommercenativec19.feature.auth.domain

import com.example.ecommercenativec19.core.session.SessionRepository
import com.example.ecommercenativec19.core.session.SessionState
import javax.inject.Inject
import kotlinx.coroutines.flow.StateFlow

/** Exposes the session to presentation without handing out the token-writing repository. */
class ObserveSessionUseCase @Inject constructor(private val sessions: SessionRepository) {
    operator fun invoke(): StateFlow<SessionState> = sessions.state
}
