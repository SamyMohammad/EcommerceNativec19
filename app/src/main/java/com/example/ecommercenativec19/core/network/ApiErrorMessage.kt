package com.example.ecommercenativec19.core.network

import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import retrofit2.Response

/**
 * Error body. 400 uses `{ message: "fail", errors: { msg, param } }`, 401/409 use `{ statusMsg, message }`;
 * every field is optional so either shape parses.
 */
@Serializable
data class ErrorBodyDto(val message: String? = null, val errors: ErrorDetailDto? = null)

@Serializable
data class ErrorDetailDto(val msg: String? = null)

/**
 * The server's own explanation of a rejected request, or null when the body is missing or not that shape.
 * Retrofit has already buffered an error body, so reading it does not block on the network. The message is shown
 * to the user, never logged, because the 400 body can echo submitted values.
 */
fun Response<*>.apiErrorMessage(json: Json): String? {
    val raw = errorBody()?.string() ?: return null
    return try {
        val body = json.decodeFromString<ErrorBodyDto>(raw)
        (body.errors?.msg ?: body.message)?.trim()?.takeIf { it.isNotEmpty() }
    } catch (e: SerializationException) {
        null
    }
}
