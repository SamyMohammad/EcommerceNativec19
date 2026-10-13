package com.example.ecommercenativec19.feature.auth.domain

import kotlinx.coroutines.CompletableDeferred

/** Records calls and answers with [result]; set [gate] to hold a call suspended. */
class FakeAuthRepository(var result: AuthResult = AuthResult.Success) : AuthRepository {
    var gate: CompletableDeferred<Unit>? = null
    var signInCalls = 0
    var lastSignIn: Pair<String, String>? = null
    var signUpCalls = 0
    var lastSignUp: SignUpData? = null

    override suspend fun signIn(email: String, password: String): AuthResult {
        signInCalls++
        lastSignIn = email to password
        gate?.await()
        return result
    }

    override suspend fun signUp(data: SignUpData): AuthResult {
        signUpCalls++
        lastSignUp = data
        gate?.await()
        return result
    }
}
