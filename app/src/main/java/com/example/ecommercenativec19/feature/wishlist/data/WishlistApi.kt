package com.example.ecommercenativec19.feature.wishlist.data

import com.example.ecommercenativec19.core.network.ListResponse
import com.example.ecommercenativec19.feature.products.data.ProductDto
import kotlinx.serialization.Serializable
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * All three endpoints need the `token` header (added by AuthInterceptor). Add and remove answer with the ids of the
 * whole wishlist, not the changed product; both are idempotent (see docs/api/wishlist.md).
 */
interface WishlistApi {
    @GET("api/v1/wishlist")
    suspend fun getWishlist(): ListResponse<ProductDto>

    @POST("api/v1/wishlist")
    suspend fun add(@Body request: AddToWishlistRequest): ListResponse<String>

    @DELETE("api/v1/wishlist/{productId}")
    suspend fun remove(@Path("productId") productId: String): ListResponse<String>
}

@Serializable
data class AddToWishlistRequest(val productId: String)
