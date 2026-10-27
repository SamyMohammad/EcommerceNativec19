package com.example.ecommercenativec19.core.session

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStoreFile
import com.example.ecommercenativec19.core.di.ApplicationScope
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

/** File name under `files/datastore/`; backup_rules.xml and data_extraction_rules.xml exclude it by this name. */
private const val SESSION_STORE_NAME = "session"

@Module
@InstallIn(SingletonComponent::class)
object SessionModule {
    // Process-wide scope for work that must outlive any screen (session loading, 401 handling).
    @Provides
    @Singleton
    @ApplicationScope
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)

    @Provides
    @Singleton
    fun provideSessionDataStore(
        @ApplicationContext context: Context,
        @ApplicationScope scope: CoroutineScope,
    ): DataStore<Preferences> =
        PreferenceDataStoreFactory.create(scope = scope) { context.preferencesDataStoreFile(SESSION_STORE_NAME) }

    @Provides
    @Singleton
    fun provideTokenCipher(): TokenCipher = AndroidKeystoreTokenCipher()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class SessionRepositoryBindings {
    // Eagerly created (EcommerceApp injects it): begin reading the token at app launch, so the cache is warm by the time Splash asks.
    @Binds
    @Singleton
    abstract fun bindSessionRepository(impl: DataStoreSessionRepository): SessionRepository
}
