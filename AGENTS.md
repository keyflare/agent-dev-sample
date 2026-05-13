# AGENTS.md

Guidance for AI coding agents working in this repository.

## Project Overview

This is a Kotlin Multiplatform mobile app named `exchange`.

The project uses shared Kotlin code for application state, navigation, DI, platform abstractions, analytics, utility UI models, and feature ViewModels. Android renders the app with Compose. iOS renders the app with native SwiftUI while consuming the shared Kotlin framework.

Prefer preserving this split:

- Shared product logic belongs in KMP modules.
- Android UI belongs in Compose modules and the Android app shell.
- iOS UI belongs in SwiftUI under `app/ios/ios/src`.
- Platform-specific services should be exposed through shared interfaces and injected from platform app shells.

## Task Workflow

Before making any repository change, check the current Git state with `git status --short --branch`.

If the repository is in detached HEAD, create a task branch before editing. Branch names must use:

```text
codex/my-task-short-description
```

Use a short kebab-case task description, for example `codex/add-settings-screen` or `codex/fix-theme-store`.

If a task does not explicitly name a single target platform, treat it as a cross-platform task. UI and platform-code changes must be implemented for both Android and iOS when the affected behavior exists on both platforms.

## Pull Requests

Regardless of the language used in the task, pull request titles and descriptions must always be written in English.

Always merge pull requests with squash merge only. Do not use merge commits or rebase merges unless the user explicitly changes this repository rule.

After a pull request has been merged, delete the local task branch.

## Repository Structure

Top-level module groups:

- `app:android` - Android application shell, Android `Application`, `Activity`, platform wiring, Compose root rendering, Android build variants and signing.
- `app:shared` - KMP app entry point, root DI, root Decompose navigation graph, shared network client factory, exported iOS framework `ExchangeShared`.
- `app/ios` - Native iOS Xcode project and SwiftUI UI layer consuming `ExchangeShared`.
- `common:*` - Generic reusable KMP utilities that are not product-specific.
- `core:*` - Product-wide building blocks used by features: analytics, design system, icons, navigation tools, platform services, strings, utility screen models/views.
- `feature:*` - User-facing features, normally split into `api`, `impl`, and `ui` modules.
- `build-utils` and `buildSrc` - Gradle convention helpers. Use existing helpers instead of open-coding Gradle setup.
- `scripts` - Release/versioning helper scripts.

Current Gradle modules are listed in `settings.gradle.kts`. Add every new Gradle module there.

## Module Placement Rules

Use these rules when adding new code:

- Put generic, product-agnostic helpers in `common:<name>`.
- Put cross-feature product infrastructure in `core:<name>`.
- Put feature contracts in `feature:<feature>:api`.
- Put feature state, ViewModels, component factories, feature DI, and business logic in `feature:<feature>:impl`.
- Put shared Compose UI for Android in `feature:<feature>:ui`.
- Put iOS SwiftUI screens for a feature in `app/ios/ios/src/Feature/<FeatureName>`.
- Put Android-only shell concerns in `app:android`, not in feature implementation modules.
- Put app-level navigation and dependency composition in `app:shared`, not inside individual features.

Do not create a new module shape if the `api / impl / ui` feature pattern fits.

## KMP Source Set Rules

Use KMP source sets intentionally:

- `commonMain` - shared Kotlin logic and platform-neutral interfaces.
- `commonTest` - platform-neutral tests for shared logic.
- `androidMain` - Android-specific implementations, Android manifests, Android platform integrations.
- `iosMain` - iOS-specific Kotlin implementations.
- `androidUnitTest` or Android `src/test` - Android-only tests.

Keep Android SDK, UIKit, SwiftUI, and other platform APIs out of `commonMain`. Use `expect/actual` only when a small platform difference belongs close to the abstraction. For product services, prefer interfaces in `core:platform` and inject platform implementations from app shells.

## UI Architecture

All UI changes must respect the design system. Use colors, typography, shapes, spacing patterns, and shared components from `core:design-system` unless the task explicitly says otherwise.

Do not hardcode colors or fonts in feature UI when an existing design-system token or component can express the same intent.

Android:

- Android root rendering is in `app/android/src/main/java/com/keyflare/exchange/android/ExchangeAppView.kt`.
- Feature Compose UI lives in `feature:<feature>:ui/src/commonMain`.
- Use `core:design-system` components and theme (`ExchangeTheme`, `CustomTheme`) instead of ad hoc styling.
- The Android root maps Decompose child instances to shared ViewModels, then calls feature Compose entry points.

iOS:

- iOS root rendering is in `app/ios/ios/src/ExchangeAppView.swift`.
- iOS feature screens live under `app/ios/ios/src/Feature`.
- SwiftUI screens consume shared Kotlin ViewModels and observe `StateFlow` through the existing flow wrappers.
- When adding a shared feature that iOS must render, update the Swift root child routing and add the SwiftUI screen.

Do not assume Compose UI automatically covers iOS. This project intentionally uses native SwiftUI for iOS presentation.

## Navigation

Root navigation is in `app:shared`:

- `RootChildConfig` defines root destinations.
- `RootNavigationGraph` creates feature components and wires navigators.
- `RootComponentImpl` owns the Decompose `StackNavigation`.

When adding a root-level screen:

1. Add a serializable config to `RootChildConfig`.
2. Add the feature component factory call in `RootNavigationGraph`.
3. Add navigator methods to the originating feature contract if needed.
4. Add Android rendering in `ExchangeAppView.kt`.
5. Add iOS rendering in `ExchangeAppView.swift` if the screen is available on iOS.
6. Ensure required shared modules are dependencies of `app:shared` and exported to iOS when Swift must see them.

Use feature navigator interfaces for cross-screen movement. ViewModels should call navigators, not root navigation directly.

## ViewModel and State Pattern

Feature ViewModels should follow the existing pattern:

- Extend `core:navigation-tools` `ViewModel<ViewState, UiEvent>`.
- Expose `StateFlow<ViewState>`.
- Accept `args`, `navigator`, and feature `di` through the constructor.
- Keep mutable internal state in internal state models.
- Convert internal state to public view state with a small mapper.
- Handle UI actions through sealed or object UI event types.
- Track analytics from ViewModels when user intent is known there.

Feature components should be created with `ViewModelComponent.with(componentContext) { ... }` so Decompose instance keeping works consistently.

## Dependency Injection

DI is manual. Do not introduce a DI framework unless explicitly requested.

- App-level dependencies live in `RootDi`.
- Platform dependencies enter shared code through `ExchangePlatformDependencies`.
- Feature-level dependencies should be grouped in small `*Di` classes near the feature implementation API.
- Platform services should be modeled in `core:platform` and provided by Android/iOS app shells.
- Data stores should be created behind shared stores, not directly inside UI code.
- ViewModels should depend on interfaces, stores, helpers, and dispatchers from DI. Avoid constructing platform services or clients inside ViewModels.

If a dependency is app-wide, add it to `RootDi`. If it is feature-only, keep it in the feature DI object.

## Networking and Data

The shared app owns a Ktor `HttpClient` factory in `app:shared`.

- Put platform engine differences in `androidMain` / `iosMain`.
- Install common Ktor plugins in shared code.
- Android debug/dev builds use Chucker for network inspection.
- iOS has no network log opener wired by default.

If adding repositories or API clients, keep interfaces and models in shared Kotlin. Inject clients through DI and keep platform-specific networking details out of features.

## Design System and Resources

- Prefer `core:design-system` components over feature-local UI primitives.
- Prefer `core:icons` for icon definitions.
- Put reusable strings in `core:strings`.
- Feature-specific Compose resources belong in that feature's `ui` module.
- iOS assets belong in the Xcode asset catalog under `app/ios/ios/Assets.xcassets`.

Avoid duplicating colors, typography, spacing, icons, or utility screen components inside feature modules.

## Gradle Conventions

Use the existing Gradle helpers:

- `plugins { applyFor(...) }`
- `deps { commonDeps { ... } androidDeps { ... } iosDeps { ... } commonTestDeps { ... } }`
- `setup { androidLibrary(...) iosLibrary(...) }`

Do not bypass these helpers without a strong reason. They centralize KMP targets, Android SDK levels, Compose setup, explicit API mode, Java/Kotlin target versions, and iOS framework setup.

For new feature modules, mirror existing feature module build files before inventing a new setup.

## Build Variants and Release Notes

Android build types:

- `debug` - debug signing, HTTP allowed, dev AppMetrica key.
- `dev` - release-like but debug signed, HTTP allowed, minify disabled.
- `release` - release signing when env vars are present, HTTP disabled, minify/shrink enabled.

Mobile versions are derived by `scripts/mobile-version` from annotated tags matching `mobile/vX.Y.Z`. Release builds must run from an exact annotated mobile tag.

Do not hardcode secrets. AppMetrica and signing values come from environment variables.

## Testing and Verification

Run the narrowest useful checks for your change, then broader checks when touching shared contracts or build setup.

Useful commands:

- `./gradlew test` - broad JVM/common test pass.
- `./gradlew :common:utils:allTests` - KMP tests for one shared module.
- `./gradlew :feature:settings:impl:allTests` - feature implementation tests.
- `./gradlew :app:android:testDebugUnitTest` - Android app unit tests.
- `./gradlew :app:android:assembleDebug` - Android debug build.

For docs-only changes, at minimum verify the file content and `git status`.

## Coding Style

- Follow existing package names under `com.keyflare.exchange` and `com.keyflare.common`.
- Keep public API intentional. Many KMP library modules use explicit API mode.
- Prefer immutable public view states and internal mutable state.
- Keep feature boundaries clear; do not make unrelated features depend on each other's implementation modules.
- Avoid platform conditionals in shared feature code when DI or `expect/actual` would be cleaner.
- Keep comments sparse and useful.
- Do not reformat unrelated files.

## Git and Worktree Notes

This repository may be opened through Git worktrees. If `main` is already checked out by another worktree, Git will not allow this worktree to switch to the same local branch. In that case, use a detached `origin/main` or create a task branch from `origin/main`, depending on the task.

Before editing, check `git status --short --branch`. Never discard unrelated user changes.
