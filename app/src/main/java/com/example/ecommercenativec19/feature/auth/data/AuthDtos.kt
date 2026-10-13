package com.example.ecommercenativec19.feature.auth.data

import kotlinx.serialization.Serializable

// Request DTOs hide the password from toString so a stray log or assertion message cannot leak it.

@Serializable
data class SignInRequest(val email: String, val password: String) {
    override fun toString(): String = "SignInRequest(email=$email, password=***)"
}

@Serializable
data class SignUpRequest(
    val name: String,
    val email: String,
    val password: String,
    val rePassword: String,
    val phone: String,
) {
    override fun toString(): String = "SignUpRequest(name=$name, email=$email, phone=$phone, password=***)"
}

/** Only the token is read; the response also carries `message` and `user { name, email, role }`. */
@Serializable
data class AuthResponseDto(val token: String) {
    override fun toString(): String = "AuthResponseDto(token=***)"
}
