# Cart

## Source

- Figma node id: `604:890`; file key `dEhOfaWrXc8wRA6mdeMecc`; version-id `2409508187813363894`.
- The version-id is owner-observed, NOT verified through the Figma MCP. Freshness is unverified.
- Code: `feature/cart` (`CartScreen.kt`, `CartRoute.kt`, `CartViewModel.kt`, `CartUiState.kt`, `CartSampleData.kt`).

## Components used

- `core/ui`: `ScreenTitleBar`, `ProductRowCard` (398x113 row), `QuantitySelector`, `AppButton` (`Primary`, trailing arrow icon), `ProductVariantDot`, `textLineBox`.
- Layout: `Column` with a `LazyColumn` of rows and a footer.

## Material defaults / token mapping

- Texts `typographySource00`; label colour `colorSource21`, price colour `colorSource12`; variant dot colours from tokens (`colorSource29` / `31` / `32` / `33`).
- "Check Out" button radius `radiusSource00` (20dp), 48dp tall, max 270dp wide.
- Material3 `Text`; `AppButton` uses `Surface`.

## Deviations

- The empty state text is an addition (the design shows none). The footer is hidden when the list is empty.
- The title bar search and cart icons do nothing (the cart icon is already on Cart).
- The footer bottom margin is 98dp (button bottom y 834 to frame bottom 932).
- The delete icon has a 48dp touch target growing inward, so the icon stays in the design position.
- Android native status bar instead of the iOS band (see README).

## Missing contracts

- Cart data and persistence (one placeholder item), checkout ("Check Out" is a no-op), search.
- The total price is not recomputed when quantity changes or rows are removed.
- Quantity floor of 1 is an assumption; removal is by the delete icon only.
- Back always leads to Home (prototype).

## Automated visual result

- Official core compare after correction round 3: **passed, 1.46%** changed pixels (policy: `channelTolerance` 16, `maxChangedRatio` 0.03, four 20x20 corner ignores).
- An automated pass is not human approval.

## Unresolved / manual checks

- The empty state and multiple rows were not compared against any design.
- Real-device effect of the 44dp top band.

## Human visual review

not_run
