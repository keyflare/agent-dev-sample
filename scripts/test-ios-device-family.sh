#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
PROJECT_FILE="$ROOT_DIR/app/ios/ios.xcodeproj/project.pbxproj"
INFO_PLISTS=(
  "$ROOT_DIR/app/ios/ios/Info-Debug.plist"
  "$ROOT_DIR/app/ios/ios/Info-Release.plist"
)

assert_not_contains() {
  local unexpected="$1"
  local actual="$2"
  local message="$3"

  if [[ "$actual" == *"$unexpected"* ]]; then
    echo "FAIL: $message"
    echo "  unexpected: $unexpected"
    exit 1
  fi
}

target_family_count="$(grep -c 'TARGETED_DEVICE_FAMILY = 1;' "$PROJECT_FILE" || true)"
if [[ "$target_family_count" != "2" ]]; then
  echo "FAIL: iOS target must be iPhone-only in Debug and Release"
  echo "  expected TARGETED_DEVICE_FAMILY = 1; twice, got $target_family_count"
  exit 1
fi

project_text="$(cat "$PROJECT_FILE")"
assert_not_contains 'TARGETED_DEVICE_FAMILY = "1,2";' "$project_text" "iOS target must not include iPad device family"

for plist in "${INFO_PLISTS[@]}"; do
  plutil -lint "$plist" >/dev/null

  if /usr/libexec/PlistBuddy -c 'Print :UISupportedInterfaceOrientations~ipad' "$plist" >/dev/null 2>&1; then
    echo "FAIL: $plist must not declare iPad-specific orientations"
    exit 1
  fi

  if /usr/libexec/PlistBuddy -c 'Print :UIRequiresFullScreen' "$plist" >/dev/null 2>&1; then
    echo "FAIL: $plist must not rely on full-screen iPad multitasking opt-out"
    exit 1
  fi
done

echo "ios-device-family tests passed"
