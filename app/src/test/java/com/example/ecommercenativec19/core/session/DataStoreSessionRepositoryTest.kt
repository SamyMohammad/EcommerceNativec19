package com.example.ecommercenativec19.core.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import java.io.File
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotEquals
import org.junit.Assert.assertNull
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TemporaryFolder

class DataStoreSessionRepositoryTest {

    @get:Rule
    val tmp = TemporaryFolder()

    /** Reversible stand-in for the Keystore cipher; anything without the prefix counts as corrupt. */
    private class FakeCipher : TokenCipher {
        override fun encrypt(plainText: String) = "enc:" + plainText.reversed()

        override fun decrypt(cipherText: String): String {
            require(cipherText.startsWith("enc:")) { "corrupt" }
            return cipherText.removePrefix("enc:").reversed()
        }
    }

    private val rawKey = stringPreferencesKey("token")

    /**
     * In-memory DataStore. The real file-backed one replaces its file with File.renameTo, which fails on Windows
     * whenever the target exists (every write after the first), so multi-write tests cannot use it on a Windows dev
     * machine. The real store is covered once, with a single write, in the smoke test below.
     */
    private class InMemoryPreferencesStore : DataStore<Preferences> {
        private val state = MutableStateFlow(emptyPreferences())
        override val data: Flow<Preferences> = state

        override suspend fun updateData(transform: suspend (t: Preferences) -> Preferences): Preferences {
            val updated = transform(state.value)
            state.value = updated
            return updated
        }
    }

    private fun dataStore(): DataStore<Preferences> = InMemoryPreferencesStore()

    @Test
    fun `smoke test with the real file-backed DataStore`() = runTest {
        val file = File(tmp.root, "session.preferences_pb")
        val store = PreferenceDataStoreFactory.create(scope = backgroundScope) { file }
        val repo = repository(store)

        repo.save("jwt-1")

        assertEquals(SessionState.SignedIn("jwt-1"), repo.state.first { it is SessionState.SignedIn })
        assertNotEquals("jwt-1", store.data.first()[rawKey])
    }

    private fun TestScope.repository(store: DataStore<Preferences>) =
        DataStoreSessionRepository(store, FakeCipher(), backgroundScope)

    @Test
    fun `empty store resolves from Loading to SignedOut`() = runTest {
        val repo = repository(dataStore())

        assertEquals(SessionState.SignedOut, repo.state.first { it !is SessionState.Loading })
    }

    @Test
    fun `saved token is exposed as SignedIn`() = runTest {
        val repo = repository(dataStore())

        repo.save("jwt-1")

        assertEquals(SessionState.SignedIn("jwt-1"), repo.state.first { it is SessionState.SignedIn })
    }

    @Test
    fun `token is stored encrypted, never as plain text`() = runTest {
        val store = dataStore()
        val repo = repository(store)

        repo.save("jwt-1")

        val stored = store.data.first()[rawKey]
        assertNotEquals("jwt-1", stored)
        assertEquals("enc:1-twj", stored)
    }

    @Test
    fun `a new repository on the same store restores the session`() = runTest {
        val store = dataStore()
        repository(store).save("jwt-1")

        val restarted = repository(store)

        assertEquals(SessionState.SignedIn("jwt-1"), restarted.state.first { it !is SessionState.Loading })
    }

    @Test
    fun `clear signs out`() = runTest {
        val repo = repository(dataStore())
        repo.save("jwt-1")
        repo.state.first { it is SessionState.SignedIn }

        repo.clear()

        assertEquals(SessionState.SignedOut, repo.state.first { it is SessionState.SignedOut })
    }

    @Test
    fun `invalidate clears only the rejected token`() = runTest {
        val repo = repository(dataStore())
        repo.save("new-jwt")
        repo.state.first { it is SessionState.SignedIn }

        repo.invalidate("old-jwt")
        assertEquals(SessionState.SignedIn("new-jwt"), repo.state.value)

        repo.invalidate("new-jwt")
        assertEquals(SessionState.SignedOut, repo.state.first { it is SessionState.SignedOut })
    }

    @Test
    fun `a late rejection of the old token does not clear the token that replaced it`() = runTest {
        val store = dataStore()
        val repo = repository(store)
        repo.save("jwt-A")
        repo.state.first { it == SessionState.SignedIn("jwt-A") }

        // No suspension point between these calls, so the in-memory state still shows jwt-A: the lagging cache that
        // a comparison against state.value would trust.
        repo.save("jwt-B")
        repo.invalidate("jwt-A")

        assertEquals("enc:B-twj", store.data.first()[rawKey])
        assertEquals(SessionState.SignedIn("jwt-B"), repo.state.first { it == SessionState.SignedIn("jwt-B") })
    }

    @Test
    fun `invalidate removes an unreadable blob`() = runTest {
        val store = dataStore()
        val repo = repository(store)
        store.edit { it[rawKey] = "not-encrypted-by-this-key" }

        repo.invalidate("anything")

        assertNull(store.data.first()[rawKey])
    }

    @Test
    fun `an undecryptable blob means signed out and is removed`() = runTest {
        val store = dataStore()
        store.edit { it[rawKey] = "not-encrypted-by-this-key" }

        val repo = repository(store)

        assertEquals(SessionState.SignedOut, repo.state.first { it !is SessionState.Loading })
        assertNull(store.data.first { it[rawKey] == null }[rawKey])
    }

    @Test
    fun `SignedIn toString does not reveal the token`() {
        assertEquals("SignedIn", SessionState.SignedIn("secret-token").toString())
    }
}
