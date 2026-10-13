package com.example.ecommercenativec19.core.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Adds the `token` header (the Route API does not use `Authorization: Bearer`) and reports a rejected token.
 *
 * [tokenProvider] must be a non-blocking read of an in-memory cache: interceptors run on OkHttp's threads.
 * [onUnauthorized] fires only when a token was attached and the server answered 401, i.e. the session is expired or
 * revoked. Sign-in and sign-up are exempt: they send no token, and their 401 means "wrong password".
 */
class AuthInterceptor(
    private val tokenProvider: () -> String?,
    private val onUnauthorized: (rejectedToken: String) -> Unit,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = tokenProvider()?.takeUnless { original.url.isCredentialEndpoint() }
        val request = if (token == null) original else original.newBuilder().header(TOKEN_HEADER, token).build()
        val response = chain.proceed(request)
        if (token != null && response.code == HTTP_UNAUTHORIZED) onUnauthorized(token)
        return response
    }

    companion object {
        const val TOKEN_HEADER = "token"
        private const val HTTP_UNAUTHORIZED = 401
    }
}

private fun okhttp3.HttpUrl.isCredentialEndpoint(): Boolean {
    val path = encodedPath
    return path.endsWith("/auth/signin") || path.endsWith("/auth/signup")
}
