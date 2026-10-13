package com.example.ecommercenativec19.core.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Protocol
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Test

class SensitivePathLoggingInterceptorTest {

    private val used = mutableListOf<String>()

    private fun marker(name: String) = Interceptor { chain ->
        used += name
        chain.proceed(chain.request())
    }

    private val client = OkHttpClient.Builder()
        .addInterceptor(SensitivePathLoggingInterceptor(default = marker("default"), safe = marker("safe")))
        .addInterceptor { chain ->
            Response.Builder()
                .request(chain.request())
                .protocol(Protocol.HTTP_1_1)
                .code(200)
                .message("ok")
                .body("{}".toResponseBody())
                .build()
        }
        .build()

    private fun call(path: String) {
        client.newCall(Request.Builder().url("https://example.test/api/v1/$path").build()).execute().close()
    }

    @Test
    fun `auth endpoints use the header-only logger`() {
        call("auth/signin")
        call("auth/signup")
        call("auth/verifyToken")

        assertEquals(listOf("safe", "safe", "safe"), used)
    }

    @Test
    fun `user endpoints use the header-only logger`() {
        call("users/getMe")
        call("users/updateMe")
        call("users/changeMyPassword")

        assertEquals(listOf("safe", "safe", "safe"), used)
    }

    @Test
    fun `address endpoints use the header-only logger`() {
        call("addresses")
        call("addresses/a1")

        assertEquals(listOf("safe", "safe"), used)
    }

    @Test
    fun `other endpoints use the default logger`() {
        call("products")

        assertEquals(listOf("default"), used)
    }
}
