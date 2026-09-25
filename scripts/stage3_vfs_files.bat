@echo off
chcp 65001 > nul
setlocal
rem Этап 3: VFS с несколькими файлами (в том числе двоичным).
cd /d "%~dp0.."

echo === VFS: files.csv ===
call run.bat --vfs examples\vfs\files.csv --script examples\startup\stage3.txt < nul
echo exit code: %errorlevel%
