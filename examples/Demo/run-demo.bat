@echo off
setlocal enabledelayedexpansion

echo Building and running FastTokenize Demo...

cd /d "%~dp0"
call mvn clean package -DskipTests >nul 2>&1

if %errorlevel% neq 0 (
    echo [ERROR] Build failed!
    exit /b 1
)

java -cp "target/fasttokenize-demo-0.1.0.jar;../../target/FastTokenize-0.1.0.jar" fasttokenize.Demo
