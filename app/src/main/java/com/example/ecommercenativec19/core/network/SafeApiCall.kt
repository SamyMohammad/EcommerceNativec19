package com.example.ecommercenativec19.core.network

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import java.io.IOException
import kotlin.coroutines.cancellation.CancellationException
import retrofit2.HttpException

/**
 * Runs an API call and maps its failures to [AppError] at the data boundary, so ViewModels never see exceptions.
 * Malformed bodies (SerializationException) fall into [AppError.Unknown].
 */
suspend inline fun <T> safeApiCall(block: suspend () -> T): AppResult<T> = try {
    AppResult.Success(block())
} catch (e: CancellationException) {
    // Swallowing this would break structured concurrency.
    throw e
} catch (e: IOException) {
    AppResult.Failure(AppError.Network)
} catch (e: HttpException) {
    AppResult.Failure(AppError.Server(e.code()))
} catch (e: Exception) {
    AppResult.Failure(AppError.Unknown)
}
