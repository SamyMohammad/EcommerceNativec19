package com.example.ecommercenativec19.core.ui

import android.app.Application
import androidx.compose.ui.semantics.ProgressBarRangeInfo
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.assert
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.click
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTouchInput
import com.example.ecommercenativec19.ui.theme.AppTheme
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
@Config(sdk = [36], application = Application::class, qualifiers = "en-ldltr-w430dp-h932dp-notnight-mdpi")
class AppButtonTest {
    @get:Rule val compose = createComposeRule()

    @Test
    fun loadingShowsProgress() {
        setButton(loading = true)

        compose.onNodeWithTag("AppButtonProgress", useUnmergedTree = true)
            .assertIsDisplayed()
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.ProgressBarRangeInfo, ProgressBarRangeInfo.Indeterminate))
    }

    @Test
    fun loadingAnnouncesState() {
        setButton(loading = true)

        compose.onNodeWithText("Login")
            .assert(SemanticsMatcher.expectValue(SemanticsProperties.StateDescription, "Loading"))
    }

    @Test
    fun loadingBlocksClicks() {
        var clicks = 0
        setButton(loading = true, onClick = { clicks++ })

        compose.onNodeWithText("Login").assertIsNotEnabled().performTouchInput { click() }

        compose.runOnIdle { assertEquals(0, clicks) }
    }

    @Test
    fun notLoadingInvokesClick() {
        var clicks = 0
        setButton(loading = false, onClick = { clicks++ })

        compose.onNodeWithText("Login").performClick()

        compose.runOnIdle { assertEquals(1, clicks) }
    }

    @Test
    fun notLoadingHasNoProgress() {
        setButton(loading = false)

        compose.onNodeWithTag("AppButtonProgress", useUnmergedTree = true).assertDoesNotExist()
    }

    private fun setButton(loading: Boolean, onClick: () -> Unit = {}) {
        compose.setContent {
            AppTheme {
                AppButton(text = "Login", onClick = onClick, loading = loading)
            }
        }
    }
}
