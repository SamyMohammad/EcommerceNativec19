package com.example.ecommercenativec19.feature.auth.domain

/** Validated sign-up input. The API also wants `rePassword`; the data layer sends the password for both. */
data class SignUpData(
    val name: String,
    val email: String,
    val phone: String,
    val password: String,
) {
    // Keeps the password out of logs and assertion messages.
    override fun toString(): String = "SignUpData(name=$name, email=$email, phone=$phone, password=***)"
}
