package com.example.ecommercenativec19.core.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Keeps the encrypted token in Preferences DataStore. [state] is derived from the store and held in memory
 * ([SharingStarted.Eagerly], so it starts loading as soon as this object is created at app start).
 *
 * A blob that cannot be decrypted (key lost after a restore or a Keystore reset) is treated as signed out and
 * removed, so the user lands on Sign in instead of looping on 401s.
 */
class DataStoreSessionRepository(
    private val dataStore: DataStore<Preferences>,
    private val cipher: TokenCipher,
    private val scope: CoroutineScope,
) : SessionRepository {

    private sealed interface Stored {
        data object Absent : Stored
        data object Unreadable : Stored
        data class Token(val value: String) : Stored {
            override fun toString(): String = "Token"
        }
    }

    override val state: StateFlow<SessionState> = dataStore.data
        // An unreadable file is recoverable: act as if nothing is stored.
        .catch { if (it is IOException) emit(emptyPreferences()) else throw it }
        .map { preferences -> preferences[TOKEN_KEY].toStored() }
        .onEach { if (it is Stored.Unreadable) scope.launch { clear() } }
        .map { stored ->
            when (stored) {
                is Stored.Token -> SessionState.SignedIn(stored.value)
                Stored.Absent, Stored.Unreadable -> SessionState.SignedOut
            }
        }
        .stateIn(scope, SharingStarted.Eagerly, SessionState.Loading)

    override suspend fun save(token: String) {
        val encrypted = cipher.encrypt(token)
        dataStore.edit { it[TOKEN_KEY] = encrypted }
    }

    override suspend fun clear() {
        dataStore.edit { it.remove(TOKEN_KEY) }
    }

    override suspend fun invalidate(rejectedToken: String) {
        if ((state.value as? SessionState.SignedIn)?.token == rejectedToken) clear()
    }

    private fun String?.toStored(): Stored {
        if (this == null) return Stored.Absent
        return try {
            Stored.Token(cipher.decrypt(this))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            Stored.Unreadable
        }
    }

    private companion object {
        val TOKEN_KEY = stringPreferencesKey("token")
    }
}
