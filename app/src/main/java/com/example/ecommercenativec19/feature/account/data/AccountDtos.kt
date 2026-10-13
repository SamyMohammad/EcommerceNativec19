package com.example.ecommercenativec19.feature.account.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * The fields the app reads from `getMe`. The response also carries the stored password hash, which is deliberately
 * not declared here so it is never decoded into an object that could be logged.
 */
@Serializable
data class UserDto(
    val name: String,
    val email: String,
    val phone: String? = null,
    val addresses: List<AddressDto> = emptyList(),
)

@Serializable
data class AddressDto(
    @SerialName("_id") val id: String,
    val details: String? = null,
)

/** Null fields are not encoded (default `encodeDefaults = false`), so only the changed fields are sent. */
@Serializable
data class UpdateMeRequest(val name: String? = null, val email: String? = null, val phone: String? = null)

@Serializable
data class ChangePasswordRequest(val currentPassword: String, val password: String, val rePassword: String) {
    // Keeps both passwords out of logs and assertion messages.
    override fun toString(): String = "ChangePasswordRequest(***)"
}

/** The API also keeps a `city`; the design's single address line only fills [details]. */
@Serializable
data class AddAddressRequest(val name: String, val details: String, val phone: String? = null)
