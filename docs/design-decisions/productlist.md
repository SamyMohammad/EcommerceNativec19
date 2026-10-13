# Product list

## Source

- Figma node id: `726:650`; file key `dEhOfaWrXc8wRA6mdeMecc`; version-id `2409508187813363894`.
- The version-id is owner-observed, NOT verified through the Figma MCP. Freshness is unverified.
- Code: `feature/productlist` (`ProductListScreen.kt`, `ProductListRoute.kt`, `ProductListSampleData.kt`, `ProductListViewModel.kt`).

## Components used

- `core/ui`: `SearchHeader`, `ProductCard` (Regular size), `FavoriteButton`, `BottomNavigationBar`.
- Material3 `Scaffold` (bottom bar slot, zero content insets), `LazyVerticalGrid` with 2 fixed columns.

## Material defaults / token mapping

- `Scaffold` container uses `MaterialTheme.colorScheme.background`, which the generated theme maps to `colorSource02`.
- Card text and colors come from the `ProductCard` tokens (card 191dp wide, 2dp border, 128dp image, 30dp heart); grid gaps 16dp.
- Card height 237dp is used as a minimum so larger fonts can grow the card.

## Deviations

- The card "+" button has no callback behavior (the route passes an empty lambda).
- Search text does not filter the list.
- The bottom bar highlights Categories (the design's selected circle is on the second tab here), even though the list is not a tab destination.
- Card images are cropped with `ContentScale.Crop`.
- Android native status bar instead of the iOS band (see README).

## Missing contracts

- Product data layer (six placeholder products in `ProductListSampleData.kt`, opaque string ids), search and filtering, add to cart, favorites persistence.
- Product details are the same for every id (see Product details).

## Automated visual result

- Official core compare after correction round 3: **FAILED, 4.60%** changed pixels (policy: `channelTolerance` 16, `maxChangedRatio` 0.03, four 20x20 corner ignores). Budget 4 per node, 2 used. This is the highest of the 10 screens.
- Attributed (agent reading, not proven) to: status bar band about 650px (0.16%), Robolectric text anti-aliasing/tracking, image resampling, dropped filters.
- An automated pass is not human approval, and this one did not pass.

## Unresolved / manual checks

- Whether the remaining 4.60% is acceptable is an owner decision; needs a human visual review.
- Two-line placeholder titles with large fonts: untested.

## Human visual review

not_run
