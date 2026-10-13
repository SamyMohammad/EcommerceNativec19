package com.example.ecommercenativec19.feature.auth.data

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Returns [Response] (not the body) so the repository can read the error body of 400/401/409 and map each to a
 * distinct domain error; a plain return type would throw an `HttpException` and lose that distinction.
 */
interface AuthApi {
    @POST("api/v1/auth/signin")
    suspend fun signIn(@Body request: SignInRequest): Response<AuthResponseDto>

    @POST("api/v1/auth/signup")
    suspend fun signUp(@Body request: SignUpRequest): Response<AuthResponseDto>
}
