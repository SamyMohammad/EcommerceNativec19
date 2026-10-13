# Theme: Material 3 mapping

The app theme is hand-owned (`app/src/main/java/com/example/ecommercenativec19/ui/theme/`). It replaced the Figma-generated token files (`colorSourceNN`, `typographySourceNN`, `radiusSourceNN`) as a pure swap: values are unchanged and the rendered screens are pixel-identical (checked with Roborazzi, see the end). Light only, Dynamic Color off. Roles the design never uses (for example `error`, `tertiary`, `display*`) keep the Material defaults.

Access: `MaterialTheme.colorScheme.x`, `MaterialTheme.typography.x`, `MaterialTheme.shapes.x`; extras through `MaterialTheme.extendedColors.x`, `.extendedTypography.x`, `.extendedShapes.x`.

The per-screen docs in this folder still quote the old token names. Use the tables below to translate them.

## Colors

| Old token | Value | New role | Used for (examples) |
|---|---|---|---|
| `colorSource00` | #004182 | `primary` | Auth/splash backgrounds, buttons, selected tab, indicators, cursors |
| `colorSource02` | #FFFFFF | `onPrimary` (text/icons on primary), `background` (screens), `surface` (cards, text fields) | Screen backgrounds, button text on primary, card fill |
| `colorSource12` | rgb(6, 0, 79) | `onSurface` (also `onBackground`) | Body text, titles, prices |
| `colorSource21` | `colorSource12` at 60% | `onSurfaceVariant` | Secondary text, hints, variant labels |
| `colorSource14` | #004182 at 30% | `outline` | Card, field and rail borders |
| `colorSource22` | rgb(219, 228, 237) at 50% | `surfaceVariant` | Categories rail and content panel fill |
| `colorSource23` | rgb(240, 240, 240) | `surfaceContainer` | Categories "Shop Now" banner fill |
| `colorSource30` | #6A6695 | `outlineVariant` | Divider bar inside cart/wishlist rows |
| `colorSource01` | #FFFFFF at 40% | extended `splashGlow` | Splash glow fallback |
| `colorSource03` | #000000 at 70% | extended `inputPlaceholder` | Auth field placeholder |
| `colorSource08` | #000000 | extended `inputText` | Auth field typed text |
| `colorSource18` | #84BFE1 | extended `footwearBanner` | Home footwear banner fill |
| `colorSource19` | #004182 at 60% | extended `oldPrice` | Struck-through old price |
| `colorSource20` | #FFFFFF at 70% | extended `searchFieldContainer` | Search field fill |
| `colorSource24` | rgb(47, 41, 41) | extended `swatchBlack` | Product details color swatch |
| `colorSource25` | #BC3018 | extended `swatchRed` | Product details color swatch |
| `colorSource26` | rgb(9, 115, 221) | extended `swatchBlue` | Product details color swatch |
| `colorSource27` | rgb(2, 185, 53) | extended `swatchGreen` | Product details color swatch |
| `colorSource28` | rgb(255, 100, 90) | extended `swatchCoral` | Product details color swatch |
| `colorSource29` | #BB3219 | extended `variantRust` | Cart/wishlist variant dot |
| `colorSource31` | #484848 | extended `variantCharcoal` | Cart/wishlist variant dot |
| `colorSource32` | #F89209 | extended `variantAmber` | Cart/wishlist variant dot |
| `colorSource33` | #FBE1C8 | extended `variantPeach` | Cart/wishlist variant dot |

Not carried over (no call site used them): `colorSource04`-`07`, `09`-`11`, `13`, `15`-`17`.

## Typography

All styles are Poppins with -0.165sp letter spacing and an 18sp line height unless noted.

| Old token | Weight / size | New role | Used for (examples) |
|---|---|---|---|
| `typographySource09` | Medium 30 | `headlineLarge` | Home banner discount |
| `typographySource03` | SemiBold 24 | `headlineMedium` | Sign in title |
| `typographySource08` | Medium 22 | `headlineSmall` | Home banner "Up to" |
| `typographySource10` | Medium 20 | `titleLarge` | Screen title bar, "Add to cart" / "Check Out" button text |
| `typographySource00` | Medium 18 | `titleMedium` | Section titles, labels, names, prices |
| `typographySource16` | SemiBold 16, line height 20 | `titleSmall` | Categories banner title |
| `typographySource04` | Light 16 | `bodyLarge` | Sign in subtitle |
| `typographySource07` | Regular 14 | `bodyMedium` | Product titles, descriptions, ratings |
| `typographySource11` | Regular 12 | `bodySmall` | Small prices, "Shop Now", captions |
| `typographySource15` | Medium 14 | `labelLarge` | Rail items, field values, compact button |
| `typographySource13` | Regular 11 | `labelSmall` | Old price |
| `typographySource02` | SemiBold 20 | extended `authButton` | "Login" / "Sign up" button text |
| `typographySource01` | Light 18 | extended `fieldInput` | Auth field text and placeholder |
| `typographySource05` | Regular 18 | extended `inlineLink` | "Forgot password" |
| `typographySource12` | Medium 20, line height 22 | extended `bannerOffer` | Footwear banner offer |
| `typographySource14` | Light 14 | extended `searchInput` | Search field text and hint |

Unused Material slots (`displayLarge/Medium/Small`, `labelMedium`) keep the Material defaults (Roboto).

## Shapes

| Old token | Value | New role | Used for (examples) |
|---|---|---|---|
| `radiusSource07` | 5dp | `shapes.extraSmall` | Square category tile |
| `radiusSource05` | 10dp | `shapes.small` | Rail, "Shop Now" pill |
| `radiusSource02` | 15dp | `shapes.medium` | Cards, fields, banners, bottom bar highlight |
| `radiusSource00` | 20dp | `shapes.large` | Quantity selector, outlined pill |
| `radiusSource08` | 30dp | `shapes.extraLarge` | Carousel active indicator |
| `radiusSource06` | 25dp | extended `searchField` | Search field |
| `radiusSource01` | 0dp | `ZeroCornerSize` on `shapes.small.copy(...)` | Rail top-end corner |

Not carried over: `radiusSource03` (1.6dp), `radiusSource04` (1.2dp); unused.

## Material component defaults

Before the rewrite, `AppTheme` already wrapped `MaterialTheme` with the nine colors listed as `primary` ... `surfaceVariant`, three typography slots and all five shapes; every other role was the Material baseline. The values of those pre-existing roles are unchanged. New explicit roles (`surfaceContainer`, `outlineVariant`, the typography slots) are not read by any Material component in the app (it uses only `Scaffold`, `Surface`, `Text` and `CircularProgressIndicator`, all with explicit colors or unchanged roles).

## Verification

Roborazzi (Robolectric, 430x932 mdpi): every screen rendered from its stateless composable with sample data, before and after the swap, compared pixel by pixel: all 10 screens identical (0 differing pixels).
