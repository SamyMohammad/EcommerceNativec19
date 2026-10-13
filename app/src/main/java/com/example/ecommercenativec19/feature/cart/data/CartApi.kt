package com.example.ecommercenativec19.feature.cart.data

import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Every endpoint needs the `token` header (added by AuthInterceptor) and answers with the whole cart after the change
 * (see docs/api/cart.md). `DELETE api/v1/cart` (clear) exists but nothing in the app clears the cart yet.
 */
interface CartApi {
    @GET("api/v1/cart")
    suspend fun getCart(): CartResponse

    /** Adds ONE unit, or increments the line by one when the product is already there. */
    @POST("api/v1/cart")
    suspend fun add(@Body request: AddToCartRequest): CartResponse

    /** Fails with 500 when the product is not in the cart. */
    @PUT("api/v1/cart/{productId}")
    suspend fun updateCount(@Path("productId") productId: String, @Body request: UpdateCartCountRequest): CartResponse

    @DELETE("api/v1/cart/{productId}")
    suspend fun remove(@Path("productId") productId: String): CartResponse
}

@Serializable
data class AddToCartRequest(val productId: String)

@Serializable
data class UpdateCartCountRequest(val count: Int)
