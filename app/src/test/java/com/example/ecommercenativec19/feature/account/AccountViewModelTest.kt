package com.example.ecommercenativec19.feature.account

import com.example.ecommercenativec19.core.common.AppError
import com.example.ecommercenativec19.core.common.AppResult
import com.example.ecommercenativec19.core.common.LoadState
import com.example.ecommercenativec19.core.session.FakeSessionRepository
import com.example.ecommercenativec19.core.session.SessionState
import com.example.ecommercenativec19.feature.account.domain.AccountField
import com.example.ecommercenativec19.feature.account.domain.AccountFixtures
import com.example.ecommercenativec19.feature.account.domain.AccountResult
import com.example.ecommercenativec19.feature.account.domain.ChangePasswordUseCase
import com.example.ecommercenativec19.feature.account.domain.FakeAccountRepository
import com.example.ecommercenativec19.feature.account.domain.GetProfileUseCase
import com.example.ecommercenativec19.feature.account.domain.UpdateProfileUseCase
import com.example.ecommercenativec19.feature.auth.domain.AuthError
import com.example.ecommercenativec19.feature.auth.domain.FieldError
import com.example.ecommercenativec19.feature.auth.domain.SignOutUseCase
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class AccountViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private val repository = FakeAccountRepository()
    private val sessions = FakeSessionRepository(SessionState.SignedIn("jwt-1"))
    private val profile = AccountFixtures.profile

    @Before
    fun setUp() {
        Dispatchers.setMain(dispatcher)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private fun viewModel() = AccountViewModel(
        GetProfileUseCase(repository),
        UpdateProfileUseCase(repository),
        ChangePasswordUseCase(repository),
        SignOutUseCase(sessions),
    )

    private fun withLoadedViewModel(block: suspend (AccountViewModel) -> Unit) = runTest(dispatcher) {
        val vm = viewModel()
        advanceUntilIdle()
        block(vm)
    }

    @Test
    fun `starts loading, then shows the profile`() = runTest(dispatcher) {
        val vm = viewModel()
        assertEquals(LoadState.Loading, vm.state.value.profile)

        advanceUntilIdle()

        assertEquals(LoadState.Content(profile), vm.state.value.profile)
    }

    @Test
    fun `load failure is an error state and retry reloads`() = runTest(dispatcher) {
        repository.profileResult = AppResult.Failure(AppError.Network)
        val vm = viewModel()
        advanceUntilIdle()
        assertEquals(LoadState.Error(AppError.Network), vm.state.value.profile)

        repository.profileResult = AppResult.Success(profile)
        vm.onRetry()
        advanceUntilIdle()

        assertEquals(LoadState.Content(profile), vm.state.value.profile)
        assertEquals(2, repository.getProfileCalls)
    }

    @Test
    fun `retry does nothing while content is shown`() = withLoadedViewModel { vm ->
        vm.onRetry()
        idle()

        assertEquals(1, repository.getProfileCalls)
    }

    private fun idle() = dispatcher.scheduler.advanceUntilIdle()

    @Test
    fun `starting an edit puts the saved value into the draft`() = withLoadedViewModel { vm ->
        vm.onStartEditing(AccountFieldType.Mobile)

        assertEquals(AccountFieldType.Mobile, vm.state.value.editing)
        assertEquals(profile.phone, vm.state.value.draft)
    }

    @Test
    fun `saving a changed name updates the profile and ends editing`() = withLoadedViewModel { vm ->
        vm.onStartEditing(AccountFieldType.FullName)
        vm.onValueChange("Janet Doe")
        vm.onFinishEditing()
        idle()

        val state = vm.state.value
        assertEquals(LoadState.Content(profile.copy(name = "Janet Doe")), state.profile)
        assertNull(state.editing)
        assertEquals(false, state.isSaving)
        assertEquals("Janet Doe", repository.lastChanges?.name)
    }

    @Test
    fun `validation failure keeps editing with the field error and sends nothing`() = withLoadedViewModel { vm ->
        vm.onStartEditing(AccountFieldType.Email)
        vm.onValueChange("nope")
        vm.onFinishEditing()
        idle()

        val state = vm.state.value
        assertEquals(AccountFieldType.Email, state.editing)
        assertEquals(FieldError.InvalidEmail, state.fieldError)
        assertEquals(0, repository.updateCalls)
    }

    @Test
    fun `typing clears the previous errors`() = withLoadedViewModel { vm ->
        vm.onStartEditing(AccountFieldType.Email)
        vm.onValueChange("nope")
        vm.onFinishEditing()
        idle()

        vm.onValueChange("jane@example.com")

        assertNull(vm.state.value.fieldError)
        assertNull(vm.state.value.saveError)
    }

    @Test
    fun `server failure keeps editing and shows the save error`() = withLoadedViewModel { vm ->
        val error = AuthError.Validation("E-mail already in use")
        repository.updateResult = AccountResult.Failure(error)
        vm.onStartEditing(AccountFieldType.Email)
        vm.onValueChange("taken@example.com")
        vm.onFinishEditing()
        idle()

        val state = vm.state.value
        assertEquals(error, state.saveError)
        assertEquals(AccountFieldType.Email, state.editing)
        assertEquals(LoadState.Content(profile), state.profile)
    }

    @Test
    fun `a second finish while saving is ignored`() = withLoadedViewModel { vm ->
        repository.gate = CompletableDeferred()
        vm.onStartEditing(AccountFieldType.FullName)
        vm.onValueChange("Janet Doe")
        vm.onFinishEditing()
        idle()
        assertTrue(vm.state.value.isSaving)

        vm.onFinishEditing()
        repository.gate?.complete(Unit)
        idle()

        assertEquals(1, repository.updateCalls)
    }

    @Test
    fun `finishing an unchanged field sends nothing and ends editing`() = withLoadedViewModel { vm ->
        vm.onStartEditing(AccountFieldType.Address)
        vm.onFinishEditing()
        idle()

        assertNull(vm.state.value.editing)
        assertEquals(0, repository.updateCalls)
    }

    @Test
    fun `the password row opens the dialog instead of editing inline`() = withLoadedViewModel { vm ->
        vm.onStartEditing(AccountFieldType.Password)

        assertEquals(PasswordFormUi(), vm.state.value.passwordForm)
        assertNull(vm.state.value.editing)
    }

    @Test
    fun `valid password change closes the dialog`() = withLoadedViewModel { vm ->
        vm.onStartEditing(AccountFieldType.Password)
        vm.onCurrentPasswordChange("old-pass")
        vm.onNewPasswordChange("new-pass")
        vm.onSubmitPassword()
        idle()

        assertNull(vm.state.value.passwordForm)
        assertEquals("old-pass" to "new-pass", repository.lastPasswords)
    }

    @Test
    fun `short new password shows a field error and sends nothing`() = withLoadedViewModel { vm ->
        vm.onStartEditing(AccountFieldType.Password)
        vm.onCurrentPasswordChange("old-pass")
        vm.onNewPasswordChange("abc")
        vm.onSubmitPassword()
        idle()

        val form = vm.state.value.passwordForm
        assertEquals(mapOf(AccountField.NewPassword to FieldError.PasswordTooShort), form?.errors)
        assertEquals(false, form?.isSaving)
        assertEquals(0, repository.passwordCalls)
    }

    @Test
    fun `wrong current password keeps the dialog open with the server message`() = withLoadedViewModel { vm ->
        val error = AuthError.Validation("Incorrect current password")
        repository.passwordResult = AccountResult.Failure(error)
        vm.onStartEditing(AccountFieldType.Password)
        vm.onCurrentPasswordChange("wrong")
        vm.onNewPasswordChange("new-pass")
        vm.onSubmitPassword()
        idle()

        assertEquals(error, vm.state.value.passwordForm?.submitError)
    }

    @Test
    fun `dialog cannot be dismissed while saving`() = withLoadedViewModel { vm ->
        repository.gate = CompletableDeferred()
        vm.onStartEditing(AccountFieldType.Password)
        vm.onCurrentPasswordChange("old-pass")
        vm.onNewPasswordChange("new-pass")
        vm.onSubmitPassword()
        idle()

        vm.onDismissPasswordForm()

        assertTrue(vm.state.value.passwordForm?.isSaving == true)
        repository.gate?.complete(Unit)
    }

    @Test
    fun `dismissing the dialog discards the typed passwords`() = withLoadedViewModel { vm ->
        vm.onStartEditing(AccountFieldType.Password)
        vm.onCurrentPasswordChange("old-pass")

        vm.onDismissPasswordForm()

        assertNull(vm.state.value.passwordForm)
    }

    @Test
    fun `sign out clears the session`() = withLoadedViewModel { vm ->
        vm.onSignOut()
        idle()

        assertEquals(SessionState.SignedOut, sessions.state.value)
    }

    @Test
    fun `password form toString hides the passwords`() {
        assertEquals("PasswordFormUi(***)", PasswordFormUi("old-pass", "new-pass").toString())
    }
}
