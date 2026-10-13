package com.example.ecommercenativec19.core.network

import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Runs the interceptor in a real [OkHttpClient] whose last interceptor answers locally, so the tests need
 * neither a network nor a mock web server.
 */
class AuthInterceptorTest {

    private var token: String? = null
    private var status = 200
    private val rejected = mutableListOf<String>()
    private var sentRequest: Request? = null

    private val client = OkHttpClient.Builder()
        .addInterceptor(AuthInterceptor(tokenProvider = { token }, onUnauthorized = { rejected += it }))
        .addInterceptor { chain ->
            sentRequest = chain.request()
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(status)
                .message("test")
                .body("{}".toResponseBody())
                .build()
        }
        .build()

    private fun call(path: String) {
        client.newCall(Request.Builder().url("https://example.test/api/v1/$path").build()).execute().close()
    }

    @Test
    fun `adds the token header when a session exists`() {
        token = "jwt-1"

        call("cart")

        assertEquals("jwt-1", sentRequest?.header("token"))
    }

    @Test
    fun `sends no token header when signed out`() {
        call("products")

        assertNull(sentRequest?.header("token"))
    }

    @Test
    fun `never attaches the token to sign in or sign up`() {
        token = "jwt-1"

        call("auth/signin")
        assertNull(sentRequest?.header("token"))

        call("auth/signup")
        assertNull(sentRequest?.header("token"))
    }

    @Test
    fun `attaches the token to verifyToken`() {
        token = "jwt-1"

        call("auth/verifyToken")

        assertEquals("jwt-1", sentRequest?.header("token"))
    }

    @Test
    fun `reports the rejected token on 401 for an authenticated request`() {
        token = "jwt-1"
        status = 401

        call("cart")

        assertEquals(listOf("jwt-1"), rejected)
    }

    @Test
    fun `ignores 401 when no token was attached`() {
        status = 401

        call("products")

        assertEquals(emptyList<String>(), rejected)
    }

    @Test
    fun `ignores 401 from sign in because it means wrong password`() {
        token = "jwt-1"
        status = 401

        call("auth/signin")

        assertEquals(emptyList<String>(), rejected)
    }

    @Test
    fun `ignores other error codes`() {
        token = "jwt-1"
        status = 500

        call("cart")

        assertEquals(emptyList<String>(), rejected)
    }
}
