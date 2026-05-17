#!/usr/bin/env bash
set -euo pipefail

APP_NAME="Orbital Simulation"
APP_VERSION="1.0.0"
APP_ID="com.orbital.simulation"
MAIN_JAR="orbital-app-0.0.1-SNAPSHOT.jar"
OPEN_AFTER_BUILD="${OPEN_AFTER_BUILD:-true}"
RUNTIME_MODULES="java.base,java.desktop,java.instrument,java.logging,java.management,java.naming,java.net.http,java.prefs,java.scripting,java.sql,java.transaction.xa,java.xml,java.xml.crypto,jdk.crypto.ec,jdk.unsupported"

ROOT_DIR="$(cd "$(dirname "$0")/.." && pwd)"
TARGET_DIR="$ROOT_DIR/target"
DIST_DIR="$TARGET_DIR/macos-app"
APP_PATH="$DIST_DIR/$APP_NAME.app"
BUILD_DIR="$(mktemp -d "${TMPDIR:-/private/tmp}/orbital-jpackage.XXXXXX")"
INPUT_DIR="$BUILD_DIR/input"
PACKAGE_DIR="$BUILD_DIR/output"

cleanup() {
  rm -rf "$BUILD_DIR"
}
trap cleanup EXIT

cd "$ROOT_DIR"

./apache-maven-3.9.6/bin/mvn -q -DskipTests package

rm -rf "$DIST_DIR"
mkdir -p "$INPUT_DIR" "$PACKAGE_DIR" "$DIST_DIR"
cp "$TARGET_DIR/$MAIN_JAR" "$INPUT_DIR/"
xattr -cr "$INPUT_DIR" 2>/dev/null || true

jpackage \
  --type app-image \
  --name "$APP_NAME" \
  --app-version "$APP_VERSION" \
  --vendor "Orbital" \
  --mac-package-identifier "$APP_ID" \
  --mac-package-name "Orbital" \
  --add-modules "$RUNTIME_MODULES" \
  --input "$INPUT_DIR" \
  --main-jar "$MAIN_JAR" \
  --dest "$PACKAGE_DIR" \
  --java-options "-Dfile.encoding=UTF-8" \
  --java-options "-Xmx1024m"

ditto --noextattr --noqtn "$PACKAGE_DIR/$APP_NAME.app" "$APP_PATH"
xattr -cr "$APP_PATH" 2>/dev/null || true

if [[ "$OPEN_AFTER_BUILD" == "true" ]]; then
  open "$APP_PATH"
else
  printf 'Built macOS app: %s\n' "$APP_PATH"
fi
