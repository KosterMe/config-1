#!/bin/sh
# Этап 3: VFS с несколькими файлами (в том числе двоичным).
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT" || exit 1

echo "=== VFS: files.csv ==="
./run.sh --vfs examples/vfs/files.csv --script examples/startup/stage3.txt < /dev/null
echo "exit code: $?"
