@echo off
chcp 65001 >nul
cd /d "%~dp0"

echo Building FastTokenize module...
call mvn -q install -DskipTests
if errorlevel 1 (
    echo Failed to build FastTokenize module.
    pause
    exit /b 1
)

echo Running FastTokenize Demo...
cd examples\Demo
call run-demo.bat
cd ..\..
pause
