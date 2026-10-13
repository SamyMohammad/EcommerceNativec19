# Account

## Source

- Figma node id: `835:523`; file key `dEhOfaWrXc8wRA6mdeMecc`; version-id `2409508187813363894`.
- The version-id is owner-observed, NOT verified through the Figma MCP. Freshness is unverified.
- Code: `feature/account` (`AccountScreen.kt`, `AccountFieldRow.kt`, `ChangePasswordDialog.kt`, `AccountRoute.kt`, `AccountViewModel.kt`, `AccountUiState.kt`, plus `data/` and `domain/`). Data and API notes: `docs/api/account.md`, `docs/architecture.md` section 12.

## Components used

- `core/ui`: `AppLogoBar` (logo row), `AppIconButton` (edit icon, 48dp target), `BottomNavigationBar` (Account selected), `StatusPane` (loading / error / retry), `AppButton` (Sign out), `textLineBox`.
- Local: `AccountFieldRow` (label + bordered field, switches to `BasicTextField` while editing), `ChangePasswordDialog`.
- Material3 `Scaffold` (bottom bar slot, zero content insets), a scrolling `Column` for the profile content, `AlertDialog` for the password change.

## Material defaults / token mapping

- Container `colorSource02`; welcome and labels `typographySource00` in `colorSource12`; header e-mail `typographySource15` in `colorSource21`.
- Field: 1dp border `colorSource14`, radius `radiusSource02` (15dp), min height 54dp, value `typographySource15`, cursor `colorSource00`.
- The field is a custom bordered `Row`, not a Material `OutlinedTextField`. Errors use `colorScheme.error` in `bodySmall` under the field.

## Data mapping

- Header: "Welcome, <first word of the name>" and the e-mail; they follow saved edits.
- Fields: name, e-mail, mobile and address come from `GET /users/getMe`; the address row shows the newest saved address line. Empty values render as an empty field.
- Password row: a fixed 22-character mask. The server never returns the password.

## Deviations

- **Sign out button (not in the design).** A primary `AppButton` below the last field. Needed so the new authentication is usable; navigation already resets on the signed-out session.
- **Edit flow (the design shows only the pencil).** The pencil turns the field editable (focus requested); the pencil again or the Done key saves. While saving the pencils are disabled. Validation and server errors show under the field, and editing stays open until the save succeeds.
- **Password row opens a dialog** (current + new password) because the API needs the current password, which a single inline field cannot collect. Both fields are masked; no show/hide toggle.
- Loading and error (retry) states are added; the design has only the loaded state.
- The mobile placeholder in the screenshot is `01000000000` (the earlier sample used a `+20` format the API's own rule does not accept).
- `design/figma/source-evidence/account/reference.png`: the personal data in the original export (name, e-mail, phone, address) was replaced with placeholders for privacy (`Jane Doe`, `jane.doe@example.com`, `01000000000`, `123 Example Street, Cairo`, Poppins at the original size and weight). It is not a Figma re-export; the layout is unchanged.
- The edit icon target is 48dp; the field end padding is reduced by the 12dp touch inset.
- Android native status bar instead of the iOS band (see README).

## Missing contracts

- No endpoint to delete an account or to clear an address through this flow (a blank address is rejected client-side).
- Multiple addresses are supported by the API but not by the design; only one line is shown.

## Automated visual result

- The earlier official core compare passed (2.55% changed pixels; policy `channelTolerance` 16, `maxChangedRatio` 0.03). It was **not re-run** for this slice: the new Sign out button below the fields and the real-data strings change the frame, so the old result no longer applies. The candidate PNG was inspected by eye only.
- An automated pass is not human approval.

## Unresolved / manual checks

- The editing, error, loading and dialog states were not compared against any design.
- Real-device effect of the 44dp top band.
- `design/figma/source-evidence/account/reference.png` is a binary Figma export that still shows the original sample profile (a realistic name, e-mail, phone and address). It is hash-bound by `reference-approval.json`, so it was left unchanged and should be replaced by a placeholder export.

## Human visual review

not_run
