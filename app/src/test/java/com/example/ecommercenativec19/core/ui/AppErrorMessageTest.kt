package com.example.ecommercenativec19.core.ui

import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.common.AppError
import org.junit.Assert.assertEquals
import org.junit.Test

class AppErrorMessageTest {

    @Test
    fun `each AppError maps to its own message resource`() {
        assertEquals(R.string.error_network, AppError.Network.toMessageRes())
        assertEquals(R.string.error_server, AppError.Server(500).toMessageRes())
        assertEquals(R.string.error_parsing, AppError.Parsing.toMessageRes())
        assertEquals(R.string.error_unknown, AppError.Unknown.toMessageRes())
    }
}
