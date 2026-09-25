#!/bin/sh
# Этап 4: стартовый скрипт с командами ls, cd, rev, clear и примеры ошибок.
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT" || exit 1

echo "=== Stage 4: all commands ==="
./run.sh --vfs examples/vfs/deep.csv --script examples/startup/stage4.txt < /dev/null

for script in examples/startup/errors/stage4_*.txt; do
    echo
    echo "=== Error example: $script ==="
    ./run.sh --vfs examples/vfs/deep.csv --script "$script" < /dev/null
done
