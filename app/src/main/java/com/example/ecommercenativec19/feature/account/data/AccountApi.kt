package com.example.ecommercenativec19.feature.account.data

import com.example.ecommercenativec19.core.network.ItemResponse
import com.example.ecommercenativec19.core.network.ListResponse
import com.example.ecommercenativec19.feature.auth.data.AuthResponseDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * All endpoints need the `token` header (added by AuthInterceptor). The two profile writes return [Response] so the
 * repository can read the 400 body (see docs/api/account.md).
 */
interface AccountApi {
    @GET("api/v1/users/getMe")
    suspend fun getMe(): ItemResponse<UserDto>

    @PUT("api/v1/users/updateMe")
    suspend fun updateMe(@Body request: UpdateMeRequest): Response<Unit>

    /** The answer carries a NEW token; the old one stops working. */
    @PUT("api/v1/users/changeMyPassword")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<AuthResponseDto>

    /** Answers with the whole address list, the new address last. */
    @POST("api/v1/addresses")
    suspend fun addAddress(@Body request: AddAddressRequest): ListResponse<AddressDto>

    @DELETE("api/v1/addresses/{id}")
    suspend fun removeAddress(@Path("id") id: String): ListResponse<AddressDto>
}
