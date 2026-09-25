@echo off
chcp 65001 > nul
setlocal
rem Этап 3: VFS с тремя и более уровнями файлов и каталогов.
cd /d "%~dp0.."

echo === VFS: deep.csv ===
call run.bat --vfs examples\vfs\deep.csv --script examples\startup\stage3.txt < nul
echo exit code: %errorlevel%
