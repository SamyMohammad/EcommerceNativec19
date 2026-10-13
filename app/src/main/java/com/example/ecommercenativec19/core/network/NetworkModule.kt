package com.example.ecommercenativec19.core.network

import com.example.ecommercenativec19.BuildConfig
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.example.ecommercenativec19.core.session.SessionRepository
import com.example.ecommercenativec19.core.session.SessionState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

private const val CONNECT_TIMEOUT_SECONDS = 15L
private const val READ_TIMEOUT_SECONDS = 30L
private const val WRITE_TIMEOUT_SECONDS = 30L

val networkModule = module {
    // The API may add fields at any time; unknown ones must not break parsing.
    single { Json { ignoreUnknownKeys = true } }
    single {
        val sessionRepository = get<SessionRepository>()
        val appScope = get<CoroutineScope>()
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
        OkHttpClient.Builder()
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            // Auth first: the logger then sees (and redacts) the token header it adds.
            .addInterceptor(auth)
            .addInterceptor(logging)
            .build()
    }
    single {
        Retrofit.Builder()
            .baseUrl(BuildConfig.BASE_URL)
            .client(get())
            .addConverterFactory(get<Json>().asConverterFactory("application/json".toMediaType()))
            .build()
    }
}
