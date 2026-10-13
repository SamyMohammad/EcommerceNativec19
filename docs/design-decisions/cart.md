# Cart

## Source

- Figma node id: `604:890`; file key `dEhOfaWrXc8wRA6mdeMecc`; version-id `2409508187813363894`.
- The version-id is owner-observed, NOT verified through the Figma MCP. Freshness is unverified.
- Code: `feature/cart` (`CartScreen.kt`, `CartRoute.kt`, `CartViewModel.kt`, `CartUiState.kt`); data and domain in `feature/cart/data` and `domain` (see `docs/api/cart.md`, `docs/architecture.md` section 11).

## Components used

- `core/ui`: `ScreenTitleBar`, `ProductRowCard` (398x113 row), `QuantitySelector`, `AppButton` (`Primary`, trailing arrow icon), `ProductImage`, `StatusPane`, `formatPrice`, `textLineBox`.
- Layout: `Column` with a `LazyColumn` of rows and a footer.

## Material defaults / token mapping

- Texts `typographySource00`; label colour `colorSource21`, price colour `colorSource12`.
- "Check Out" button radius `radiusSource00` (20dp), 48dp tall, max 270dp wide.
- Material3 `Text`; `AppButton` uses `Surface`.

## Deviations

- Loading, error (with retry) and empty states are additions (the design shows none). The footer is shown only when there are rows.
- The row image is the product `imageCover`, cropped to fill; the variant dot, colour and size are gone (the API has no variants), so the row shows title, price and quantity only.
- The row price is the line total (unit price x quantity) so rows add up to the footer total; the footer total is the server's `totalCartPrice`.
- "Add to cart" on Details adds the selected quantity on top of what the cart already holds, then navigates to Cart (the prototype transition). The add keeps running if the user leaves; if it fails the Cart simply does not show the item. The "+" on Home / Product list and the Wishlist row button add one unit and stay on their screen.
- The title bar search and cart icons do nothing (the cart icon is already on Cart).
- The footer bottom margin is 98dp (button bottom y 834 to frame bottom 932).
- The delete icon has a 48dp touch target growing inward, so the icon stays in the design position.
- Android native status bar instead of the iOS band (see README).

## Missing contracts

- Checkout ("Check Out" is a no-op; payments are out of scope), search.
- Decrementing the last unit removes the row (a count of 0 cannot exist in the API); removal is also available by the delete icon.
- No maximum quantity (the API exposes stock only on some answers).
- Back pops to the previous screen (the prototype always leads to Home).

## Automated visual result

- Official core compare after correction round 3: **passed, 1.46%** changed pixels (policy: `channelTolerance` 16, `maxChangedRatio` 0.03, four 20x20 corner ignores).
- An automated pass is not human approval.

## Unresolved / manual checks

- The empty state, loading, error and multiple rows were not compared against any design.
- The compare above predates the Route API slice: the captured row now shows the product image fixture and no variant line, so the pixel result is no longer measured.
- A failed count change or add has no message (the row snaps back / nothing happens).
- Real-device effect of the 44dp top band.

## Human visual review

not_run
