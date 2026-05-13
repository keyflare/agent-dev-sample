---
name: exchange-svg-icon
description: Use when adding a new app icon to the Exchange Kotlin Multiplatform project from an SVG file, especially for Android Compose ImageVector icons, AppIcon enum entries, core:icons, or matching iOS icon mappings.
---

# Exchange SVG Icon

## Overview

Exchange app icons are shared product concepts, not Android drawables. Add every new icon through `core/icons`: `AppIcon` is the public enum, Android/Compose uses `ImageVector`s from `commonMain`, and iOS maps the same enum to either an SF Symbol or a custom asset.

## Workflow

1. Start with `git status --short --branch`. If HEAD is detached, create a `codex/<short-kebab-task>` branch before edits.
2. Inspect the SVG. If it contains `<rect>`, `<circle>`, `<line>`, `<polygon>`, `<polyline>`, `<ellipse>`, or `transform`, flatten/export it as path-only SVG first. The bundled script intentionally rejects unflattened SVGs.
3. Choose names:
   - enum: `UPPER_SNAKE`, e.g. `RECEIPT_LONG`
   - Compose property/file: `PascalCase`, e.g. `ReceiptLong.kt`
   - iOS custom asset, when needed: `ic_lower_snake`, e.g. `ic_receipt_long`
4. Add the enum to `core/icons/src/commonMain/kotlin/com/keyflare/exchange/core/icons/AppIcon.kt`.
5. Run `./gradlew :core:icons:allTests`. It should fail until the Compose vector and iOS mapping are wired.
6. Generate the Kotlin vector:

```bash
python3 .codex/skills/exchange-svg-icon/scripts/svg_to_exchange_icon.py \
  /path/to/icon.svg \
  --icon-name RECEIPT_LONG \
  --out core/icons/src/commonMain/kotlin/com/keyflare/exchange/core/icons/compose/list/ReceiptLong.kt
```

7. Open the generated Kotlin and compare it with nearby files in `core/icons/src/commonMain/kotlin/com/keyflare/exchange/core/icons/compose/list/`. Keep package, `AppIcon.Compose.<Name>`, cache var, `defaultWidth/defaultHeight = 24.dp`, and `name = AppIcon.<ENUM>.name`.
8. Register the vector in `core/icons/src/commonMain/kotlin/com/keyflare/exchange/core/icons/compose/ComposeIcons.kt`:
   - add `import com.keyflare.exchange.core.icons.compose.list.<Name>`
   - add `<Name>,` to `AppIcon.Compose.AllIcons`
9. Update `core/icons/src/iosMain/kotlin/com/keyflare/exchange/core/icons/IosIcons.kt`:
   - prefer `IconName.System("<sf-symbol>")` when there is a close SF Symbol
   - otherwise add a custom iOS asset and return `IconName.Custom("ic_lower_snake")`
10. For a custom iOS asset, create `app/ios/ios/Assets.xcassets/ic_lower_snake.imageset/`, copy the SVG into it, and add:

```json
{
  "images" : [
    {
      "filename" : "ic_lower_snake.svg",
      "idiom" : "universal",
      "scale" : "1x"
    },
    {
      "idiom" : "universal",
      "scale" : "2x"
    },
    {
      "idiom" : "universal",
      "scale" : "3x"
    }
  ],
  "info" : {
    "author" : "xcode",
    "version" : 1
  }
}
```

11. Run `./gradlew :core:icons:allTests`. If the icon is used by Android UI, also run the narrowest relevant UI/module test or `./gradlew :app:android:assembleDebug`.

## Project Rules

- Do not add Android `res/drawable` files for shared app icons. This project renders Android feature icons from `core/icons` Compose `ImageVector`s.
- Do not skip `AppIcon.Compose.AllIcons`; `AppIcon.imageVector()` searches this list by `ImageVector.name`.
- Keep `ImageVector.Builder(name = AppIcon.<ENUM>.name)`. A mismatched name makes lookup fail.
- Keep icon colors conventional. The project usually uses `Color(0xFFE8EAED)` in vectors and applies design-system tint at render sites.
- Do not assume Android coverage gives iOS coverage. Adding an enum requires an iOS mapping in `IosIcons.kt`.

## Script Notes

`scripts/svg_to_exchange_icon.py` generates Kotlin that uses `PathParser().parsePathString(...).toNodes()` for each SVG `<path>`. It supports path fill/stroke attributes, `style`, `viewBox`, `fill-rule`, line cap/join, opacity, and stroke width. It deliberately fails on shapes and transforms because those need to be flattened before reliable code generation.
