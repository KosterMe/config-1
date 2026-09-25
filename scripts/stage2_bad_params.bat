@echo off
chcp 65001 > nul
setlocal
rem Этап 2: ошибки в параметрах командной строки.
cd /d "%~dp0.."

echo === 1. Unknown option ===
call run.bat --foo bar < nul
echo exit code: %errorlevel%

echo === 2. Option without value ===
call run.bat --script < nul
echo exit code: %errorlevel%

echo === 3. Duplicate option ===
call run.bat --vfs a.csv --vfs b.csv < nul
echo exit code: %errorlevel%

echo === 4. Script file not found ===
call run.bat --script examples\startup\missing.txt < nul
echo exit code: %errorlevel%
