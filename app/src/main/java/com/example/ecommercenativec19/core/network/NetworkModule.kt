package com.example.ecommercenativec19.core.network

import com.example.ecommercenativec19.BuildConfig
import com.example.ecommercenativec19.core.di.ApplicationScope
import com.example.ecommercenativec19.core.session.SessionRepository
import com.example.ecommercenativec19.core.session.SessionState
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

private const val CONNECT_TIMEOUT_SECONDS = 15L
private const val READ_TIMEOUT_SECONDS = 30L
private const val WRITE_TIMEOUT_SECONDS = 30L

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    // The API may add fields at any time; unknown ones must not break parsing.
    @Provides
    @Singleton
    fun provideJson(): Json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        sessionRepository: SessionRepository,
        @ApplicationScope appScope: CoroutineScope,
    ): OkHttpClient {
        val auth = AuthInterceptor(
            // Cached StateFlow value: never blocks an OkHttp thread on disk.
            tokenProvider = { (sessionRepository.state.value as? SessionState.SignedIn)?.token },
            onUnauthorized = { rejected -> appScope.launch { sessionRepository.invalidate(rejected) } },
        )

        fun logger(debugLevel: HttpLoggingInterceptor.Level) = HttpLoggingInterceptor().apply {
            level = if (BuildConfig.DEBUG) debugLevel else HttpLoggingInterceptor.Level.NONE
            // Credentials must never reach Logcat, even in debug builds.
            redactHeader("token")
            redactHeader("Authorization")
        }
        // /auth/ bodies hold passwords and tokens, so those endpoints log headers only.
        val logging = SensitivePathLoggingInterceptor(
            default = logger(HttpLoggingInterceptor.Level.BODY),
            safe = logger(HttpLoggingInterceptor.Level.HEADERS),
        )
        return OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            // Auth first: the logger then sees (and redacts) the token header it adds.
            .addInterceptor(auth)
            .addInterceptor(logging)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient, json: Json): Retrofit =
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
            .build()
}
