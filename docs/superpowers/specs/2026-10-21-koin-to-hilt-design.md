# Koin → Hilt migration — design

Date: 2026-10-21 · Branch: `refactor/koin-to-hilt` (from `main`, merged back into `main`)

## Intent

The project is a teaching codebase. The instructor will teach dependency injection with **Hilt**, so `main` must
use Hilt instead of Koin. After this lands, a separate lesson branch (`lesson/categories-simple-mvvm`, out of scope
here) will rebuild one feature *without* any DI, so students can compare "Hilt does it for you" against "you do it by
hand".

Success: the whole app runs on Hilt, behaves exactly as before, every existing test passes, and a student reading any
feature sees idiomatic, standard Hilt (the patterns in developer.android.com's Hilt guide), not clever workarounds.

## Goals

- Replace Koin with Hilt across the entire app. No Koin dependency remains.
- Keep Clean Architecture exactly as it is (presentation → domain → data, use cases, repositories). Only the DI wiring
  changes.
- Keep runtime behavior identical, including the eager-start and scoping behavior listed below.
- Update `docs/architecture.md` §5, `CLAUDE.md` (Tech Stack: DI line), and `README.md` (tech table, package tree) to
  record the switch and the reason (teaching Hilt, compile-time graph validation).

## Non-goals

- No feature, UI, navigation, or architecture changes. No renaming of classes beyond what DI requires.
- No Kotlin/AGP/Compose BOM upgrade unless Hilt/KSP strictly require it (if they do, stop and explain first).
- No multi-module split, no Hilt instrumented-test setup (`HiltAndroidTest`) — the app has no instrumented DI tests
  today.
- The simple-MVVM lesson branch is a separate, later project.

## Anti-goals (would be a failure even if it compiles)

- Koin and Hilt coexisting in `main`.
- Service-locator style left behind (e.g. `EntryPointAccessors` sprinkled in composables) where constructor injection
  would do.
- Behavior drift: session/cart/wishlist no longer warming up at launch, or a ViewModel no longer keyed per product /
  sub-category id.

## Current state (Koin)

- `EcommerceApp` calls `startKoin(appModules)` and injects `OkHttpClient` (for Coil's `SingletonImageLoader.Factory`).
- `di/AppModules.kt` lists 14 modules: `core/network/NetworkModule.kt`, `core/session/SessionModule.kt`, and one
  `feature/*/di/*Module.kt` per feature (account, auth, cart, categories, home, productdetails, productlist,
  products, signin, signup, splash, wishlist).
- ViewModels are obtained with `koinViewModel()` in each `*Route.kt`; Nav3's
  `rememberViewModelStoreNavEntryDecorator()` scopes them per back-stack entry.
- `navigation/AppNavDisplay.kt` uses `koinInject<ObserveSessionUseCase>()`.
- Tests: `test/.../di/KoinModulesTest.kt` verifies the graph. `ComposeCaptureTest` runs under a plain `Application`
  to avoid starting Koin and renders stateless Screens only. ViewModel tests construct ViewModels directly with fakes
  (no DI).

## Decisions (made with the user)

1. **Whole app moves to Hilt**, in one branch, merged to `main`.
2. **Clean Architecture stays** as-is.
3. Docs are updated to reflect Hilt as the project's DI choice.

## Mapping (Claude's calls — standard Hilt idioms)

| Koin today | Hilt replacement |
|---|---|
| `startKoin` in `EcommerceApp` | `@HiltAndroidApp` on `EcommerceApp`; `@AndroidEntryPoint` on `MainActivity` |
| `EcommerceApp` `by inject<OkHttpClient>()` | `@Inject lateinit var okHttpClient: OkHttpClient` (or `dagger.Lazy<OkHttpClient>` to keep it lazy) |
| `single { … }` for third-party types (Json, OkHttpClient, Retrofit, DataStore, app `CoroutineScope`, `*Api` via `retrofit.create`) | `@Module @InstallIn(SingletonComponent::class) object` with `@Provides @Singleton` |
| `single<Repo> { RepoImpl(...) }` | `@Inject constructor` on the Impl + `@Binds @Singleton` in an abstract feature module |
| `factory { UseCase(get()) }` | `@Inject constructor` on the use case, unscoped, no module entry |
| `viewModelOf(::XViewModel)` + `koinViewModel()` | `@HiltViewModel` + `@Inject constructor` + `hiltViewModel()` |
| `viewModel { params -> …(id = params.get()) }` (ProductDetails, ProductList) + `koinViewModel(key = id) { parametersOf(id) }` | Assisted injection: `@HiltViewModel(assistedFactory = …)` + `@AssistedInject` + `hiltViewModel<VM, Factory>(key = id) { it.create(id) }` |
| `createdAtStart = true` (SessionRepository, CartRepository, WishlistRepository) | Inject these into `EcommerceApp` (field injection) so they are created in `Application.onCreate`, preserving eager start. Comment why. |
| `koinInject<ObserveSessionUseCase>()` in `AppNavDisplay` | Inject into `MainActivity` (or a small `@HiltViewModel` for the activity) and pass the session `StateFlow`/use case down as a parameter |
| Unqualified `CoroutineScope` singleton | Provide with a `@Qualifier` (e.g. `@ApplicationScope`) so it is not an ambiguous bare type |
| `KoinModulesTest` | Delete. Hilt validates the graph at compile time; `./gradlew assembleDebug` is the check. Note this in docs. |
| `ComposeCaptureTest` comment about Koin | Update the comment (reason becomes Hilt) — test logic unchanged. |

Module placement: keep the per-feature `di/` package (`feature/x/di/XModule.kt`) so the structure students already
know stays the same; it just holds a Hilt module instead of a Koin module. Features that only need `@Inject`
constructors (home, signin, signup, splash) end up with no `di/` file — that absence is itself a teaching point.

## Constraints

- Toolchain today: AGP 9.4.1 (built-in Kotlin), Kotlin 2.2.10, Compose BOM 2026.02.01, lifecycle 2.11.0, Nav3 1.2.0.
  Hilt via **KSP** (not kapt). Exact Hilt / KSP / `androidx.hilt` versions to be pinned from the compatibility
  research (latest stable that works on Kotlin 2.2.10 + AGP 9). If none does without a Kotlin/AGP bump, stop and
  ask.
- R8 is enabled for release; confirm release build still assembles (Hilt ships its own keep rules).
- Public repo: no secrets; follow CLAUDE.md commit/PR discipline (git-expert for commits, code-reviewer before PR).
- Verification gates: `./gradlew assembleDebug`, `./gradlew testDebugUnitTest`, `./gradlew assembleRelease`, and a
  manual app launch (Splash → Home → Categories → Product list → Product details → Cart) on an emulator.

## Versions (researched 2026-10-21, no Kotlin/AGP bump needed)

| Artifact | Version | Note |
|---|---|---|
| `com.google.dagger:hilt-android` / `hilt-compiler` + plugin `com.google.dagger.hilt.android` | 2.60.1 | Hilt Gradle plugin requires AGP 9.0+ since 2.59.1 |
| KSP plugin `com.google.devtools.ksp` | 2.3.12 | Supports KGP 2.2.10–2.3.x. Fallback: `2.2.10-2.0.2` |
| `androidx.hilt:hilt-lifecycle-viewmodel-compose` | 1.4.0 | `hiltViewModel()` + assisted overload; needs KGP ≥ 2.2.0, AGP ≥ 9.2, compileSdk 37 (we have 37) |

- kapt is not an option with AGP 9 built-in Kotlin, so Hilt runs on KSP.
- Unverified until the first build: KSP working under AGP 9 built-in Kotlin, and `hiltViewModel`'s `key` parameter
  on the assisted overload. Both are covered by the gate task below.
- Dagger needs JDK 17 to *run* the build (AGP 9 already does); bytecode target stays Java 11.
- Assisted values are not restored after process death (same as today's Koin parameter — the id comes back from
  the Nav3 back stack key, which is saved).

## Settled details (Claude's calls, from the builder review)

- **First task is a gate:** add Hilt + KSP to the build and convert one plain ViewModel (e.g. Categories) and one
  assisted ViewModel (ProductDetails) end-to-end under Nav3's `rememberViewModelStoreNavEntryDecorator()`. Only
  continue once `hiltViewModel()` and the assisted overload work there with no workaround. If either needs a
  non-standard factory, stop and ask the user.
- **Assisted id:** a plain `@Assisted` parameter (`productId: String`, `subCategoryId: String`), not `SavedStateHandle`
  — that matches today's Koin parameter. Editing test constructor calls to match is fine; test assertions stay the
  same.
- **Dependency cycles:** assume none (network → session; cart/wishlist → api + session; session does not depend on
  network). If Dagger reports a cycle, `Provider<T>`/`dagger.Lazy<T>` at the one offending edge is acceptable, with
  a comment saying why.
- **`@ApplicationScope`:** qualifier only, same `SupervisorJob() + Dispatchers.Default` as today, one shared
  singleton (DataStore keeps using it). No injected dispatcher.
- **Eager start:** `EcommerceApp` field-injects the three repositories by their interface types (`SessionRepository`,
  `CartRepository`, `WishlistRepository`). Context comes from `@ApplicationContext`. Verified by the manual launch
  gate (cart badge / favorites filled on first Home render), no new automated test.
- **Session for `AppNavDisplay`:** `MainActivity` field-injects `ObserveSessionUseCase` and passes it down as a
  parameter. No new `MainViewModel`.
- **CI / release:** keep the existing CI workflow's commands; only change it if the new plugins require it.
  `assembleRelease` runs unsigned locally, as today.

## Left to the builder

- File-by-file ordering of the migration and commit slicing (suggested: build setup → core modules → features →
  remove Koin → docs).
- Exact module names (`NetworkModule`, `SessionModule`, `CartModule`, …) and whether a feature's `@Binds` and
  `@Provides` share one file.
- Whether `EcommerceApp` uses `dagger.Lazy` for `OkHttpClient`.
