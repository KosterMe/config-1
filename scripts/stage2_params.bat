@echo off
chcp 65001 > nul
setlocal
rem Этап 2: запуск эмулятора с разными наборами параметров.
cd /d "%~dp0.."

echo === 1. No options ===
call run.bat < nul
echo exit code: %errorlevel%

echo === 2. Only --vfs ===
call run.bat --vfs examples\vfs\deep.csv < nul
echo exit code: %errorlevel%

echo === 3. Only --script ===
call run.bat --script examples\startup\stage2.txt < nul
echo exit code: %errorlevel%

echo === 4. --vfs and --script ===
call run.bat --vfs examples\vfs\deep.csv --script examples\startup\stage2.txt < nul
echo exit code: %errorlevel%
