#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
APPLY_VERSION="$ROOT_DIR/scripts/apply-ios-mobile-version"
MOBILE_VERSION="$ROOT_DIR/scripts/mobile-version"

assert_eq() {
  local expected="$1"
  local actual="$2"
  local message="$3"

  if [[ "$actual" != "$expected" ]]; then
    echo "FAIL: $message"
    echo "  expected: $expected"
    echo "  actual:   $actual"
    exit 1
  fi
}

make_commit() {
  local message="$1"

  echo "$message" >> file.txt
  git add file.txt
  git commit -m "$message" >/dev/null
}

TMP_DIR="$(mktemp -d)"
trap 'rm -rf "$TMP_DIR"' EXIT

cd "$TMP_DIR"
git init --bare -q origin.git
git init -q repo
cd repo
git config user.email "codex@example.test"
git config user.name "Codex"
git remote add origin "$TMP_DIR/origin.git"

mkdir -p app/ios build/ios.app
make_commit "Initial commit"
GIT_COMMITTER_DATE="2026-01-01T00:00:00Z" git tag -a mobile/v2.3.4 -m "mobile/v2.3.4"
DEFAULT_BRANCH="$(git symbolic-ref --short HEAD)"
git push -q origin "$DEFAULT_BRANCH" mobile/v2.3.4

cat > build/ios.app/Info.plist <<'PLIST'
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE plist PUBLIC "-//Apple//DTD PLIST 1.0//EN" "https://www.apple.com/DTDs/PropertyList-1.0.dtd">
<plist version="1.0">
<dict>
    <key>CFBundleShortVersionString</key>
    <string>0.0.0</string>
    <key>CFBundleVersion</key>
    <string>1</string>
    <key>ComicVineApiKey</key>
    <string>$(COMICVINE_API_KEY)</string>
</dict>
</plist>
PLIST

SRCROOT="$TMP_DIR/repo/app/ios" \
TARGET_BUILD_DIR="$TMP_DIR/repo/build" \
INFOPLIST_PATH="ios.app/Info.plist" \
CONFIGURATION="Release" \
COMICVINE_API_KEY="ios-test-key" \
MOBILE_VERSION_SCRIPT="$MOBILE_VERSION" \
"$APPLY_VERSION" >/dev/null

short_version="$(/usr/libexec/PlistBuddy -c 'Print :CFBundleShortVersionString' "$TMP_DIR/repo/build/ios.app/Info.plist")"
build_version="$(/usr/libexec/PlistBuddy -c 'Print :CFBundleVersion' "$TMP_DIR/repo/build/ios.app/Info.plist")"
comic_vine_api_key="$(/usr/libexec/PlistBuddy -c 'Print :ComicVineApiKey' "$TMP_DIR/repo/build/ios.app/Info.plist")"

assert_eq "2.3.4" "$short_version" "release iOS short version comes from mobile tag"
assert_eq "1" "$build_version" "release iOS build number comes from mobile tag order"
assert_eq "ios-test-key" "$comic_vine_api_key" "iOS Comic Vine API key comes from COMICVINE_API_KEY env"

mkdir -p "$TMP_DIR/bin"
cat > "$TMP_DIR/bin/launchctl" <<'SH'
#!/usr/bin/env bash
exit 0
SH
chmod +x "$TMP_DIR/bin/launchctl"

cat > "$TMP_DIR/bin/fake-shell" <<'SH'
#!/usr/bin/env bash
if [[ -z "${COMICVINE_API_KEY+x}" ]]; then
  export COMICVINE_API_KEY="ios-shell-key"
fi

if [[ "${2:-}" == "printenv COMICVINE_API_KEY" ]]; then
  printenv COMICVINE_API_KEY
fi
SH
chmod +x "$TMP_DIR/bin/fake-shell"

/usr/libexec/PlistBuddy -c 'Set :ComicVineApiKey $(COMICVINE_API_KEY)' "$TMP_DIR/repo/build/ios.app/Info.plist"

SRCROOT="$TMP_DIR/repo/app/ios" \
TARGET_BUILD_DIR="$TMP_DIR/repo/build" \
INFOPLIST_PATH="ios.app/Info.plist" \
CONFIGURATION="Release" \
COMICVINE_API_KEY='$(COMICVINE_API_KEY)' \
PATH="$TMP_DIR/bin:/usr/bin:/bin:/usr/sbin:/sbin" \
SHELL="$TMP_DIR/bin/fake-shell" \
MOBILE_VERSION_SCRIPT="$MOBILE_VERSION" \
"$APPLY_VERSION" >/dev/null

comic_vine_api_key="$(/usr/libexec/PlistBuddy -c 'Print :ComicVineApiKey' "$TMP_DIR/repo/build/ios.app/Info.plist")"

assert_eq "ios-shell-key" "$comic_vine_api_key" "iOS Comic Vine API key falls back to the user shell when Xcode provides a placeholder"

stamp_file="$TMP_DIR/repo/build/apply-ios-mobile-version.stamp"
rm -f "$stamp_file"

SRCROOT="$TMP_DIR/repo/app/ios" \
TARGET_BUILD_DIR="$TMP_DIR/repo/build" \
INFOPLIST_PATH="ios.app/Info.plist" \
CONFIGURATION="Release" \
MOBILE_VERSION_SCRIPT="$MOBILE_VERSION" \
SCRIPT_OUTPUT_FILE_0="$stamp_file" \
"$APPLY_VERSION" >/dev/null

[[ -f "$stamp_file" ]] || {
  echo "FAIL: iOS version script creates Xcode output stamp"
  exit 1
}

echo "apply-ios-mobile-version tests passed"
