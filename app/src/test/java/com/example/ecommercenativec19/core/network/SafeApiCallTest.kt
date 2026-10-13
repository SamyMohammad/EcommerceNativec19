package com.example.ecommercenativec19.core.network

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.SerializationException
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException

/** Verifies that [safeApiCall] is the single place exceptions get mapped to [AppError] at the data boundary. */
class SafeApiCallTest {

    @Test
    fun `wraps successful result in Success`() = runTest {
        val result = safeApiCall { "payload" }

        assertEquals(AppResult.Success("payload"), result)
    }

    @Test
    fun `maps IOException to Network error`() = runTest {
        val result = safeApiCall { throw IOException("no connection") }

        assertEquals(AppResult.Failure(AppError.Network), result)
    }

    @Test
    fun `maps HttpException to Server error with status code`() = runTest {
        val body = "{}".toResponseBody("application/json".toMediaType())
        val httpException = HttpException(Response.error<Unit>(404, body))

        val result = safeApiCall { throw httpException }

        assertEquals(AppResult.Failure(AppError.Server(404)), result)
    }

    @Test
    fun `maps SerializationException to Parsing error`() = runTest {
        val result = safeApiCall { throw SerializationException("bad json") }

        assertEquals(AppResult.Failure(AppError.Parsing), result)
    }

    @Test
    fun `maps unexpected exception to Unknown error`() = runTest {
        val result = safeApiCall { throw IllegalStateException("boom") }

        assertEquals(AppResult.Failure(AppError.Unknown), result)
    }

    @Test
    fun `rethrows CancellationException instead of mapping it`() = runTest {
        var rethrown = false
        try {
            safeApiCall { throw CancellationException("cancelled") }
        } catch (e: CancellationException) {
            rethrown = true
        }

        assertTrue(rethrown)
    }

    @Test
    fun `does not swallow CancellationException into a Failure`() = runTest {
        var reachedFailureBranch = false
        try {
            val result = safeApiCall { throw CancellationException("cancelled") }
            reachedFailureBranch = result is AppResult.Failure
        } catch (e: CancellationException) {
            // expected
        }

        assertFalse(reachedFailureBranch)
    }
}
