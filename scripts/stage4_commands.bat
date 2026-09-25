@echo off
chcp 65001 > nul
setlocal
rem Этап 4: стартовый скрипт с командами ls, cd, rev, clear и примеры ошибок.
cd /d "%~dp0.."

echo === Stage 4: all commands ===
call run.bat --vfs examples\vfs\deep.csv --script examples\startup\stage4.txt < nul

for %%s in (examples\startup\errors\stage4_*.txt) do (
    echo.
    echo === Error example: %%s ===
    call run.bat --vfs examples\vfs\deep.csv --script %%s < nul
)
