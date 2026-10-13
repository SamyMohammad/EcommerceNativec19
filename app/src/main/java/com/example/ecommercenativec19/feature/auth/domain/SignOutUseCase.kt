package com.example.ecommercenativec19.feature.auth.domain

import com.example.ecommercenativec19.core.session.SessionRepository

/** Clears the stored session; navigation reacts to the session turning signed-out. */
class SignOutUseCase(private val sessions: SessionRepository) {
    suspend operator fun invoke() = sessions.clear()
}
