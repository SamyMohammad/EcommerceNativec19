package com.example.ecommercenativec19.core.session

import kotlinx.coroutines.flow.StateFlow

/**
 * The single owner of the auth token. [state] is an in-memory cache, so OkHttp threads can read the current token
 * without waiting for disk.
 */
interface SessionRepository {
    val state: StateFlow<SessionState>

    suspend fun save(token: String)

    suspend fun clear()

    /**
     * Signs out only if [rejectedToken] is still the current one. A 401 for a request sent with an old token must
     * not wipe a session the user created in the meantime.
     */
    suspend fun invalidate(rejectedToken: String)
}
