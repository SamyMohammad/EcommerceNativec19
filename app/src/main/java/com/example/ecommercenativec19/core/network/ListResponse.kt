package com.example.ecommercenativec19.core.network

import kotlinx.serialization.Serializable

/** Envelope shared by the list endpoints; the API also sends paging metadata, which we ignore for now. */
@Serializable
data class ListResponse<T>(val data: List<T>)
