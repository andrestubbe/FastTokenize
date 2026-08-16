@echo off
setlocal enabledelayedexpansion

echo Building and running FastTokenize Demo...

cd /d "%~dp0"
call mvn clean package -DskipTests >nul 2>&1

if %errorlevel% neq 0 (
    echo [ERROR] Build failed!
    exit /b 1
)

call mvn -q compile exec:java "-Dexec.mainClass=fasttokenize.Demo"
