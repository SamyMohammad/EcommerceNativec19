# Architecture

Why the app is built the way it is, and where it is going. The [README](../README.md) is the project map (stack,
package tree, how to run); this document holds the reasoning. The data-flow diagram lives in the README's
[Architecture](../README.md#architecture) section and is not repeated here.

## 1. Overview and layers

Clean Architecture with three layers: **presentation → domain → data**. Dependencies point inward. Presentation
knows the domain, data implements domain interfaces, and the domain knows neither Android nor Retrofit.

| Layer | Owns | Does not know about |
|---|---|---|
| Presentation | Composables, `ViewModel`, `UiState` | Retrofit, DTOs, JSON |
| Domain | Models, repository interfaces, use cases | Android, Compose, HTTP |
| Data | Retrofit APIs, DTOs, mappers, repository implementations | Composables, `ViewModel` |

Use cases are the gateway between presentation and data, even when thin: a thin use case costs a few lines, while
business logic that leaks into a `ViewModel` is expensive to move later.

## 2. Package structure

Single Gradle module (`:app`), feature-first packages: `feature/<name>/{data,domain,di}` plus the screen files.
The layout mirrors the module boundaries we would extract later, so splitting is a move, not a rewrite. Shared code
(`core/`) only exists once two features need it. See the README for the full tree.

## 3. State and unidirectional data flow

- A `ViewModel` exposes one immutable `UiState` through `StateFlow`.
- The screen is stateless: it renders state and emits events upward.
- Compose collects with `collectAsStateWithLifecycle`, so collection stops when the UI is not visible.
- Loading, error, empty and content are explicit states, never nullable "maybe loading" flags. Each independently
  loading region holds one `LoadState<T>` (`Loading | Content(data) | Empty | Error(AppError)`), so contradictory
  combinations cannot be represented.

## 4. Error handling

Errors are values, and they are converted at the repository boundary:

```
Retrofit call -> safeApiCall -> AppResult.Success(data) | AppResult.Failure(AppError)
```

`safeApiCall` maps `IOException` to `AppError.Network`, `HttpException` to `AppError.Server(code)`,
`SerializationException` to `AppError.Parsing` and anything else to `AppError.Unknown`, and rethrows
`CancellationException` so structured concurrency keeps working. ViewModels only ever see `AppResult`, never
exceptions, and keep the specific `AppError` in `LoadState.Error`. The UI turns it into copy with
`AppError.toMessageRes()` (`core/ui`), so the lower layers stay free of Android resources.

## 5. Dependency injection: Koin

**Choice:** Koin 4.1.1.

**Why Koin over Hilt:**

- **Simplicity.** Modules are plain Kotlin (`module { single { ... } }`), with no component or scope boilerplate to
  learn before the first screen works.
- **No annotation processing.** Hilt needs KSP/kapt and generates code, which adds build time and one more
  toolchain to keep compatible with Kotlin and AGP. Koin has none.
- **KMP path.** Koin supports Kotlin Multiplatform, so the DI setup would survive a move of the domain and data
  layers to shared code. Hilt is Android-only.

**Tradeoff:** Koin resolves dependencies at runtime, so a missing binding fails when first requested, not at
compile time. Hilt gives compile-time graph validation. We accept that for a small app; a Koin module-graph
verification test closes most of that gap. Every ViewModel is created by Koin from a per-feature `di/` module, and
`KoinModulesTest` verifies the full production list (`appModules`).

**Why 4.1.x:** it is the newest Koin line built on Kotlin 2.2. Koin 4.2.x requires Kotlin 2.3, and bumping Kotlin
drags AGP and Compose compatibility with it. Revisit when the Kotlin upgrade is done on its own merits.

## 6. Navigation

Navigation 3 (`navigation3-runtime/ui` 1.2.0, `lifecycle-viewmodel-navigation3` 2.11.0). The back stack is
app-owned state, not a framework object you query.

- **Keys** (`navigation/AppRoutes.kt`): `@Serializable` types implementing `NavKey` (`Home`, `Cart`,
  `ProductDetails(productId)`, ...). The product id travels in the key, so there is no string parsing.
- **Per-tab back stacks** (`navigation/AppNavigator.kt`): one auth stack (Splash, Sign in, Sign up) and one stack
  per bottom-bar tab, each a `NavBackStack` from `rememberNavBackStack`, so they survive configuration change and
  process death. `AppNavigator` is plain Kotlin over `MutableList`s and is unit-tested on the JVM
  (`AppNavigatorTest`). Rules: a tab switch keeps each tab's history, reselecting a tab pops it to its root, back
  pops inside the tab, back at a non-Home tab root goes to Home, back at Home root exits. Auth success clears the
  auth stack. This replaces `saveState`/`restoreState`/`popUpTo` from classic Navigation Compose.
- **Rendering** (`navigation/AppNavDisplay.kt`): `NavDisplay` + `entryProvider`. Hidden tab stacks stay in the
  list passed to `NavDisplay` (below the current tab), otherwise Nav3 would treat them as popped and discard their
  state. Duplicate keys across stacks would collide, which the flow rules out (Cart and Product details have no
  bottom bar, so they are always the top of their stack).
- **Entry-scoped ViewModels:** `rememberSaveableStateHolderNavEntryDecorator()` and
  `rememberViewModelStoreNavEntryDecorator()` give every entry its own `ViewModelStore`, so `koinViewModel()` inside
  an entry is scoped to that entry and cleared when it is popped. Koin's own Nav3 integration
  (`koin-compose-navigation3`) is not used: it needs Koin 4.2 (Kotlin 2.3) and is experimental; the AndroidX
  decorators are enough because Koin only needs `LocalViewModelStoreOwner`.
- **Shared shell** (`navigation/AppScaffold.kt`): one `BottomNavigationBar`, shown only while the top key belongs to
  a tab (Product list highlights Categories). Tab screens no longer know about the bar.

**Tradeoffs:** Nav3 is new and has no built-in multi-stack or tab helper, so `AppNavigator` is ours to maintain.
The Android `rememberNavBackStack` restores keys through reflection (`Class.forName`), so enabling R8 later needs
keep rules for the key classes. Predictive back preview at a tab root shows the flattened stack's previous entry
rather than "Home".

## 7. Networking

- Retrofit 3 + OkHttp 5 + kotlinx.serialization (compile-time, no reflection).
- The base URL is `BuildConfig.BASE_URL`, defined in `app/build.gradle.kts`, so a different environment is a build
  config change rather than a code change.
- Explicit connect, read and write timeouts on the OkHttp client.
- Debug builds log request and response bodies; release builds log nothing. The `token` and `Authorization`
  headers are redacted so credentials never reach Logcat.
- `Json { ignoreUnknownKeys = true }` and nullable DTO fields for anything the API does not guarantee.
- Image loading: Coil 3 (`SingletonImageLoader.Factory` on `EcommerceApp`) fetches through the same Koin-provided
  `OkHttpClient`, so image requests share the API client's timeouts, logging and connection pool. Screens adopt
  `AsyncImage` as the API slices bring real image URLs; bundled artwork stays in `drawable-nodpi` as WebP, sized to
  at most 3x its largest displayed size.

## Theme

The theme is hand-owned Material 3 in `ui/theme/` (light only, no dynamic color). Screens read
`MaterialTheme.colorScheme`, `.typography` and `.shapes`; values the Material roles have no honest name for
(price strike-through, product swatches, variant dots, a few text styles, the search-field shape) live in
`AppExtendedColors` / `AppExtendedTypography` / `AppExtendedShapes`, read via `MaterialTheme.extendedColors` etc.
They are static composition locals because they never change at runtime. The token-to-role table is in
`docs/design-decisions/theme-m3.md`. The earlier Figma-generated token files were retired.

## 8. Refactor roadmap

Planned, in rough order. Each item is a small reviewed PR.

- **Route API per feature**, replacing sample data. Done: categories, product list, product details (shared
  `feature/products/`) and Home. Next: cart, wishlist and account.
- **Authentication** via DataStore token storage and an OkHttp interceptor that adds the `token` header. This is a
  deliberate, user-approved exception to the "no login" rule in `CLAUDE.md` section G, because the cart, wishlist
  and account endpoints require a token.
- **Release hardening:** enable R8 (currently disabled for release builds) with the keep rules the stack needs (including the Navigation 3 key classes).
