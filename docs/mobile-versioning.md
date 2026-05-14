# Mobile Versioning

Mobile app versions are derived from Git tags by `scripts/mobile-version`.
The same script is used by both Android and iOS, so the version name and build
number stay consistent across platforms.

## Release Tags

Release versions come from annotated Git tags that match this format:

```text
mobile/vX.Y.Z
```

For example:

```text
mobile/v1.2.3
```

The tag must satisfy all of these rules:

- It must point exactly at the commit being built for a release.
- It must be an annotated tag, not a lightweight tag.
- It must use semantic numeric components: `major.minor.patch`.
- Version components must not contain leading zeroes, except for the value `0`.

Valid examples:

```text
mobile/v0.1.0
mobile/v1.2.3
mobile/v10.20.30
```

Invalid examples:

```text
mobile/v01.2.3
mobile/v1.02.3
mobile/v1.2
mobile/vnot.a.release
```

## Version Name

The version name is the public app version:

- Android: `versionName`
- iOS: `CFBundleShortVersionString`

For release builds, `scripts/mobile-version` requires `HEAD` to be exactly on a
valid `mobile/vX.Y.Z` tag. The script removes the `mobile/v` prefix and uses the
remaining semantic version as the version name.

Example:

```text
mobile/v1.2.3 -> 1.2.3
```

For debug and non-release builds, the script uses the latest local annotated
`mobile/vX.Y.Z` tag by tagger date. If there is no valid local release tag, the
debug version name falls back to:

```text
0.0.0
```

## Build Number

The build number is the platform build identifier:

- Android: `versionCode`
- iOS: `CFBundleVersion`

For debug and non-release builds, the build number is always:

```text
1
```

For release builds, the build number is calculated from the order of valid
annotated mobile release tags in `origin`.

The script:

1. Reads valid annotated `mobile/vX.Y.Z` tags from `origin`.
2. Converts each semantic version into a sortable numeric key.
3. Counts all remote release versions less than or equal to the current release
   version.
4. Adds one more if the current local release tag has not been pushed to
   `origin` yet.

This means older release tags keep their original build numbers, and a new
unpushed release tag can still produce the next expected build number.

Example sequence:

```text
mobile/v1.0.0 -> build number 1
mobile/v1.1.0 -> build number 2
mobile/v1.2.0 -> build number 3
```

## Android Integration

Android configures default placeholder values in `app/android/build.gradle.kts`:

```kotlin
versionName = "0.0.0"
versionCode = 1
```

The actual variant outputs are then overridden through the Android Components
API. For each variant, Gradle calls:

```text
scripts/mobile-version version-name --debug
scripts/mobile-version build-number --debug
```

or, for the `release` build type:

```text
scripts/mobile-version version-name --release
scripts/mobile-version build-number --release
```

The returned values are assigned to `output.versionName` and
`output.versionCode`.

## iOS Integration

iOS uses the same versioning script through the `Apply Git Version` Xcode build
phase. That build phase runs:

```text
scripts/apply-ios-mobile-version
```

`scripts/apply-ios-mobile-version` selects the mode from the Xcode build
configuration:

- `Release` uses `--release`.
- Any other configuration uses `--debug`.

The script writes the computed values into the built `Info.plist`:

```text
CFBundleShortVersionString = version name
CFBundleVersion = build number
```

The Xcode project still contains fallback build settings:

```text
MARKETING_VERSION = 0.0.0
CURRENT_PROJECT_VERSION = 1
```

Those values are placeholders. The build phase updates the built plist with the
Git-derived values.

## Useful Commands

Check the debug version name:

```sh
scripts/mobile-version version-name --debug
```

Check the debug build number:

```sh
scripts/mobile-version build-number --debug
```

Check the release version name:

```sh
scripts/mobile-version version-name --release
```

Check the release build number:

```sh
scripts/mobile-version build-number --release
```

Print Xcode-style settings:

```sh
scripts/mobile-version xcode-settings --release
```

Release commands must be run from a commit that is exactly tagged with a valid
annotated `mobile/vX.Y.Z` tag. Otherwise, the script fails intentionally to
prevent accidental release builds with an ambiguous version.
