#!/bin/sh
# Этап 5: стартовый скрипт с командой cp и примеры ошибок.
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT" || exit 1

echo "=== Stage 5: cp ==="
./run.sh --vfs examples/vfs/deep.csv --script examples/startup/stage5.txt < /dev/null

for script in examples/startup/errors/stage5_*.txt; do
    echo
    echo "=== Error example: $script ==="
    ./run.sh --vfs examples/vfs/deep.csv --script "$script" < /dev/null
done
