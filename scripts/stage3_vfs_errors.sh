#!/bin/sh
# Этап 3: ошибки загрузки VFS (файл не найден, неверный формат).
ROOT="$(cd "$(dirname "$0")/.." && pwd)"
cd "$ROOT" || exit 1

echo "=== VFS: missing.csv ==="
./run.sh --vfs examples/vfs/missing.csv --script examples/startup/stage3.txt < /dev/null
echo "exit code: $?"

echo "=== VFS: bad_header.csv ==="
./run.sh --vfs examples/vfs/bad_header.csv --script examples/startup/stage3.txt < /dev/null
echo "exit code: $?"

echo "=== VFS: bad_fields.csv ==="
./run.sh --vfs examples/vfs/bad_fields.csv --script examples/startup/stage3.txt < /dev/null
echo "exit code: $?"

echo "=== VFS: bad_type.csv ==="
./run.sh --vfs examples/vfs/bad_type.csv --script examples/startup/stage3.txt < /dev/null
echo "exit code: $?"

echo "=== VFS: bad_base64.csv ==="
./run.sh --vfs examples/vfs/bad_base64.csv --script examples/startup/stage3.txt < /dev/null
echo "exit code: $?"

echo "=== VFS: bad_parent.csv ==="
./run.sh --vfs examples/vfs/bad_parent.csv --script examples/startup/stage3.txt < /dev/null
echo "exit code: $?"
