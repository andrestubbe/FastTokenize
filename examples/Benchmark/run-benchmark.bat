@echo off
setlocal enabledelayedexpansion

echo Building FastTokenize JMH Benchmark...

cd /d "%~dp0"
call mvn clean package -DskipTests >nul 2>&1

if %errorlevel% neq 0 (
    echo [ERROR] Benchmark build failed!
    exit /b 1
)

echo Running JMH Benchmark...
java -cp "target/benchmarks.jar;../../target/FastTokenize-0.1.0.jar" org.openjdk.jmh.Main TokenizerBenchmark
