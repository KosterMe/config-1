@echo off
chcp 65001 > nul
setlocal
rem Этап 2: выполнение стартового скрипта (остановка на ошибке, команда exit).
cd /d "%~dp0.."

echo === 1. Script stops at the first error ===
call run.bat --script examples\startup\stage2_error.txt < nul
echo exit code: %errorlevel%

echo === 2. exit in the script ===
call run.bat --script examples\startup\stage2_exit.txt < nul
echo exit code: %errorlevel%
