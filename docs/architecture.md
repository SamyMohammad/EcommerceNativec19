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
- Loading, error, empty and content are explicit states, never nullable "maybe loading" flags.

## 4. Error handling

Errors are values, and they are converted at the repository boundary:

```
Retrofit call -> safeApiCall -> AppResult.Success(data) | AppResult.Failure(AppError)
```

`safeApiCall` maps `IOException` to `AppError.Network`, `HttpException` to `AppError.Server(code)` and anything
else to `AppError.Unknown`, and rethrows `CancellationException` so structured concurrency keeps working.
ViewModels only ever see `AppResult`, never exceptions.

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
verification test (planned with the "Koin for all ViewModels" step) closes most of that gap.

**Why 4.1.x:** it is the newest Koin line built on Kotlin 2.2. Koin 4.2.x requires Kotlin 2.3, and bumping Kotlin
drags AGP and Compose compatibility with it. Revisit when the Kotlin upgrade is done on its own merits.

## 6. Navigation

Today: Navigation Compose 2.9 with string routes, one `NavHost` in `navigation/AppNavHost.kt`.

Planned: migrate to Navigation 3 (back stack as app-owned state, type-safe keys). If Nav3 hits ecosystem gaps or
tooling friction, the fallback is staying on classic Navigation Compose; the switch will not happen silently.

## 7. Networking

- Retrofit 3 + OkHttp 5 + kotlinx.serialization (compile-time, no reflection).
- The base URL is `BuildConfig.BASE_URL`, defined in `app/build.gradle.kts`, so a different environment is a build
  config change rather than a code change.
- Explicit connect, read and write timeouts on the OkHttp client.
- Debug builds log request and response bodies; release builds log nothing. The `token` and `Authorization`
  headers are redacted so credentials never reach Logcat.
- `Json { ignoreUnknownKeys = true }` and nullable DTO fields for anything the API does not guarantee.

## 8. Refactor roadmap

Planned, in rough order. Each item is a small reviewed PR.

- **Sealed `LoadState`** instead of per-screen status enums plus parallel fields.
- **Koin for all ViewModels**, replacing manual construction in the remaining screens.
- **Navigation 3** (see section 6).
- **Material 3 theme rewrite** that replaces the Figma-generated tokens with a hand-owned theme.
- **Coil 3 and image performance** (sizing, caching) once products show real images.
- **Route API per feature**, replacing sample data: products, then cart, wishlist and account.
- **Authentication** via DataStore token storage and an OkHttp interceptor that adds the `token` header. This is a
  deliberate, user-approved exception to the "no login" rule in `CLAUDE.md` section G, because the cart, wishlist
  and account endpoints require a token.
- **Release hardening:** enable R8 (currently disabled for release builds) with the keep rules the stack needs.
