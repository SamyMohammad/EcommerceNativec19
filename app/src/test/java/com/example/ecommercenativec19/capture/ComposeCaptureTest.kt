package com.example.ecommercenativec19.capture

import android.app.Application
import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.semantics.SemanticsNode
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.example.ecommercenativec19.R
import com.example.ecommercenativec19.core.ui.AppTab
import com.example.ecommercenativec19.feature.account.AccountRoute
import com.example.ecommercenativec19.feature.cart.CartRoute
import com.example.ecommercenativec19.feature.categories.CategoriesSampleData
import com.example.ecommercenativec19.feature.categories.CategoriesScreen
import com.example.ecommercenativec19.feature.home.HomeRoute
import com.example.ecommercenativec19.feature.productdetails.ProductDetailsRoute
import com.example.ecommercenativec19.feature.productlist.ProductListRoute
import com.example.ecommercenativec19.feature.signin.SignInRoute
import com.example.ecommercenativec19.feature.signup.SignUpRoute
import com.example.ecommercenativec19.feature.splash.SplashScreen
import com.example.ecommercenativec19.feature.wishlist.WishlistRoute
import com.example.ecommercenativec19.navigation.AppScaffold
import com.example.ecommercenativec19.ui.theme.AppTheme
import com.github.takahirom.roborazzi.captureRoboImage
import java.io.File
import java.security.MessageDigest
import javax.imageio.ImageIO
import org.json.JSONArray
import org.json.JSONObject
import org.junit.Assert.assertEquals
import org.junit.Assume.assumeTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.RuntimeEnvironment
import org.robolectric.annotation.Config
import org.robolectric.annotation.GraphicsMode

private const val SOURCE_REVISION = "2409508187813363894"
private const val ROBORAZZI_VERSION = "1.76.0"
private const val ROBOLECTRIC_VERSION = "4.17"

/** Source screens bound to the figma-core receipt (design/figma/source-evidence/manifest.json). */
private enum class Scene(val variant: String, val nodeId: String, val containsText: Boolean) {
    Splash("splash", "1:2", false),
    SignIn("signin", "14:47", true),
    SignUp("signup", "9:97", true),
    Home("home", "14:116", true),
    Categories("categories", "601:775", true),
    ProductList("productlist", "726:650", true),
    Details("details", "620:1083", true),
    Cart("cart", "604:890", true),
    Wishlist("wishlist", "731:1294", true),
    Account("account", "835:523", true),
}

@RunWith(RobolectricTestRunner::class)
@GraphicsMode(GraphicsMode.Mode.NATIVE)
// Plain Application: avoids EcommerceApp.startKoin running once per Robolectric test (it would throw on the second).
@Config(sdk = [36], application = Application::class, qualifiers = "en-ldltr-w430dp-h932dp-notnight-mdpi")
class ComposeCaptureTest {
    @get:Rule val compose = createComposeRule()

    @Test fun splash() = capture(Scene.Splash)
    @Test fun signin() = capture(Scene.SignIn)
    @Test fun signup() = capture(Scene.SignUp)
    @Test fun home() = capture(Scene.Home)
    @Test fun categories() = capture(Scene.Categories)
    @Test fun productlist() = capture(Scene.ProductList)
    @Test fun details() = capture(Scene.Details)
    @Test fun cart() = capture(Scene.Cart)
    @Test fun wishlist() = capture(Scene.Wishlist)
    @Test fun account() = capture(Scene.Account)

    @Composable
    private fun SceneContent(scene: Scene) {
        // Tab screens no longer draw the bottom bar; the shell does, so render them inside it.
        val tab = when (scene) {
            Scene.Home -> AppTab.Home
            Scene.Categories, Scene.ProductList -> AppTab.Categories
            Scene.Wishlist -> AppTab.Wishlist
            Scene.Account -> AppTab.Account
            else -> null
        }
        AppScaffold(selectedTab = tab, onTabSelected = {}) { SceneBody(scene) }
    }

    @Composable
    private fun SceneBody(scene: Scene) {
        when (scene) {
            Scene.Splash -> SplashScreen()
            Scene.SignIn -> SignInRoute(onSignIn = {}, onCreateAccount = {})
            Scene.SignUp -> SignUpRoute(onSignUp = {})
            Scene.Home -> HomeRoute(onOpenCart = {}, onOpenCategories = {})
            // The screen with sample state, not the Route: the Route needs Koin and the network.
            Scene.Categories -> CategoriesScreen(CategoriesSampleData.state, {}, {}, {}, {}, {})
            Scene.ProductList -> ProductListRoute(onOpenCart = {}, onOpenProduct = {})
            Scene.Details -> ProductDetailsRoute(productId = "1", onBack = {}, onOpenCart = {}, onAddToCart = {})
            Scene.Cart -> CartRoute(onBack = {})
            Scene.Wishlist -> WishlistRoute(onOpenCart = {})
            Scene.Account -> AccountRoute()
        }
    }

    private fun hasText(node: SemanticsNode): Boolean =
        node.config.contains(SemanticsProperties.Text) || node.config.contains(SemanticsProperties.EditableText) || node.children.any(::hasText)

    private fun sha256(bytes: ByteArray) = MessageDigest.getInstance("SHA-256").digest(bytes).joinToString("") { "%02x".format(it) }

    private fun capture(scene: Scene) {
        assumeTrue("Candidate recording must be explicitly enabled", System.getProperty("roborazzi.test.record") == "true")
        val width = 430
        val height = 932
        RuntimeEnvironment.getApplication().resources.configuration.fontScale = 1f
        compose.mainClock.autoAdvance = false
        var observed = JSONObject()
        compose.setContent {
            val configuration = LocalConfiguration.current
            val direction = if (configuration.layoutDirection == 1) LayoutDirection.Rtl else LayoutDirection.Ltr
            CompositionLocalProvider(LocalLayoutDirection provides direction) {
                val density = LocalDensity.current
                val layoutDirection = LocalLayoutDirection.current
                SideEffect {
                    observed = JSONObject().put("density", density.density).put("fontScale", density.fontScale)
                        .put("locale", configuration.locales[0].toLanguageTag())
                        .put("direction", if (layoutDirection == LayoutDirection.Rtl) "rtl" else "ltr")
                        .put("theme", if (configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK == Configuration.UI_MODE_NIGHT_YES) "dark" else "light")
                }
                AppTheme {
                    Box(Modifier.size(width.dp, height.dp).background(MaterialTheme.colorScheme.background).testTag("capture-frame")) {
                        SceneContent(scene)
                    }
                }
            }
        }
        compose.mainClock.advanceTimeByFrame()
        compose.waitForIdle()
        val target = compose.onNodeWithTag("capture-frame").assertIsDisplayed()
        val semantics = target.fetchSemanticsNode()
        val containsText = hasText(semantics)
        assertEquals(scene.containsText, containsText)
        val bounds = semantics.boundsInRoot
        assertEquals(width.toFloat(), bounds.width, 0f)
        assertEquals(height.toFloat(), bounds.height, 0f)
        val metrics = RuntimeEnvironment.getApplication().resources.displayMetrics
        assertEquals(metrics.density, observed.getDouble("density").toFloat(), 0f)
        assertEquals(1.0, observed.getDouble("density"), 0.0)
        assertEquals("light", observed.getString("theme"))
        assertEquals("ltr", observed.getString("direction"))
        assertEquals("en", observed.getString("locale"))
        val folder = File("build/figma-candidates/${scene.variant}").apply { mkdirs() }
        val image = File(folder, "actual.png")
        target.captureRoboImage(image.absolutePath)
        val png = ImageIO.read(image)
        assertEquals(true, png.colorModel.colorSpace.isCS_sRGB)
        val resources = RuntimeEnvironment.getApplication().resources
        val fonts = JSONObject()
        for ((name, id) in listOf(
            "fonts/Poppins-Light.ttf" to R.font.poppins_light,
            "fonts/Poppins-Regular.ttf" to R.font.poppins_regular,
            "fonts/Poppins-Medium.ttf" to R.font.poppins_medium,
            "fonts/Poppins-SemiBold.ttf" to R.font.poppins_semibold,
        )) fonts.put(name, sha256(resources.openRawResource(id).use { it.readBytes() }))
        val density = observed.getDouble("density")
        observed.put("schemaVersion", 1)
            .put("nodeId", scene.nodeId)
            .put("sourceRevision", SOURCE_REVISION)
            .put("pixelSize", JSONArray(listOf(png.width, png.height)))
            .put("logicalSize", JSONArray(listOf(bounds.width / density, bounds.height / density)))
            .put("targetBounds", JSONArray(listOf(bounds.left, bounds.top, bounds.right, bounds.bottom)))
            .put("crop", JSONArray(listOf(0, 0, png.width, png.height)))
            .put("insets", JSONArray(listOf(0, 0, 0, 0)))
            .put("containsText", containsText).put("usedFontHashes", fonts)
            .put("animation", "settled").put("clockAutoAdvance", compose.mainClock.autoAdvance)
            .put("colorSpace", "srgb").put("platform", "android").put("renderer", "robolectric-native")
            .put("imageHash", sha256(image.readBytes()))
            .put("versions", JSONObject().put("sdk", android.os.Build.VERSION.SDK_INT.toString())
                .put("roborazzi", ROBORAZZI_VERSION).put("robolectric", ROBOLECTRIC_VERSION))
        File(folder, "capture-receipt.json").writeText(observed.toString(2) + "\n")
    }
}
