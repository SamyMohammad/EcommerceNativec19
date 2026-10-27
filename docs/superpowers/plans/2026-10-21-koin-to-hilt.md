# Koin → Hilt Migration Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Replace Koin with Hilt across the whole app with identical runtime behavior.

**Architecture:** Clean Architecture is untouched; only wiring changes. Constructors get `@Inject`, third-party
types come from `@Provides` in `@InstallIn(SingletonComponent::class)` modules, repository interfaces are bound with
`@Binds`, ViewModels become `@HiltViewModel` resolved with `hiltViewModel()` under Nav3's per-entry
`ViewModelStore`. Koin and Hilt never both own the runtime graph in a committed state.

**Tech Stack:** Hilt 2.60.1 (KSP 2.3.12), `androidx.hilt:hilt-lifecycle-viewmodel-compose` 1.4.0, AGP 9.4.1,
Kotlin 2.2.10, Navigation 3 1.2.0.

**Spec:** `docs/superpowers/specs/2026-10-21-koin-to-hilt-design.md`

## Global Constraints

- Versions: `hilt = "2.60.1"`, `ksp = "2.3.12"` (fallback `2.2.10-2.0.2`), `androidxHilt = "1.4.0"`. No Kotlin, AGP,
  Compose BOM or Gradle bump. If the build needs one, stop and ask the user.
- Hilt runs on KSP (`ksp(libs.hilt.compiler)`), never kapt.
- No Koin artifact, import or comment mention remains in `app/` when the plan ends.
- No feature/UI/navigation behavior change; Clean Architecture layers unchanged.
- Per-feature `di/` package stays as the home of a feature's Hilt module; features with nothing to `@Provides`/`@Binds`
  have no `di/` file.
- App `CoroutineScope` is provided under qualifier `@ApplicationScope`, value `CoroutineScope(SupervisorJob() + Dispatchers.Default)`, `@Singleton`.
- Any non-standard workaround (custom `ViewModelProvider.Factory`, `EntryPointAccessors` in composables) → stop and ask.
- Commits only with explicit user approval, made through the `git-expert` agent. Never commit `local.properties`.
- Gates (same as CI `.github/workflows/android.yml`): `./gradlew assembleDebug testDebugUnitTest lintDebug` and
  `./gradlew assembleRelease`.

## Review Focus

1. Eager start lost: cart badge / favorite hearts empty on first Home render after a cold start while signed in → must be filled as before (Task 3 manual check).
2. Assisted ViewModel not keyed: open product A, back, open product B → B must show B's details, not A's cached VM (Task 3 manual check).
3. Duplicate singletons: two `SessionRepository`/`OkHttpClient` instances (e.g. a stray `@Provides` plus `@Binds`) → sign-out must clear cart/wishlist and bounce to Sign in on 401 (Task 3 manual check).
4. Release/R8: `assembleRelease` must succeed and the release APK must launch to Home (Task 3).
5. Process death on Product details: id must come back from the Nav3 back-stack key (Task 3 manual check with "Don't keep activities").

---

### Task 1: Hilt toolchain on the build

**Files:**
- Modify: `gradle/libs.versions.toml` (versions, libraries, plugins)
- Modify: `build.gradle.kts` (root: `alias(libs.plugins.ksp) apply false`, `alias(libs.plugins.hilt) apply false`)
- Modify: `app/build.gradle.kts` (apply `ksp` + `hilt` plugins; `implementation(libs.hilt.android)`, `ksp(libs.hilt.compiler)`, `implementation(libs.androidx.hilt.lifecycle.viewmodel.compose)`)
- Modify: `app/src/main/java/com/example/ecommercenativec19/EcommerceApp.kt` (add `@HiltAndroidApp`; Koin still started)
- Modify: `app/src/main/java/com/example/ecommercenativec19/MainActivity.kt` (add `@AndroidEntryPoint`)

**Interfaces:**
- Produces: catalog aliases `libs.hilt.android`, `libs.hilt.compiler`, `libs.androidx.hilt.lifecycle.viewmodel.compose`, `libs.plugins.ksp`, `libs.plugins.hilt`.

Catalog entries (exact):
```toml
hilt = "2.60.1"
ksp = "2.3.12"
androidxHilt = "1.4.0"
hilt-android = { group = "com.google.dagger", name = "hilt-android", version.ref = "hilt" }
hilt-compiler = { group = "com.google.dagger", name = "hilt-compiler", version.ref = "hilt" }
androidx-hilt-lifecycle-viewmodel-compose = { group = "androidx.hilt", name = "hilt-lifecycle-viewmodel-compose", version.ref = "androidxHilt" }
ksp = { id = "com.google.devtools.ksp", version.ref = "ksp" }
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
```
Add one comment on the `hilt` version line: why KSP (kapt is incompatible with AGP 9 built-in Kotlin).

- [ ] **Step 1:** Apply the edits above.
- [ ] **Step 2:** Run `./gradlew assembleDebug`. Expected: BUILD SUCCESSFUL and `app/build/generated/ksp/debug/` contains `Hilt_EcommerceApp`. If KSP 2.3.12 fails on AGP 9 built-in Kotlin, retry with `2.2.10-2.0.2`; if both fail, stop and report the error.
- [ ] **Step 3:** Run `./gradlew testDebugUnitTest`. Expected: all pass (Koin still drives runtime; `KoinModulesTest` passes).
- [ ] **Step 4:** Commit (with approval): `build: add Hilt and KSP toolchain`.

### Task 2: Hilt graph for core, data and domain

Koin still runs the app in this task; `@Inject`/Hilt modules are added alongside and compile-validated. Nothing is
requested from Hilt yet, so no runtime duplication.

**Files:**
- Create: `core/di/ApplicationScope.kt` — `@Qualifier @Retention(AnnotationRetention.BINARY) annotation class ApplicationScope`
- Create: `core/session/SessionHiltModule.kt` (object `SessionHiltModule`). Provides: `@ApplicationScope CoroutineScope`, `DataStore<Preferences>` (via `@ApplicationContext`, keeps `SESSION_STORE_NAME = "session"` and its comment), `TokenCipher` → `AndroidKeystoreTokenCipher()`; binds `SessionRepository` → `DataStoreSessionRepository`.
- Create: Hilt module object for network (`core/network/NetworkHiltModule.kt`): `Json`, `OkHttpClient` (same body as today, depending on `SessionRepository` and `@ApplicationScope CoroutineScope`), `Retrofit`. All `@Singleton`. Keep every existing comment.
- Create per feature `feature/<f>/di/<F>HiltModule.kt` for account, auth, cart, categories, products, wishlist: `@Provides @Singleton fun provide<F>Api(retrofit: Retrofit): <F>Api = retrofit.create(...)` and `@Binds @Singleton` for `<F>Repository` → `<F>RepositoryImpl`. (Two objects or an abstract class with a companion — builder's choice, consistent across features.)
- Modify: add `@Inject constructor` to every `*RepositoryImpl`, `DataStoreSessionRepository`, and every use case listed in the Koin modules (`GetCategoriesUseCase`, `GetSubCategoriesUseCase`, `GetProductsUseCase`, `GetProductDetailsUseCase`, `SignInUseCase`, `SignUpUseCase`, `SignOutUseCase`, `ObserveSessionUseCase`, `GetProfileUseCase`, `UpdateProfileUseCase`, `ChangePasswordUseCase`, the five cart use cases in `CartUseCases.kt`, `ObserveFavoriteIdsUseCase`, `ToggleFavoriteUseCase`, `GetWishlistUseCase`). Constructor params of type `CoroutineScope` get `@ApplicationScope`.

File names with `Hilt` suffix are temporary to avoid clashing with the Koin `*Module.kt` files; Task 3 renames them to `<F>Module.kt` after the Koin files are deleted.

**Interfaces:**
- Produces: `@ApplicationScope` qualifier; singleton bindings for `Json`, `OkHttpClient`, `Retrofit`, `DataStore<Preferences>`, `TokenCipher`, `SessionRepository`, every `<F>Api` and `<F>Repository`; injectable use cases.

- [ ] **Step 1:** Apply the edits above.
- [ ] **Step 2:** Run `./gradlew assembleDebug`. Expected: BUILD SUCCESSFUL. A Dagger cycle error here means a real cycle — fix with `Provider<T>` on that one edge plus a comment (spec allows exactly this).
- [ ] **Step 3:** Run `./gradlew testDebugUnitTest`. Expected: all pass, unchanged.
- [ ] **Step 4:** Commit (with approval): `refactor(di): add Hilt bindings for core, data and domain`.

### Task 3: ViewModels on Hilt, Koin removed

**Files:**
- Modify every ViewModel: `@HiltViewModel class XViewModel @Inject constructor(...)` for Account, Cart, Categories, Home, SignIn, SignUp, Splash, Wishlist.
- Modify `ProductDetailsViewModel` and `ProductListViewModel` to assisted injection (keep parameter order and names so the named-argument test calls compile unchanged):
  ```kotlin
  @HiltViewModel(assistedFactory = ProductDetailsViewModel.Factory::class)
  class ProductDetailsViewModel @AssistedInject constructor(
      @Assisted private val productId: String, /* existing params unchanged */
  ) : ViewModel() {
      @AssistedFactory interface Factory { fun create(productId: String): ProductDetailsViewModel }
  }
  ```
  Same shape for `ProductListViewModel` with `subCategoryId`.
- Modify every `*Route.kt`: `koinViewModel()` → `hiltViewModel()`. ProductDetails/ProductList: `hiltViewModel<ProductDetailsViewModel, ProductDetailsViewModel.Factory>(key = productId) { it.create(productId) }` (drop `key` only if 1.4.0 has no such parameter — then the per-entry ViewModelStore alone scopes it; note that in the KDoc).
- Modify `MainActivity`: `@Inject lateinit var observeSession: ObserveSessionUseCase`; call `AppNavDisplay(observeSession = observeSession)`.
- Modify `navigation/AppNavDisplay.kt`: new first parameter `observeSession: ObserveSessionUseCase`; replace `koinInject<ObserveSessionUseCase>()()` with `observeSession()`; update the decorator comment to say `hiltViewModel()`.
- Modify `EcommerceApp`: drop `KoinComponent`/`startKoin`; `@Inject lateinit var okHttpClient: dagger.Lazy<OkHttpClient>` (used as `okHttpClient.get()` in the Coil call factory); `@Inject lateinit var sessionRepository: SessionRepository`, `cartRepository: CartRepository`, `wishlistRepository: WishlistRepository` with one comment: injected only so they start at launch (replaces Koin `createdAtStart`).
- Delete: `di/AppModules.kt`, all Koin `*Module.kt` files (`core/network/NetworkModule.kt`, `core/session/SessionModule.kt`, `feature/*/di/*Module.kt` incl. home, signin, signup, splash, productdetails, productlist), `app/src/test/.../di/KoinModulesTest.kt`.
- Rename Task 2's `*HiltModule.kt` files/objects to `*Module.kt` (`NetworkModule`, `SessionModule`, `CartModule`, …).
- Modify `app/build.gradle.kts` + `gradle/libs.versions.toml`: remove `koin-android`, `koin-androidx-compose`, `koin-test` and the `koin` version with its comment.
- Modify `app/src/test/.../capture/ComposeCaptureTest.kt`: comments at lines ~87 and ~118 say Hilt instead of Koin (plain `Application` stays — avoids Hilt's generated app in Robolectric).

**Interfaces:**
- Consumes: Task 2 bindings.
- Produces: `AppNavDisplay(observeSession: ObserveSessionUseCase, modifier: Modifier = Modifier, navigator: AppNavigator = rememberAppNavigator())`; `ProductDetailsViewModel.Factory.create(productId: String)`; `ProductListViewModel.Factory.create(subCategoryId: String)`.

- [ ] **Step 1 (gate):** Convert `CategoriesViewModel`/`CategoriesRoute` and `ProductDetailsViewModel`/`ProductDetailsRoute` first, run `./gradlew assembleDebug`. Expected: compiles with the standard `hiltViewModel` overloads. If the assisted overload needs anything non-standard, stop and ask.
- [ ] **Step 2:** Apply the rest of the edits above.
- [ ] **Step 3:** Run `grep -rni koin app/ gradle/`. Expected: no matches.
- [ ] **Step 4:** Run `./gradlew assembleDebug testDebugUnitTest lintDebug`. Expected: BUILD SUCCESSFUL, all tests pass (ProductDetails/ProductList tests unchanged or only constructor-call edits).
- [ ] **Step 5:** Run `./gradlew assembleRelease`. Expected: BUILD SUCCESSFUL.
- [ ] **Step 6 (manual, user or `run` skill on emulator):** cold start signed-in → Splash → Home shows cart badge and favorite hearts; Categories loads; Product list; open product A, back, open product B shows B; add to cart; sign out from Account clears state and lands on Sign in; with "Don't keep activities" on, background on Product details and return → same product.
- [ ] **Step 7:** Commit (with approval): `refactor(di): move ViewModels to Hilt and remove Koin`.

### Task 4: Docs

**Files:**
- Modify: `docs/architecture.md` §5 → "Dependency injection: Hilt": choice + versions, why (teaching the Android-standard DI; compile-time graph validation replaces `KoinModulesTest`), tradeoff (annotation processing via KSP, build time, Android-only so the KMP path now needs a rethink), the `@ApplicationScope` and eager-start notes. Also update the Koin mentions at lines ~89–92 (Nav3 decorators now feed `hiltViewModel()`; drop the `koin-compose-navigation3` note), ~118 (Coil uses the Hilt-provided OkHttpClient), ~285 (R8 note: drop "Koin definitions are lambdas"; Hilt ships its own keep rules).
- Modify: `README.md` tech table DI row (Hilt 2.60.1 via KSP), package tree lines ~111–123 (`EcommerceApp`: Hilt application; no top-level `di/`; feature `di/` = Hilt module), docs table line ~208.
- Modify: `CLAUDE.md` Section B.3 DI line → `**DI:** Hilt (Android-standard; chosen for teaching DI; see docs/architecture.md §5)`.

- [ ] **Step 1:** Apply the edits.
- [ ] **Step 2:** Run `grep -rni koin README.md CLAUDE.md docs/architecture.md`. Expected: only intentional historical mentions (e.g. "replaced Koin").
- [ ] **Step 3:** Commit (with approval): `docs: record the switch from Koin to Hilt`. Include the spec and this plan.
- [ ] **Step 4:** `code-reviewer` agent on the branch, then PR via `git-expert` (with approval).
