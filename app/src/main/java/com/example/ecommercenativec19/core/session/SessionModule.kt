package com.example.ecommercenativec19.core.session

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

/** File name under `files/datastore/`; backup_rules.xml and data_extraction_rules.xml exclude it by this name. */
private const val SESSION_STORE_NAME = "session"

val sessionModule = module {
    // Process-wide scope for work that must outlive any screen (session loading, 401 handling).
    single<CoroutineScope> { CoroutineScope(SupervisorJob() + Dispatchers.Default) }
    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(scope = get()) { androidContext().preferencesDataStoreFile(SESSION_STORE_NAME) }
    }
    single<TokenCipher> { AndroidKeystoreTokenCipher() }
    // createdAtStart: begin reading the token at app launch, so the cache is warm by the time Splash asks.
    single<SessionRepository>(createdAtStart = true) { DataStoreSessionRepository(get(), get(), get()) }
}
