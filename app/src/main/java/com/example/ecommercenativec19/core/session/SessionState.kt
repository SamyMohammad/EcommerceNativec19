package com.example.ecommercenativec19.core.session

/**
 * Whether the user is signed in. [Loading] exists because the token is read from disk asynchronously after process
 * start; consumers that must not guess (Splash) wait for the first non-[Loading] value.
 */
sealed interface SessionState {
    data object Loading : SessionState
    data object SignedOut : SessionState

    data class SignedIn(val token: String) : SessionState {
        // The generated toString would print the token into any log or assertion message that shows the state.
        override fun toString(): String = "SignedIn"
    }
}
