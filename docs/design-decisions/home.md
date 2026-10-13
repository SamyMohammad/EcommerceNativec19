# Home

## Source

- Figma node id: `14:116`; file key `dEhOfaWrXc8wRA6mdeMecc`; version-id `2409508187813363894`.
- The version-id is owner-observed, NOT verified through the Figma MCP. Freshness is unverified.
- Code: `feature/home` (`HomeScreen.kt`, `HomeSections.kt`, `HomeBanners.kt`, `HomeRoute.kt`, `HomeSampleData.kt`, `HomeViewModel.kt`).

## Components used

- `core/ui`: `SearchHeader` (logo, search field, cart icon), `CategoryTile` (Circle style), `ProductCard` (Compact), `FavoriteButton`, `BottomNavigationBar` (Home selected), `textLineBox`, `LineBoxText`, `FigmaCropImage` / `FigmaCroppedImage`.
- Local: `PromoBanner`, `FootwearBanner`, `HomeSectionHeader`, `HomeCategoriesGrid`, `HomeProductRow`.
- Layout: `Column` with a `LazyColumn` body (sections) and horizontally scrolling rows (`LazyRow`, `horizontalScroll`).

## Material defaults / token mapping

- Background `colorSource02`; section titles `typographySource00` in `colorSource12`; "view all" `typographySource11`.
- Banner: radius `radiusSource02`; photo text uses `typographySource08` (22sp), `typographySource09` (30sp), `typographySource10`, `typographySource07`; "Shop Now" pill radius `radiusSource05`.
- Footwear banner: fill `colorSource18`, border `colorSource14`, offer text `typographySource12`.
- Material3 `Text` only; no Material card, chip or navigation-bar component.

## Deviations

- The promo banner is a single static slide plus the exported 3-dot artwork. The design shows a 3-dot indicator but no content for slides 2 and 3, so there is no carousel or pager.
- Controls without a prototype reaction are non-interactive: "view all", banner "Shop Now", product cards. The card "+" button has an empty callback. Only the "men's fashion" circle (to Categories), the favorite hearts (local toggle), the cart icon and the tabs react.
- Banners are forced left-to-right (artwork-bound), also in RTL locales.
- Banner texts use 22sp and 30sp while the token line height is 18sp; `textLineBox` keeps the 18sp line box and the glyphs may overflow it.
- Category grid scrolls horizontally with the 4th column cut at the screen edge as in the design. Photo widths/offsets of a few tiles are hand-placed from the frame.
- Android native status bar instead of the iOS band (see README).

## Missing contracts

- Search (the field is a real text field, but nothing filters), add to cart ("+"), product details from Home, "view all", banner and "Shop Now" targets, all data (placeholders in `HomeSampleData.kt`).
- Reactions to nodes outside the 10 screens (for example Women's fashion to 564:542) are not implemented.

## Automated visual result

- Official core compare after correction round 3: **FAILED, 4.15%** changed pixels (policy: `channelTolerance` 16, `maxChangedRatio` 0.03, four 20x20 corner ignores). Budget 4 per node, 2 used.
- Attributed (agent reading, not proven) to: status bar band about 650px (0.16%), Robolectric text anti-aliasing/tracking, image resampling, dropped filters.
- An automated pass is not human approval, and this one did not pass.

## Unresolved / manual checks

- Whether the remaining 4.15% is acceptable is an owner decision; needs a human visual review.
- Heart shadow on the product cards (`Paint.setShadowLayer`, API 28+) is not compared on devices.
- RTL: only banners are forced LTR; the rest was not verified in RTL.

## Human visual review

not_run
