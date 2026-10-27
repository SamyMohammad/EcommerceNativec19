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
  loading region holds one `UiState<T>` (`Loading | Success(data) | Empty | Error(AppError)`), so contradictory
  combinations cannot be represented.

## 4. Error handling

Errors are values, and they are converted at the repository boundary:

```
Retrofit call -> safeApiCall -> AppResult.Success(data) | AppResult.Failure(AppError)
```

`safeApiCall` maps `IOException` to `AppError.Network`, `HttpException` to `AppError.Server(code)`,
`SerializationException` to `AppError.Parsing` and anything else to `AppError.Unknown`, and rethrows
`CancellationException` so structured concurrency keeps working. ViewModels only ever see `AppResult`, never
exceptions, and keep the specific `AppError` in `UiState.Error`. The UI turns it into copy with
`AppError.toMessageRes()` (`core/ui`), so the lower layers stay free of Android resources.

## 5. Dependency injection: Hilt

**Choice:** Hilt 2.60.1 through KSP 2.3.12, with `androidx.hilt:hilt-lifecycle-viewmodel-compose` 1.4.0.
KSP is required because kapt is incompatible with AGP 9 built-in Kotlin; existing Kotlin, AGP and Compose versions
stay unchanged.

**Why:** this teaching project uses Android-standard DI so students learn constructor injection, scopes and a
compile-time dependency graph. Hilt's compile-time graph validation replaces the former `KoinModulesTest`;
`./gradlew assembleDebug` now checks that the production graph compiles.

Repositories and use cases use `@Inject constructor`. Per-feature `di/` modules bind repository interfaces with
`@Binds @Singleton` and provide Retrofit APIs with `@Provides @Singleton`. Third-party core types live in
`NetworkModule` and `SessionModule`, installed in `SingletonComponent`. Features that only inject ViewModels
(home, signin, signup, splash, productdetails and productlist) need no DI module file.

ViewModels use `@HiltViewModel` and Routes resolve them through `hiltViewModel()`. Product details and Product list
use assisted factories for the opaque id from the Nav3 key, retaining `key = productId` / `key = subCategoryId` so
each id keeps its own state. After process death, the restored Nav3 key supplies that id again. `MainActivity`
injects `ObserveSessionUseCase` and passes it to `AppNavDisplay`.

`@ApplicationScope` qualifies one singleton `CoroutineScope(SupervisorJob() + Dispatchers.Default)`, shared by
DataStore, session loading, cart/wishlist observation and 401 handling. `EcommerceApp` field-injects
`SessionRepository`, `CartRepository` and `WishlistRepository` only to create them at launch: the session begins
loading and the cart/favorites caches begin observing before screens ask. Coil receives a `dagger.Lazy<OkHttpClient>`
that resolves the same shared client.

**Tradeoff:** KSP annotation processing generates code and adds build time and a toolchain compatibility concern.
Hilt is Android-only, so moving domain/data into Kotlin Multiplatform would require rethinking the DI boundary.

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
  auth stack; `signOut()` clears every tab stack and shows Sign in. This replaces `saveState`/`restoreState`/`popUpTo` from classic Navigation Compose.
- **Rendering** (`navigation/AppNavDisplay.kt`): `NavDisplay` + `entryProvider`. Hidden tab stacks stay in the
  list passed to `NavDisplay` (below the current tab), otherwise Nav3 would treat them as popped and discard their
  state. Duplicate keys across stacks would collide, which the flow rules out (Cart and Product details have no
  bottom bar, so they are always the top of their stack).
- **Entry-scoped ViewModels:** `rememberSaveableStateHolderNavEntryDecorator()` and
  `rememberViewModelStoreNavEntryDecorator()` give every entry its own `ViewModelStore`, so `hiltViewModel()` inside
  an entry is scoped to that entry and cleared when it is popped. The AndroidX decorators supply its
  `LocalViewModelStoreOwner`; assisted factories receive the id from the navigation key.
- **Shared shell** (`navigation/AppScaffold.kt`): one `BottomNavigationBar`, shown only while the top key belongs to
  a tab (Product list highlights Categories). Tab screens no longer know about the bar.

**Tradeoffs:** Nav3 is new and has no built-in multi-stack or tab helper, so `AppNavigator` is ours to maintain.
The Android `rememberNavBackStack` restores keys through reflection (`Class.forName`), so the release build keeps the
key classes (section 13). Predictive back preview at a tab root shows the flattened stack's previous entry
rather than "Home".

## 7. Networking

- Retrofit 3 + OkHttp 5 + kotlinx.serialization (compile-time, no reflection).
- The base URL is `BuildConfig.BASE_URL`, defined in `app/build.gradle.kts`, so a different environment is a build
  config change rather than a code change.
- Explicit connect, read and write timeouts on the OkHttp client.
- Debug builds log request and response bodies; release builds log nothing. The `token` and `Authorization`
  headers are redacted so credentials never reach Logcat. `SensitivePathLoggingInterceptor` routes `/auth/`
  requests to a headers-only logger, because their bodies carry the password (request, and echoed back in 400
  responses) and the token (success responses).
- Interceptor order: `AuthInterceptor` first (adds the `token` header, so the logger sees and redacts it), then
  logging. `AuthInterceptor` reads the token from `SessionRepository.state.value`, an in-memory `StateFlow`, so
  OkHttp threads never wait for disk. On a 401 to a request that carried a token it calls
  `SessionRepository.invalidate(token)`, which signs out only if that token is still current (a late 401 for an old
  token must not end a newer session). Sign-in and sign-up never carry the token, so their 401 ("wrong password")
  cannot sign anyone out.
- `Json { ignoreUnknownKeys = true }` and nullable DTO fields for anything the API does not guarantee.
- Image loading: Coil 3 (`SingletonImageLoader.Factory` on `EcommerceApp`) fetches through the same Hilt-provided
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
  `feature/products/`), Home and wishlist (section 10), cart (section 11), account (section 12). Every screen is now on the API.
- **Authentication.** Done: sign-in, sign-up, encrypted session, `token` header, sign-out on 401 (section 9). This
  is a deliberate, user-approved exception to the "no login" rule in `CLAUDE.md` section G, because the cart,
  wishlist and account endpoints require a token. Profile editing and password change arrived with the account slice (section 12). Still no password reset or social login.
- **Release hardening.** Done: R8 and resource shrinking on, keep rules, fixture-only drawables moved to
  `src/debug`, GitHub Actions CI (section 13). The roadmap is complete.

## 9. Authentication and session

Authentication is the one place the app stores something secret, so the design is deliberate.

**Layers.** `feature/auth/` follows the usual split. `AuthApi` (Retrofit, returns `Response<T>` so the repository can
read 400/401/409 error bodies) and `AuthRepositoryImpl` in `data/`; `AuthRepository`, `SignInUseCase`,
`SignUpUseCase`, `SignOutUseCase`, `ObserveSessionUseCase` and the validation rules in `domain/`. The token itself is
owned by `core/session/` because the network layer and every later feature (cart, wishlist, account) need it.

**Session storage.** `SessionRepository` exposes `state: StateFlow<SessionState>` with `Loading`, `SignedOut` and
`SignedIn(token)`. It is backed by Preferences DataStore (`files/datastore/session.preferences_pb`) holding the token
encrypted by `TokenCipher`; the production implementation is AES-256/GCM with a non-exportable Android Keystore key
(`androidx.security:security-crypto` is deprecated, and the platform API is about 40 lines). The state is derived from
the store with `stateIn(Eagerly)` and the repository is created at app start, so the cache is warm when Splash or
OkHttp ask. `Loading` exists so Splash can wait for the disk read instead of guessing "signed out". `SignedIn`
overrides `toString`, and the request DTOs and UI states hide passwords the same way, so a stray log line or failing
assertion cannot print a secret.

**Failure modes.** A stored blob that cannot be decrypted (key lost after a backup restore, Keystore reset) is treated
as signed out and deleted. If the token cannot be stored after a successful sign-in, the user sees a generic error
rather than a silently half-signed-in app.

**Backup.** `allowBackup` stays on, but the DataStore file is excluded in `backup_rules.xml` (Android 11 and older)
and in `data_extraction_rules.xml` (Android 12+, cloud backup and device transfer). Keystore keys are never backed
up, so a restored blob would be unreadable anyway; excluding it keeps credentials out of backups altogether, and
leaves the option to back up harmless preferences later. If a second DataStore file appears, decide per file.

**Errors.** Failures the UI words differently get their own case in `AuthError` (`InvalidCredentials` for 401,
`EmailAlreadyUsed` for 409, `Validation(message)` for 400); everything else is `Other(AppError)` and reuses the shared
mapping. It is a separate type rather than new `AppError` cases because `AppError` is shared by every feature and
these only mean something for credentials. Use cases return `AuthOutcome`: `Success`, `Invalid(fieldErrors)` (the
client-side check failed and nothing was sent) or `Failed(AuthError)`. Domain results carry no Android resources; the
UI maps them to strings in `feature/auth/ui/AuthMessages.kt`.

**Validation.** `AuthValidation` mirrors the server (docs/api/auth.md): name at least 3 characters, valid email,
password at least 6 characters (no complexity rule), Egyptian mobile `01[0125]` plus 8 digits. The server reports only
the first failing field; the client reports all of them. The design has no confirm-password field, so the data layer
sends the password as `rePassword` and the UI offers the show/hide toggle instead.

**One-shot "signed in" effect.** Modeled as state (`SignInUiState.signedIn`, `SignUpUiState.signedUp`) observed by the
route in a `LaunchedEffect`, following the official guidance that UI events that change state should be state, not
`Channel`/`SharedFlow` events that can be lost or replayed. Leaving the screen removes its ViewModel, so there is no
flag to reset.

**Navigation reaction.** Splash waits for the first non-`Loading` session (after its 0.4 s delay) and goes to Home or
Sign in. `AppNavDisplay` collects the session with `collectAsStateWithLifecycle`; when it turns `SignedOut` while the
user is past the auth screens (sign-out, or a 401 handled by `AuthInterceptor`), it calls `AppNavigator.signOut()`.

**Not covered by JVM tests.** The Keystore cipher needs a device; `DataStoreSessionRepositoryTest` uses a fake cipher
and an in-memory DataStore (the file-backed one cannot rewrite its file on Windows hosts), plus one real-file smoke test.

## 10. Favorites: one source of truth

Before this slice each screen with a heart kept its own copy (Home, Product list and Details each held a local set),
so a heart tapped on Details was empty again on Home. Now `WishlistRepository.favoriteIds: StateFlow<Set<String>>` is
the only owner. Home, Product list, Details and the Wishlist screen observe it through `ObserveFavoriteIdsUseCase` and
change it through `ToggleFavoriteUseCase`; none of them keeps a favorite flag of its own.

- **Scope.** `WishlistRepositoryImpl` is an app-scoped singleton (created at launch by `EcommerceApp` injection, using the process-wide
  `@ApplicationScope` `CoroutineScope` from `SessionModule`). A ViewModel-scoped cache would start empty on every screen.
- **Lifecycle follows the session.** The repository observes `SessionRepository.state`: becoming `SignedIn` triggers one
  `GET /wishlist` (so hearts are right before any screen asks); `SignedOut` clears the cache so the next account never
  sees the previous one's favorites. A failed background load is silent; the Wishlist screen reloads on every visit.
- **Optimistic toggle with rollback.** `toggle` flips the id immediately, sends POST or DELETE, and flips it back on
  failure. The request runs `NonCancellable` so leaving the screen right after the tap cannot strand the cache in a
  state the server never saw.
- **Double taps.** One request per product at a time: a second tap on a product that still has a request in flight is
  dropped (reported as success). Queuing instead would let two requests race on the server and leave two rollbacks
  competing over one id. A version counter also stops a `GET` that started before a toggle from overwriting it.
- **Wishlist screen.** It loads the full products once per visit and renders them filtered by `favoriteIds`, so
  un-hearting a row removes it instantly and a failed request brings it back with no extra bookkeeping.
- **Known gap.** A failed toggle is visible only as the heart flipping back; there is no snackbar yet.

Details of the endpoints: [`docs/api/wishlist.md`](api/wishlist.md).

## 11. Cart: server totals, optimistic counts

`CartRepository.cart: StateFlow<Cart?>` is the only owner of cart data (null = not loaded / signed out). Details,
Home, Product list and Wishlist put products in through `AddToCartUseCase`; the Cart screen observes it
(`ObserveCartUseCase`), refreshes on every visit (`RefreshCartUseCase`) and changes it through
`UpdateCartItemCountUseCase` (a count below 1 means remove) and `RemoveFromCartUseCase`. Same scope and session
lifecycle as the wishlist (section 10): app-scoped, created at launch, loaded on sign-in, wiped on sign-out.

- **The server owns the numbers.** Every endpoint answers with the whole cart, and that answer replaces the cache. The
  app sums prices itself only while a tap is unconfirmed.
- **Display = confirmed + pending.** The repository keeps `confirmed` (the last server cart) and `pending` (counts the
  user asked for, 0 = remove) and renders `cart` from the two. A server answer replaces `confirmed` without undoing
  taps on other products, and a rollback is just dropping a pending entry.
- **Coalesce, don't drop.** Unlike a heart, a quantity tap means "one more": ten quick taps are 10, so dropping taps
  while a request is out would lose nine. One worker per product sends the latest pending count, then checks whether
  the user asked for something newer meanwhile and sends that. Only the first caller waits for the network; the rest
  return success at once. A failure rolls the product back to its confirmed count (including the coalesced taps).
- **Add is not optimistic.** `POST /cart` adds one unit and answers with bare product ids, so the new line has no
  title or image yet. Add is POST, then PUT (when more than one unit was wanted) or GET, and the populated answer
  replaces the cache. A per-product `Mutex` stops an add and a count worker from overlapping on the wire.
- **Stale responses.** An epoch (bumped when the session changes) discards answers that belong to a previous account;
  a mutation counter stops a `GET` that started before a change from overwriting it. Requests run `NonCancellable`.
- **Known gaps.** A failed change is visible only as the row snapping back (no snackbar); a failed "add" shows nothing;
  if the second step of an add fails the cache is resynced with a background GET.

Details of the endpoints: [`docs/api/cart.md`](api/cart.md); screen notes: `docs/design-decisions/cart.md`.

## 12. Account: profile from the server, nothing stored

`feature/account/` has `AccountApi` and `AccountRepositoryImpl` in `data/`, and `GetProfileUseCase`,
`UpdateProfileUseCase`, `ChangePasswordUseCase` plus the `Profile` model in `domain/`. Endpoints and quirks:
[`docs/api/account.md`](api/account.md); screen notes: `docs/design-decisions/account.md`.

- **Profile source: `GET /users/getMe`, no local copy.** The sign-in response and the token only carry a name, so the
  first plan was to persist the profile next to the session. `getMe` made that unnecessary: one call returns name,
  e-mail, phone and the embedded addresses. The profile therefore lives only in `AccountViewModel`, so no personal data
  is written to disk and none can outlive the signed-in user (compare the wishlist and cart caches, which need an
  explicit wipe on sign-out). That is why the use case is `GetProfileUseCase`, not an "observe" one.
- **Send only what changed.** The server answers an unchanged e-mail with 400 "already in use", so `UpdateProfileUseCase`
  diffs the edit against the loaded profile, validates only the changed fields (a stored `+20` phone is valid for the
  server but not for `AuthValidation`) and skips the request when nothing changed.
- **Password change replaces the token.** `PUT /users/changeMyPassword` invalidates the old token the moment it
  succeeds and returns a new one; the repository stores it before reporting success. The server also accepts a
  3-character new password here, so `ChangePasswordUseCase` is the only length guard.
- **One address line over a list.** The design shows one address; the API keeps many. Editing adds the new address
  and then removes the previous ones. This is not atomic; a failed removal is ignored because the screen shows the
  last address anyway.
- **Failures reuse `AuthError`.** Server 400 text and transport errors are already worded by `AuthMessages`.
- **Logging.** `SensitivePathLoggingInterceptor` now covers `/users/` as well as `/auth/`: `getMe` returns the stored
  password hash and `changeMyPassword` carries both passwords and a token, so those paths log headers only.
- **Sign out** is `SignOutUseCase`; the signed-out session already resets navigation and wipes the wishlist and cart.
- **Known gaps.** A request that is in flight with the old token while the password changes can get a 401 and sign the
  user out (narrow race). No snackbar for a failed save beyond the inline message.

## 13. Release build (R8) and CI

`release` uses AGP 9's `optimization { enable = true }` plus `isShrinkResources = true`; the unsigned APK went from
about 12.0 MB (optimization off) to about 2.5 MB. AGP 9 reads keep rules from `app/src/main/keepRules/`, and the only
project rule in `rules.keep` is:

- `-keep class * implements androidx.navigation3.runtime.NavKey`. Navigation 3 restores the back stack by resolving each
  key's class by name via reflection, so renaming or removing the route classes would crash restoration after process
  death or rotation. It is limited to the handful of route classes, not a blanket rule.

Everything else is covered by consumer rules that ship inside the libraries, so we deliberately add nothing for them:
kotlinx.serialization keeps `Companion` / `serializer()` / `$$serializer` of `@Serializable` DTOs, and Retrofit keeps
service interfaces with their generic and annotation metadata. Hilt ships its own consumer keep rules.
After changing dependencies, rebuild with `./gradlew assembleRelease` and check `app/build/outputs/mapping/release/`
(`mapping.txt`, `usage.txt`, `configuration.txt`).

Images that only test fixtures use live in `app/src/debug/res/drawable-nodpi`, so unit tests still see them but the
release APK does not ship them.

CI (`.github/workflows/android.yml`) runs on pushes to `main` and on pull requests: `assembleDebug`,
`testDebugUnitTest`, `lintDebug`, then `assembleRelease`, with read-only permissions and no secrets.
