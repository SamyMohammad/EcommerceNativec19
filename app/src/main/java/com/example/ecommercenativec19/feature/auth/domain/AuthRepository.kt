package com.example.ecommercenativec19.feature.auth.domain

interface AuthRepository {
    suspend fun signIn(email: String, password: String): AuthResult

    suspend fun signUp(data: SignUpData): AuthResult
}
