# Wishlist

## Source

- Figma node id: `731:1294`; file key `dEhOfaWrXc8wRA6mdeMecc`; version-id `2409508187813363894`.
- The version-id is owner-observed, NOT verified through the Figma MCP. Freshness is unverified.
- Code: `feature/wishlist` (`WishlistScreen.kt`, `WishlistRoute.kt`, `WishlistViewModel.kt`, `WishlistUiState.kt`, `WishlistSampleData.kt`).

## Components used

- `core/ui`: `SearchHeader`, `ProductRowCard`, `FavoriteButton`, `AppButton` (`PrimaryCompact`), `BottomNavigationBar` (Wishlist selected), `CoverImage`, `FractionalCropImage`, `ProductVariantDot`.
- Material3 `Scaffold` (bottom bar slot, zero content insets) with a `LazyColumn`.

## Material defaults / token mapping

- Container `colorSource02`; row text from `ProductRowCard`; variant text `colorSource12`; button `PrimaryCompact` is 36dp tall, radius `radiusSource02`, `typographySource15`.
- Dot colours from tokens (`colorSource29` / `31` / `32` / `33`).

## Deviations

- The empty state text is an addition (the design shows none).
- The heart toggles a local favorite flag; the row is not removed, so the toggle is reversible.
- `extraGapBefore` 13dp in the sample data for the third row (the design leaves 37dp instead of 24dp there).
- Small touch targets: the "Add to Cart" button is 36dp tall and the heart is 30dp (design sizes kept; below the 48dp guideline).
- The search field is local only; search icon and placeholder offsets (23.33dp / 6.17dp) are specific to this frame.
- Heart and button sit 16dp from the end, so 8dp extra end padding is added to the row slots.
- Android native status bar instead of the iOS band (see README).

## Missing contracts

- Wishlist data and persistence (three placeholder items), search, add to cart from the wishlist (the button is a no-op), removal.

## Automated visual result

- Official core compare after correction round 3: **passed, 2.69%** changed pixels (policy: `channelTolerance` 16, `maxChangedRatio` 0.03, four 20x20 corner ignores).
- Close to the 3% limit. An automated pass is not human approval.

## Unresolved / manual checks

- Heart shadow (`Paint.setShadowLayer`, API 28+) on a real device.
- Behavior of the small targets under accessibility settings.

## Human visual review

not_run
