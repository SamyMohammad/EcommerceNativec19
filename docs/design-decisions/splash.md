# Splash

## Source

- Figma node id: `1:2`; file key `dEhOfaWrXc8wRA6mdeMecc`; version-id `2409508187813363894`.
- The version-id is owner-observed, NOT verified through the Figma MCP. Freshness is unverified.
- Code: `feature/splash` (`SplashScreen.kt`, `SplashRoute.kt`).

## Components used

- No shared `core/ui` layout components. Uses `FigmaCroppedImage` (logo crop) and `LightStatusBarIconsEffect` (white status bar icons).
- Plain `Box`, `Image` and `Modifier.blur` for the two glows.

## Material defaults / token mapping

- Background: `colorSource00` (primary). Glow color fallback: `colorSource01` (white, 40%).
- No text on this screen, so no typography token is used.
- No Material component is used; `AppTheme` is only the token provider.

## Deviations

- The two blurred glows (730x757, blur 75) come from an SVG with a Gaussian blur filter that VectorDrawable drops. The blur is re-applied with `Modifier.blur` on API 31+; below that a radial gradient approximates it. The fallback is an approximation and was not compared against the design.
- The Android native status bar is used; the 44px iOS band is not reproduced (see README).
- The logo is a crop of a 2000x2000 image (height 240%, top -66.06%) reproduced with fractions in `FigmaCroppedImage`.

## Missing contracts

- None for data. The only behavior is the 400 ms `AFTER_TIMEOUT` to Sign in (`SplashRoute`), which pops Splash from the back stack.
- Transition: cross-fade 400 ms linear instead of SMART_ANIMATE.

## Automated visual result

- Official core compare after correction round 3: **passed, 0.71%** changed pixels (policy: `channelTolerance` 16, `maxChangedRatio` 0.03, four 20x20 corner ignores).
- Harness: Roborazzi 1.76.0 + Robolectric 4.17, SDK 36, mdpi 430x932, light, en, ltr.
- An automated pass is not human approval.

## Unresolved / manual checks

- The `Modifier.blur` path (API 31+) and the gradient fallback were not checked on a device; the capture runs on SDK 36 only.
- Glow edge rendering on a real device is unverified.
- Poppins binary match with Figma is unverified (not used for text here).

## Human visual review

not_run
