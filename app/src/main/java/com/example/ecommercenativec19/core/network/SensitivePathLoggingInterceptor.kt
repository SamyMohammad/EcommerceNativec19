package com.example.ecommercenativec19.core.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Routes each request to one of two loggers: [safe] for `/auth/` and `/users/` endpoints, [default] for the rest.
 *
 * The auth request bodies carry the password, the 400 validation response echoes it back, and the success response
 * carries the token. `/users/` adds the password-change request (current and new password, new token in the answer)
 * and `getMe`, whose body holds the stored password hash plus e-mail and phone. A header-level logger prints the line
 * and status without any body, so none of that reaches Logcat even in debug builds.
 */
class SensitivePathLoggingInterceptor(
    private val default: Interceptor,
    private val safe: Interceptor,
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val path = chain.request().url.encodedPath
        val sensitive = SENSITIVE_SEGMENTS.any(path::contains)
        return if (sensitive) safe.intercept(chain) else default.intercept(chain)
    }

    private companion object {
        val SENSITIVE_SEGMENTS = listOf("/auth/", "/users/")
    }
}
