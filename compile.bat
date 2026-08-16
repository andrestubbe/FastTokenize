@echo off
setlocal enabledelayedexpansion

echo ===================================================
echo   FastTokenize Native DLL Builder (AVX2 / MSVC)
echo ===================================================

:: Detect Visual Studio MSVC environment
if defined VCINSTALLDIR goto build

if exist "C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Auxiliary\Build\vcvars64.bat" (
    call "C:\Program Files\Microsoft Visual Studio\2022\Community\VC\Auxiliary\Build\vcvars64.bat" >nul 2>&1
    goto build
)

if exist "C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\VC\Auxiliary\Build\vcvars64.bat" (
    call "C:\Program Files (x86)\Microsoft Visual Studio\2019\Community\VC\Auxiliary\Build\vcvars64.bat" >nul 2>&1
    goto build
)

echo [ERROR] Visual Studio x64 C++ compiler (cl.exe) not found!
exit /b 1

:build
if not exist "build" mkdir build
if not exist "src\main\resources\win32-x86-64" mkdir "src\main\resources\win32-x86-64"

set JAVA_INCLUDE="%JAVA_HOME%\include"
set JAVA_INCLUDE_WIN="%JAVA_HOME%\include\win32"

echo [INFO] Compiling C++/AVX2 native library fasttokenize.dll...
cl.exe /O2 /arch:AVX2 /LD /I%JAVA_INCLUDE% /I%JAVA_INCLUDE_WIN% native\src\fasttokenize_simd.cpp /Fe:build\fasttokenize.dll /Fo:build\

if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed!
    exit /b 1
)

copy /y build\fasttokenize.dll src\main\resources\win32-x86-64\fasttokenize.dll >nul 2>&1
echo [SUCCESS] Built fasttokenize.dll and copied to src/main/resources/win32-x86-64/
