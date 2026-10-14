package com.example.ecommercenativec19.feature.account

/** What the Account screen shows. [address] is one line: the design has a single address field. */
data class Profile(
    val name: String,
    val email: String,
    val phone: String,
    val address: String,
)
