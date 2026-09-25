@echo off
chcp 65001 > nul
setlocal
rem Этап 3: минимальная VFS (только корневой каталог).
cd /d "%~dp0.."

echo === VFS: minimal.csv ===
call run.bat --vfs examples\vfs\minimal.csv --script examples\startup\stage3.txt < nul
echo exit code: %errorlevel%
