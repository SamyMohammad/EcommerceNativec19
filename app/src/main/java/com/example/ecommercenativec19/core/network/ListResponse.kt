package com.example.ecommercenativec19.core.network

import kotlinx.serialization.Serializable

/** Envelope shared by the list endpoints. [metadata] is optional because not every list endpoint pages. */
@Serializable
data class ListResponse<T>(val data: List<T>, val metadata: PageMetadataDto? = null)

/** Only the field the app reads; the API also sends currentPage, numberOfPages and limit. */
@Serializable
data class PageMetadataDto(
    /** Absent on the last page. */
    val nextPage: Int? = null,
)
