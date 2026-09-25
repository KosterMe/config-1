#!/bin/sh
# Этап 3: минимальная VFS (только корневой каталог).
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT" || exit 1

echo "=== VFS: minimal.csv ==="
./run.sh --vfs examples/vfs/minimal.csv --script examples/startup/stage3.txt < /dev/null
echo "exit code: $?"
