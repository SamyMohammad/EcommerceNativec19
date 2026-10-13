# Categories

## Source

- Figma node id: `601:775`; file key `dEhOfaWrXc8wRA6mdeMecc`; version-id `2409508187813363894`.
- The version-id is owner-observed, NOT verified through the Figma MCP. Freshness is unverified.
- Code: `feature/categories` (`CategoriesScreen.kt`, `CategoriesRail.kt`, `CategoriesContent.kt`, `CategoriesRoute.kt`, `CategoriesSampleData.kt`).

## Components used

- `core/ui`: `SearchHeader`, `CategoryTile` (Square style), `BottomNavigationBar` (Categories selected), `textLineBox`, `LineBoxText`, `FigmaCropImage`.
- Local: `CategoriesRail` (left rail), `CategoriesContent` (title, `SelectedCategoryBanner`, 3-column grid).
- Layout: `Row` with a 137dp rail and a vertically scrolling content column.

## Material defaults / token mapping

- Background `colorSource02`; rail fill `colorSource22`, border `colorSource14`, selected row `colorSource02` with a primary (`colorSource00`) 7x72 indicator.
- Rail shape: `radiusSource05` on three corners, `radiusSource01` (0dp) top-end.
- Texts `typographySource15` (rail, title), `typographySource16` (banner title), `typographySource11` ("Shop Now").
- Banner radius `radiusSource05`, fill `colorSource23`.
- Material3 `Text` only.

## Deviations

- Only the Men's Fashion content exists (the design shows content for the selected entry only). Other rail entries are display-only.
- A gap slot is kept between Beauty and Cameras in the rail, as in the design.
- The rail border is drawn on four sides.
- The banner PNG already carries 80% alpha, so no extra opacity is applied.
- Banner is forced left-to-right (directional artwork).
- Label x position of each grid tile is hand-set per tile (`labelStart`) from the frame.
- Android native status bar instead of the iOS band (see README).

## Missing contracts

- Category and sub-category data (placeholders in `CategoriesSampleData.kt`); switching the selected category; search; banner "Shop Now" target.
- "Women's Fashion" rail entry leads to a node outside the 10 screens (562:469) and is not wired.
- Only "Footwear" opens Product list; other tiles are non-interactive.

## Automated visual result

- Official core compare after correction round 3: **FAILED, 3.34%** changed pixels (policy: `channelTolerance` 16, `maxChangedRatio` 0.03, four 20x20 corner ignores). Budget 4 per node, 2 used.
- Attributed (agent reading, not proven) to: status bar band about 650px (0.16%), Robolectric text anti-aliasing/tracking, image resampling, dropped filters.
- An automated pass is not human approval, and this one did not pass.

## Unresolved / manual checks

- Whether the remaining 3.34% is acceptable is an owner decision; needs a human visual review.
- Rail behavior with large fonts (rows use fixed paddings) is untested.

## Human visual review

not_run

## API integration (feat/categories-api)

- Rail and grid now come from the Route API (`docs/api/categories.md`); `CategoriesSampleData.kt` remains for previews and screenshot tests only.
- Sub-category tiles show a neutral placeholder: the API has no sub-category image.
- Per-item design tweaks that only fitted the fixed Figma content (label offsets, image crops, the empty rail gap, "only Footwear opens Product list") were dropped: tiles are uniform and all open Product list.
- The promo banner title follows the selected category; its photo stays the static Figma image until an image loader is added (category `image` URLs are available from the API).
