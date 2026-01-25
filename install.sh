#!/usr/bin/env bash

set -e
set -o pipefail

case "$1" in
debug) MODE="debugExecutable" ;;
release) MODE="releaseExecutable" ;;
*) echo "Specify build type: debug or release" && exit 1 ;;
esac

case "$OSTYPE" in
linux*) OS="linux" ;;
*) echo "Unsupported operating system: $OSTYPE" && exit 1 ;;
esac

case "$(arch)" in
x86_64) ARCH="X64" ;;
*) echo "Unsupported architecture: $(arch)" && exit 1 ;;
esac

./gradlew linuxX64MainBinaries -P"$1"

sudo cp "$PWD/build/bin/$OS$ARCH/$MODE/translate.kexe" "/usr/local/bin/translate"
