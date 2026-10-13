# Sign in

## Source

- Figma node id: `14:47`; file key `dEhOfaWrXc8wRA6mdeMecc`; version-id `2409508187813363894`.
- The version-id is owner-observed, NOT verified through the Figma MCP. Freshness is unverified.
- Code: `feature/signin` (`SignInScreen.kt`, `SignInRoute.kt`, `SignInViewModel.kt`, `SignInUiState.kt`).

## Components used

- `core/ui`: `AppLogo` (Hero style), `AuthTextField`, `PasswordVisibilityToggle`, `AppButton` (`OnPrimary`), `LightStatusBarIconsEffect`, `textLineBox`.
- Material3 `Text`; `AppButton` is built on Material3 `Surface`.
- Layout: a scrolling `Column` with `imePadding()` and `navigationBarsPadding()`.

## Material defaults / token mapping

- Screen background `colorSource00`; text `colorSource02` (white).
- Title `typographySource03`, subtitle `typographySource04`, "Forgot password" `typographySource05`, "Create Account" link and field labels `typographySource00`, field text/placeholder `typographySource01`.
- Field: `colorSource02` fill, radius `radiusSource02` (15dp), 64dp high, typed text `colorSource08`, placeholder `colorSource03`, cursor `colorSource00`.
- Login button: `AppButtonStyle.OnPrimary` (64dp, radius `radiusSource02`, `typographySource02`, primary text on white).
- The field is a `BasicTextField`, not a Material `TextField`; no Material text-field defaults apply.

## Deviations

- Absolute positions of the frame became vertical `Spacer` gaps; the column scrolls so it survives large fonts and the keyboard.
- "Forgot password" is plain text with no reaction (the prototype defines none).
- The same eye-slash icon is shown in both states (only that drawable was exported); the state is conveyed by the content description.
- The keyboard Done action submits (calls `onSignIn`). This is an addition.
- The Login button is offset 1dp on x (design x = 17 versus the 16dp margin).
- "Create Account" is padded to a 48dp touch target; the gap above it is reduced so the text stays at y = 763.
- Android native status bar instead of the iOS band (see README); the logo top never sits closer than 6dp to the status bar.

## Missing contracts

- Authentication, backend, validation rules and session state do not exist. `onSignIn` is invoked as-is; typed text lives only in `SignInViewModel`.
- Forgot-password flow.

## Automated visual result

- Official core compare after correction round 3: **passed, 2.84%** changed pixels (policy: `channelTolerance` 16, `maxChangedRatio` 0.03, four 20x20 corner ignores).
- Close to the 3% limit. An automated pass is not human approval.

## Unresolved / manual checks

- Behavior with keyboard open and with large font scale was designed for (scroll, `imePadding`) but not tested here.
- Password visibility toggle: the visual state cannot be told apart by icon (see deviations).
- Real-device effect of the 44dp top band.

## Human visual review

not_run
