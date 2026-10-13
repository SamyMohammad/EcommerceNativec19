# Account

## Source

- Figma node id: `835:523`; file key `dEhOfaWrXc8wRA6mdeMecc`; version-id `2409508187813363894`.
- The version-id is owner-observed, NOT verified through the Figma MCP. Freshness is unverified.
- Code: `feature/account` (`AccountScreen.kt`, `AccountFieldRow.kt`, `AccountRoute.kt`, `AccountViewModel.kt`, `AccountUiState.kt`, `AccountSampleData.kt`).

## Components used

- `core/ui`: `AppLogoBar` (logo row), `AppIconButton` (edit icon, 48dp target), `BottomNavigationBar` (Account selected), `textLineBox`.
- Local: `AccountFieldRow` (label + bordered field, switches to `BasicTextField` while editing).
- Material3 `Scaffold` (bottom bar slot, zero content insets) with a vertically scrolling `Column`.

## Material defaults / token mapping

- Container `colorSource02`; welcome and labels `typographySource00` in `colorSource12`; header e-mail `typographySource15` in `colorSource21`.
- Field: 1dp border `colorSource14`, radius `radiusSource02` (15dp), min height 54dp, value `typographySource15`, cursor `colorSource00`.
- The field is a custom bordered `Row`, not a Material `OutlinedTextField`.

## Deviations

- The edit icon turns the field editable locally (focus requested; the icon or the Done key finishes editing). The design shows only the icon.
- The password is shown as a masked placeholder of `*` characters (22 `*` characters in the sample data; `PasswordVisualTransformation` with `*` while editing). It is not a real credential.
- The header name and e-mail are static and do not follow edits of the fields.
- The edit icon target is 48dp; the field end padding is reduced by the 12dp touch inset.
- Android native status bar instead of the iOS band (see README).

## Missing contracts

- Profile data, persistence, validation and saving. Edits stay in `AccountViewModel` only.

## Automated visual result

- Official core compare after correction round 3: **passed, 2.55%** changed pixels (policy: `channelTolerance` 16, `maxChangedRatio` 0.03, four 20x20 corner ignores).
- An automated pass is not human approval.

## Unresolved / manual checks

- The editing state was not compared against any design (the design has only the idle state).
- Real-device effect of the 44dp top band.

## Human visual review

not_run
