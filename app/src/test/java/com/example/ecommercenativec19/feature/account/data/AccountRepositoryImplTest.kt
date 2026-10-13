package com.example.ecommercenativec19.feature.account.data

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.network.ItemResponse
import com.example.ecommercenativec19.core.network.ListResponse
import com.example.ecommercenativec19.core.session.FakeSessionRepository
import com.example.ecommercenativec19.core.session.SessionState
import com.example.ecommercenativec19.feature.account.domain.AccountResult
import com.example.ecommercenativec19.feature.account.domain.Profile
import com.example.ecommercenativec19.feature.account.domain.ProfileChanges
import com.example.ecommercenativec19.feature.auth.data.AuthResponseDto
import com.example.ecommercenativec19.feature.auth.domain.AuthError
import java.io.IOException
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test
import retrofit2.Response

class AccountRepositoryImplTest {

    private class FakeAccountApi : AccountApi {
        var me: () -> ItemResponse<UserDto> = { ItemResponse(UserDto("Jane Doe", "jane.doe@example.com")) }
        var updateMe: () -> Response<Unit> = { Response.success(Unit) }
        var changePassword: () -> Response<AuthResponseDto> = { Response.success(AuthResponseDto("new-jwt")) }
        var addAddress: () -> ListResponse<AddressDto> = { ListResponse(listOf(AddressDto("a1", "1 Example Road"))) }
        var removeAddress: () -> ListResponse<AddressDto> = { ListResponse(emptyList()) }

        var lastUpdate: UpdateMeRequest? = null
        var lastPassword: ChangePasswordRequest? = null
        var lastAddress: AddAddressRequest? = null
        val removedIds = mutableListOf<String>()

        override suspend fun getMe() = me()

        override suspend fun updateMe(request: UpdateMeRequest): Response<Unit> {
            lastUpdate = request
            return updateMe()
        }

        override suspend fun changePassword(request: ChangePasswordRequest): Response<AuthResponseDto> {
            lastPassword = request
            return changePassword()
        }

        override suspend fun addAddress(request: AddAddressRequest): ListResponse<AddressDto> {
            lastAddress = request
            return addAddress()
        }

        override suspend fun removeAddress(id: String): ListResponse<AddressDto> {
            removedIds += id
            return removeAddress()
        }
    }

    private val api = FakeAccountApi()
    private val sessions = FakeSessionRepository(SessionState.SignedIn("old-jwt"))
    private val json = Json { ignoreUnknownKeys = true }
    private val repository = AccountRepositoryImpl(api, sessions, json)
    private val current = Profile("Jane Doe", "jane.doe@example.com", "01000000000", "1 Example Road")

    private fun <T> errorResponse(code: Int, body: String): Response<T> =
        Response.error(code, body.toResponseBody("application/json".toMediaType()))

    // --- getProfile

    @Test
    fun `getProfile maps the user and shows the last address`() = runTest {
        api.me = {
            ItemResponse(
                UserDto(
                    "Jane Doe", "jane.doe@example.com", "01000000000",
                    listOf(AddressDto("a1", "Old Street"), AddressDto("a2", "New Street")),
                ),
            )
        }

        val result = repository.getProfile()

        assertEquals(
            AppResult.Success(Profile("Jane Doe", "jane.doe@example.com", "01000000000", "New Street")),
            result,
        )
    }

    @Test
    fun `getProfile treats a missing phone and no address as empty`() = runTest {
        val result = repository.getProfile()

        assertEquals(AppResult.Success(Profile("Jane Doe", "jane.doe@example.com", "", "")), result)
    }

    @Test
    fun `getProfile network failure maps to a network error`() = runTest {
        api.me = { throw IOException("offline") }

        assertEquals(AppResult.Failure(AppError.Network), repository.getProfile())
    }

    @Test
    fun `getMe body is decoded without the password hash`() {
        val body = """{"data":{"_id":"x","name":"Jane Doe","email":"jane.doe@example.com","phone":"01000000000",""" +
            """"password":"${'$'}2a${'$'}hash","addresses":[{"_id":"a1","name":"Home","details":"1 Example Road","city":"Cairo"}]}}"""

        val user = json.decodeFromString<ItemResponse<UserDto>>(body).data

        assertEquals(UserDto("Jane Doe", "jane.doe@example.com", "01000000000", listOf(AddressDto("a1", "1 Example Road"))), user)
    }

    // --- updateProfile

    @Test
    fun `update sends only the changed fields and returns the merged profile`() = runTest {
        val result = repository.updateProfile(current, ProfileChanges(name = "Janet Doe"))

        assertEquals(UpdateMeRequest(name = "Janet Doe"), api.lastUpdate)
        assertEquals(AccountResult.Success(current.copy(name = "Janet Doe")), result)
        assertEquals(null, api.lastAddress)
    }

    @Test
    fun `unset request fields are not encoded`() {
        assertEquals("""{"name":"Janet Doe"}""", json.encodeToString(UpdateMeRequest(name = "Janet Doe")))
    }

    @Test
    fun `update 400 carries the server message`() = runTest {
        api.updateMe = {
            errorResponse(
                400,
                """{"message":"fail","errors":{"value":"x@example.com","msg":"E-mail already in use","param":"email","location":"body"}}""",
            )
        }

        val result = repository.updateProfile(current, ProfileChanges(email = "x@example.com"))

        assertEquals(AccountResult.Failure(AuthError.Validation("E-mail already in use")), result)
    }

    @Test
    fun `update 401 maps to a server error`() = runTest {
        api.updateMe = { errorResponse(401, """{"statusMsg":"fail","message":"invalid token"}""") }

        val result = repository.updateProfile(current, ProfileChanges(name = "Janet Doe"))

        assertEquals(AccountResult.Failure(AuthError.Other(AppError.Server(401))), result)
    }

    @Test
    fun `update network failure maps to a network error`() = runTest {
        api.updateMe = { throw IOException("offline") }

        val result = repository.updateProfile(current, ProfileChanges(name = "Janet Doe"))

        assertEquals(AccountResult.Failure(AuthError.Other(AppError.Network)), result)
    }

    @Test
    fun `address change adds the new address and removes the previous ones`() = runTest {
        api.addAddress = {
            ListResponse(listOf(AddressDto("a1", "Old"), AddressDto("a2", "Older"), AddressDto("a3", "2 New Road")))
        }

        val result = repository.updateProfile(current, ProfileChanges(address = "2 New Road"))

        assertEquals(AddAddressRequest("Home", "2 New Road", "01000000000"), api.lastAddress)
        assertEquals(listOf("a1", "a2"), api.removedIds)
        assertEquals(AccountResult.Success(current.copy(address = "2 New Road")), result)
        assertEquals(null, api.lastUpdate)
    }

    @Test
    fun `a failed removal of an old address is not an error`() = runTest {
        api.addAddress = { ListResponse(listOf(AddressDto("a1", "Old"), AddressDto("a2", "2 New Road"))) }
        api.removeAddress = { throw IOException("offline") }

        val result = repository.updateProfile(current, ProfileChanges(address = "2 New Road"))

        assertEquals(AccountResult.Success(current.copy(address = "2 New Road")), result)
    }

    @Test
    fun `a failed address add is reported`() = runTest {
        api.addAddress = { throw IOException("offline") }

        val result = repository.updateProfile(current, ProfileChanges(address = "2 New Road"))

        assertEquals(AccountResult.Failure(AuthError.Other(AppError.Network)), result)
    }

    // --- changePassword

    @Test
    fun `password change sends the new password twice and stores the new token`() = runTest {
        val result = repository.changePassword("old-pass", "new-pass")

        assertEquals(AccountResult.Success(Unit), result)
        assertEquals(ChangePasswordRequest("old-pass", "new-pass", "new-pass"), api.lastPassword)
        assertEquals(SessionState.SignedIn("new-jwt"), sessions.state.value)
    }

    @Test
    fun `wrong current password keeps the session and carries the server message`() = runTest {
        api.changePassword = {
            errorResponse(400, """{"message":"fail","errors":{"msg":"Incorrect current password","param":"password"}}""")
        }

        val result = repository.changePassword("wrong", "new-pass")

        assertEquals(AccountResult.Failure(AuthError.Validation("Incorrect current password")), result)
        assertEquals(SessionState.SignedIn("old-jwt"), sessions.state.value)
    }

    @Test
    fun `success without a token is a parsing error`() = runTest {
        api.changePassword = { Response.success(null) }

        val result = repository.changePassword("old-pass", "new-pass")

        assertEquals(AccountResult.Failure(AuthError.Other(AppError.Parsing)), result)
    }

    @Test
    fun `failing to store the new token is reported`() = runTest {
        sessions.saveError = IllegalStateException("keystore unavailable")

        val result = repository.changePassword("old-pass", "new-pass")

        assertEquals(AccountResult.Failure(AuthError.Other(AppError.Unknown)), result)
    }

    @Test
    fun `request toString hides the passwords`() {
        assertFalse(ChangePasswordRequest("old-pass", "new-pass", "new-pass").toString().contains("pass"))
    }
}
