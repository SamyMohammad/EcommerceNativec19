# Sign up

## Source

- Figma node id: `9:97`; file key `dEhOfaWrXc8wRA6mdeMecc`; version-id `2409508187813363894`.
- The version-id is owner-observed, NOT verified through the Figma MCP. Freshness is unverified.
- Code: `feature/signup` (`SignUpScreen.kt`, `SignUpRoute.kt`, `SignUpViewModel.kt`, `SignUpUiState.kt`).

## Components used

- `core/ui`: `AppLogo` (Hero style), `AuthTextField` (x4), `PasswordVisibilityToggle`, `AppButton` (`OnPrimary`), `LightStatusBarIconsEffect`.
- Layout: a scrolling `Column` with `imePadding()` and `navigationBarsPadding()`.

## Material defaults / token mapping

- Same auth styling as Sign in: background `colorSource00`, labels `typographySource00` in `colorSource02`, field fill `colorSource02`, radius `radiusSource02`, text `typographySource01`.
- Submit button `AppButtonStyle.OnPrimary`.
- Fields are `BasicTextField`, not Material `TextField`.

## Deviations

- Absolute positions became gaps (logo top y 85, 32dp between fields, 56dp before the button); the column scrolls.
- Keyboard options per field: capitalization words (name), phone, email, password. The Done key on the password field submits. These are additions.
- Same eye-slash icon in both password states.
- Android native status bar instead of the iOS band (see README).

## Missing contracts

- Registration, validation (name, phone, email, password rules), backend and session state. `onSignUp` only navigates to Home.

## Automated visual result

- Official core compare after correction round 3: **passed, 2.31%** changed pixels (policy: `channelTolerance` 16, `maxChangedRatio` 0.03, four 20x20 corner ignores).
- An automated pass is not human approval.

## Unresolved / manual checks

- Scroll behavior with keyboard open and large font scale: not tested here.
- Real-device effect of the 44dp top band.

## Human visual review

not_run
