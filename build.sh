#!/usr/bin/env bash
set -e

PROJECT_ROOT="$(cd "$(dirname "$0")" && pwd)"
BIN_DIR="$PROJECT_ROOT/bin"
SRC_DIR="$PROJECT_ROOT/src"
JAR_NAME="uco-si-final.jar"
MAIN_CLASS="App"

# Clean and compile
rm -rf "$BIN_DIR"
mkdir -p "$BIN_DIR"
javac -d "$BIN_DIR" "$SRC_DIR"/*.java

# Copy assets into bin so they are packaged inside the jar
if [ -d "$SRC_DIR/assets" ]; then
  cp -r "$SRC_DIR/assets" "$BIN_DIR/"
fi

# Create manifest
MANIFEST="$BIN_DIR/manifest.txt"
echo "Main-Class: $MAIN_CLASS" > "$MANIFEST"
echo "" >> "$MANIFEST"

# Package jar (include classes and assets)
(
  cd "$BIN_DIR"
  jar cfm "$JAR_NAME" manifest.txt *
  mv "$JAR_NAME" "$PROJECT_ROOT/"
)

echo "Created $PROJECT_ROOT/$JAR_NAME"
