#!/bin/sh
# Этап 3: VFS с тремя и более уровнями файлов и каталогов.
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT" || exit 1

echo "=== VFS: deep.csv ==="
./run.sh --vfs examples/vfs/deep.csv --script examples/startup/stage3.txt < /dev/null
echo "exit code: $?"
