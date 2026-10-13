package com.example.ecommercenativec19.core.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Routes each request to one of two loggers: [safe] for `/auth/` endpoints, [default] for the rest.
 *
 * The auth request bodies carry the password, the 400 validation response echoes it back, and the success response
 * carries the token. A header-level logger prints the line and status without any body, so none of that reaches
 * Logcat even in debug builds.
 */
class SensitivePathLoggingInterceptor(
    private val default: Interceptor,
    private val safe: Interceptor,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val sensitive = chain.request().url.encodedPath.contains("/auth/")
        return if (sensitive) safe.intercept(chain) else default.intercept(chain)
    }
}
