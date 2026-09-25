@echo off
chcp 65001 > nul
setlocal
rem Этап 3: ошибки загрузки VFS (файл не найден, неверный формат).
cd /d "%~dp0.."

echo === VFS: missing.csv ===
call run.bat --vfs examples\vfs\missing.csv --script examples\startup\stage3.txt < nul
echo exit code: %errorlevel%

echo === VFS: bad_header.csv ===
call run.bat --vfs examples\vfs\bad_header.csv --script examples\startup\stage3.txt < nul
echo exit code: %errorlevel%

echo === VFS: bad_fields.csv ===
call run.bat --vfs examples\vfs\bad_fields.csv --script examples\startup\stage3.txt < nul
echo exit code: %errorlevel%

echo === VFS: bad_type.csv ===
call run.bat --vfs examples\vfs\bad_type.csv --script examples\startup\stage3.txt < nul
echo exit code: %errorlevel%

echo === VFS: bad_base64.csv ===
call run.bat --vfs examples\vfs\bad_base64.csv --script examples\startup\stage3.txt < nul
echo exit code: %errorlevel%

echo === VFS: bad_parent.csv ===
call run.bat --vfs examples\vfs\bad_parent.csv --script examples\startup\stage3.txt < nul
echo exit code: %errorlevel%
