#!/bin/sh
# Сборка и запуск эмулятора. Аргументы передаются эмулятору.
ROOT="$(cd "$(dirname "$0")" && pwd)"
javac -encoding UTF-8 -d "$ROOT/out/classes" -sourcepath "$ROOT/src" \
    "$ROOT/src/emulator/Main.java" || exit 1
java -cp "$ROOT/out/classes" emulator.Main "$@"
