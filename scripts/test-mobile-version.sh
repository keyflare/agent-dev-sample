#!/usr/bin/env bash
set -euo pipefail

ROOT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")/.." && pwd)"
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

assert_fails() {
  local message="$1"
  shift

  if "$@" >/tmp/mobile-version-test.out 2>/tmp/mobile-version-test.err; then
    echo "FAIL: $message"
    echo "  command unexpectedly succeeded: $*"
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
trap 'rm -rf "$TMP_DIR" /tmp/mobile-version-test.out /tmp/mobile-version-test.err' EXIT

cd "$TMP_DIR"
git init --bare -q origin.git
git init -q repo
cd repo
git config user.email "codex@example.test"
git config user.name "Codex"
git remote add origin "$TMP_DIR/origin.git"
DEFAULT_BRANCH="$(git symbolic-ref --short HEAD)"

make_commit "Initial commit"

assert_eq "1" "$("$MOBILE_VERSION" build-number --debug)" "debug build number is always 1"
assert_eq "0.0.0" "$("$MOBILE_VERSION" version-name --debug)" "debug version name falls back before the first tag"
assert_fails "release version cannot be computed away from a release tag" "$MOBILE_VERSION" version-name --release

GIT_COMMITTER_DATE="2026-01-01T00:00:00Z" git tag -a mobile/v1.0.0 -m "mobile/v1.0.0"
make_commit "Second release commit"
GIT_COMMITTER_DATE="2026-01-01T12:00:00Z" git tag -a mobile/vnot.a.release -m "mobile/vnot.a.release"
GIT_COMMITTER_DATE="2026-01-02T00:00:00Z" git tag -a mobile/v1.1.0 -m "mobile/v1.1.0"
git push -q origin "$DEFAULT_BRANCH" mobile/v1.0.0 mobile/vnot.a.release mobile/v1.1.0

assert_eq "1.1.0" "$("$MOBILE_VERSION" version-name --release)" "release version name comes from the exact mobile tag"
assert_eq "2" "$("$MOBILE_VERSION" build-number --release)" "release build number increments with each mobile tag"

make_commit "Private local tag"
GIT_COMMITTER_DATE="2026-01-03T00:00:00Z" git tag -a mobile/v9.0.0 -m "mobile/v9.0.0"
git checkout -q mobile/v1.1.0

assert_eq "2" "$("$MOBILE_VERSION" build-number --release)" "private local mobile tags do not affect release build number"

git checkout -q mobile/v1.0.0

assert_eq "1.0.0" "$("$MOBILE_VERSION" version-name --release)" "older release tags keep their version name"
assert_eq "1" "$("$MOBILE_VERSION" build-number --release)" "older release tags keep their original build number"

git checkout -q "$DEFAULT_BRANCH"
make_commit "Local release commit"
GIT_COMMITTER_DATE="2026-01-04T00:00:00Z" git tag -a mobile/v1.2.0 -m "mobile/v1.2.0"

assert_eq "1.2.0" "$("$MOBILE_VERSION" version-name --release)" "unpushed release tags keep their version name"
assert_eq "3" "$("$MOBILE_VERSION" build-number --release)" "unpushed release tags use origin release count plus one"

make_commit "Invalid release tag commit"
GIT_COMMITTER_DATE="2026-01-05T00:00:00Z" git tag -a mobile/v01.2.3 -m "mobile/v01.2.3"

assert_fails "release tags must not have leading zeroes" "$MOBILE_VERSION" version-name --release

make_commit "Lightweight tag commit"
git tag mobile/v1.3.0

assert_fails "release tags must be annotated" "$MOBILE_VERSION" version-name --release

echo "mobile-version tests passed"
