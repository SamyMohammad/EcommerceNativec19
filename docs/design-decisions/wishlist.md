# Wishlist

## Source

- Figma node id: `731:1294`; file key `dEhOfaWrXc8wRA6mdeMecc`; version-id `2409508187813363894`.
- The version-id is owner-observed, NOT verified through the Figma MCP. Freshness is unverified.
- Code: `feature/wishlist` (`WishlistScreen.kt`, `WishlistRoute.kt`, `WishlistViewModel.kt`, `WishlistUiState.kt`); data and domain in `feature/wishlist/data` and `domain` (see `docs/api/wishlist.md`).

## Components used

- `core/ui`: `SearchHeader`, `ProductRowCard`, `FavoriteButton`, `AppButton` (`PrimaryCompact`), `BottomNavigationBar` (Wishlist selected), `CoverImage`, `FractionalCropImage`, `ProductVariantDot`.
- Material3 `Scaffold` (bottom bar slot, zero content insets) with a `LazyColumn`.

## Material defaults / token mapping

- Container `colorSource02`; row text from `ProductRowCard`; variant text `colorSource12`; button `PrimaryCompact` is 36dp tall, radius `radiusSource02`, `typographySource15`.
- Dot colours from tokens (`colorSource29` / `31` / `32` / `33`).

## Deviations

- The empty state text is an addition (the design shows none).
- The heart removes the row (optimistically, via the Route API) and a failed request restores it; the design has no removal behavior.
- Rows are uniform 24dp apart (the design leaves 37dp before the third row), and the variant dot and label are gone: the API has no variants.
- The row image is the product `imageCover` URL, cropped to fill, instead of the design artwork.
- Small touch targets: the "Add to Cart" button is 36dp tall and the heart is 30dp (design sizes kept; below the 48dp guideline).
- The search field is local only; search icon and placeholder offsets (23.33dp / 6.17dp) are specific to this frame.
- Heart and button sit 16dp from the end, so 8dp extra end padding is added to the row slots.
- Android native status bar instead of the iOS band (see README).

## Missing contracts

- Search from the wishlist. "Add to Cart" adds one unit through the cart API and stays on the screen (no confirmation message). Loading and error states are additions.

## Automated visual result

- Official core compare after correction round 3: **passed, 2.69%** changed pixels (policy: `channelTolerance` 16, `maxChangedRatio` 0.03, four 20x20 corner ignores).
- Close to the 3% limit. An automated pass is not human approval.

## Unresolved / manual checks

- Heart shadow (`Paint.setShadowLayer`, API 28+) on a real device.
- Behavior of the small targets under accessibility settings.

## Human visual review

not_run
