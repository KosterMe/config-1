#!/bin/sh
# Этап 2: запуск эмулятора с разными наборами параметров.
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT" || exit 1

echo "=== 1. No options ==="
./run.sh < /dev/null
echo "exit code: $?"

echo "=== 2. Only --vfs ==="
./run.sh --vfs examples/vfs/deep.csv < /dev/null
echo "exit code: $?"

echo "=== 3. Only --script ==="
./run.sh --script examples/startup/stage2.txt < /dev/null
echo "exit code: $?"

echo "=== 4. --vfs and --script ==="
./run.sh --vfs examples/vfs/deep.csv --script examples/startup/stage2.txt < /dev/null
echo "exit code: $?"
