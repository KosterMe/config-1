#!/bin/sh
# Этап 2: ошибки в параметрах командной строки.
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT" || exit 1

echo "=== 1. Unknown option ==="
./run.sh --foo bar < /dev/null
echo "exit code: $?"

echo "=== 2. Option without value ==="
./run.sh --script < /dev/null
echo "exit code: $?"

echo "=== 3. Duplicate option ==="
./run.sh --vfs a.csv --vfs b.csv < /dev/null
echo "exit code: $?"

echo "=== 4. Script file not found ==="
./run.sh --script examples/startup/missing.txt < /dev/null
echo "exit code: $?"
