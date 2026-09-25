#!/bin/sh
# Этап 2: выполнение стартового скрипта (остановка на ошибке, команда exit).
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT" || exit 1

echo "=== 1. Script stops at the first error ==="
./run.sh --script examples/startup/stage2_error.txt < /dev/null
echo "exit code: $?"

echo "=== 2. exit in the script ==="
./run.sh --script examples/startup/stage2_exit.txt < /dev/null
echo "exit code: $?"
