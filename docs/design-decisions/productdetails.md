# Product details

## Source

- Figma node id: `620:1083`; file key `dEhOfaWrXc8wRA6mdeMecc`; version-id `2409508187813363894`.
- The version-id is owner-observed, NOT verified through the Figma MCP. Freshness is unverified.
- Code: `feature/productdetails` (`ProductDetailsScreen.kt`, `ProductDetailsImageCarousel.kt`, `ProductDetailsRoute.kt`, `ProductDetailsViewModel.kt`).
- Data: the product comes from the Route API (`feature/products`, see `docs/api/products.md`); screenshot fixtures live in `src/test/.../capture/ProductFixtures.kt`..

## Components used

- `core/ui`: `ScreenTitleBar`, `FavoriteButton`, `QuantitySelector`, `AppButton` (`Primary`, with leading cart icon), `ProductImage` (Coil `AsyncImage`), `StatusPane`, `centeredBorder`, `textLineBox`.
- Local: `ProductDetailsImageCarousel` (Compose `HorizontalPager` with indicators).

## Material defaults / token mapping

- Text `typographySource00` (title, price, headings), `typographySource15` (sold pill), `typographySource07` (rating, description, sizes), colors `colorSource12` / `colorSource21`.
- Carousel: radius `radiusSource02`, border `colorSource14`; active indicator pill `colorSource00` with `radiusSource08`.
- Sold pill radius `radiusSource00`, stroke `colorSource14`.
- Material3 `Text`; `AppButton` uses `Surface`.

## Deviations

- The "Add to cart" row is a footer pinned below the scroll area, not an in-flow button.
- The carousel shows the product's own gallery (1 to 10 images), so the indicator count follows the data.
- Total price is unit price times quantity, computed from state.
- **Size and color pickers were removed**: the API has no variants (`availableColors` is never populated), so the screen shows no option the data cannot back. The sections below the description are therefore gone and the layout after it differs from the Figma frame.
- Quantity floor of 1 is an assumption (the design shows no limit).
- "Read More" is non-interactive text inside the description.
- The search icon in the title bar has no action.
- The multi-line description is tuned 1dp up and 2dp shorter below to match the design position in the Robolectric capture. This may differ on a real device.
- The carousel overlaps the 48dp title bar band by 2dp (design y 90 versus 92).
- Footer bottom margin 104dp from the frame, which includes the design's bottom area.
- Android native status bar instead of the iOS band (see README).

## Missing contracts

- Cart state (add to cart only navigates to Cart), price calculation, stock, search.

## Automated visual result

- Official core compare after correction round 3: **FAILED, 3.24%** changed pixels (policy: `channelTolerance` 16, `maxChangedRatio` 0.03, four 20x20 corner ignores). Budget 4 per node, 2 used. Closest of the failing screens to the 3% limit.
- Attributed (agent reading, not proven) to: status bar band about 650px (0.16%), Robolectric text anti-aliasing/tracking, image resampling, dropped filters.
- An automated pass is not human approval, and this one did not pass.

## Unresolved / manual checks

- Whether 3.24% is acceptable is an owner decision; needs a human visual review.
- Description tuning on real devices and with other font scales.
- Heart shadow and the "filled heart at Small size" scaling gap noted in `FavoriteButton.kt` (filled variant exported at Regular scale only).

## Human visual review

not_run
