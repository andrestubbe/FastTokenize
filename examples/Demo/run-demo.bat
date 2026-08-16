@echo off
setlocal enabledelayedexpansion
chcp 65001 >nul
reg add "HKCU\Console" /v VirtualTerminalLevel /t REG_DWORD /d 1 /f >nul 2>&1

echo Building and running FastTokenize Demo...

cd /d "%~dp0"
call mvn clean package -DskipTests >nul 2>&1

if %errorlevel% neq 0 (
    echo [ERROR] Build failed!
    exit /b 1
)

call mvn -q compile exec:java "-Dexec.mainClass=fasttokenize.Demo"
