@echo off
chcp 65001 > nul
setlocal
rem Этап 5: стартовый скрипт с командой cp и примеры ошибок.
cd /d "%~dp0.."

echo === Stage 5: cp ===
call run.bat --vfs examples\vfs\deep.csv --script examples\startup\stage5.txt < nul

for %%s in (examples\startup\errors\stage5_*.txt) do (
    echo.
    echo === Error example: %%s ===
    call run.bat --vfs examples\vfs\deep.csv --script %%s < nul
)
