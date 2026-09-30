@echo off
setlocal enabledelayedexpansion

cd /d "%~dp0"

echo ===================================================
echo   FastTokenize Native DLL Builder (AVX2 / MSVC)
echo ===================================================

set "VSWHERE=%ProgramFiles(x86)%\Microsoft Visual Studio\Installer\vswhere.exe"
if not exist "%VSWHERE%" set "VSWHERE=%ProgramFiles%\Microsoft Visual Studio\Installer\vswhere.exe"

if exist "%VSWHERE%" (
    for /f "usebackq tokens=*" %%i in (`"%VSWHERE%" -latest -products * -requires Microsoft.VisualStudio.Component.VC.Tools.x86.x64 -property installationPath`) do (
        set "VS_PATH=%%i"
    )
)

if not defined VS_PATH (
    if exist "C:\Program Files\Microsoft Visual Studio\18\Community" set "VS_PATH=C:\Program Files\Microsoft Visual Studio\18\Community"
)

if not defined VS_PATH (
    echo [ERROR] Visual Studio with C++ tools not found!
    exit /b 1
)

if not defined JAVA_HOME (
    if exist "C:\Program Files\Java\jdk-21.0.12.1" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-21.0.12.1"
    ) else if exist "C:\Program Files\Java\latest" (
        set "JAVA_HOME=C:\Program Files\Java\latest"
    ) else if exist "C:\Program Files\Java\jdk-25.0.3" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-25.0.3"
    ) else if exist "C:\Program Files\Java\jdk-21" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-21"
    ) else if exist "C:\Program Files\Java\jdk-17" (
        set "JAVA_HOME=C:\Program Files\Java\jdk-17"
    )
)

if not exist "!JAVA_HOME!\include\jni.h" (
    echo [ERROR] Cannot find jni.h in !JAVA_HOME!\include
    exit /b 1
)

call "!VS_PATH!\VC\Auxiliary\Build\vcvars64.bat"

if not exist "build" mkdir build > nul 2>&1
if not exist "release" mkdir release > nul 2>&1
if not exist "src\main\resources\native" mkdir "src\main\resources\native" > nul 2>&1
if not exist "src\main\resources\win32-x86-64" mkdir "src\main\resources\win32-x86-64" > nul 2>&1
if not exist "target\classes\native" mkdir "target\classes\native" > nul 2>&1
set "FASTCORE_DIR=%USERPROFILE%\.fastcore\native\fasttokenize"
if not exist "!FASTCORE_DIR!" mkdir "!FASTCORE_DIR!" > nul 2>&1

echo [INFO] Compiling C++/AVX2 native library fasttokenize.dll...
cl.exe /nologo /O2 /arch:AVX2 /LD /D_CRT_SECURE_NO_WARNINGS ^
    /I"!JAVA_HOME!\include" ^
    /I"!JAVA_HOME!\include\win32" ^
    native\src\fasttokenize_simd.cpp ^
    /Fo:build\fasttokenize.obj ^
    /link /DLL /OUT:release\fasttokenize.dll user32.lib gdi32.lib

if %errorlevel% neq 0 (
    echo [ERROR] Compilation failed!
    exit /b 1
)

copy /Y release\fasttokenize.dll build\fasttokenize.dll > nul
copy /Y release\fasttokenize.dll src\main\resources\fasttokenize.dll > nul
copy /Y release\fasttokenize.dll src\main\resources\native\fasttokenize.dll > nul
copy /Y release\fasttokenize.dll src\main\resources\win32-x86-64\fasttokenize.dll > nul
copy /Y release\fasttokenize.dll target\classes\native\fasttokenize.dll > nul 2>&1
copy /Y release\fasttokenize.dll target\classes\fasttokenize.dll > nul 2>&1
copy /Y release\fasttokenize.dll "!FASTCORE_DIR!\fasttokenize.dll" > nul
powershell -NoProfile -Command "Unblock-File -Path '!FASTCORE_DIR!\fasttokenize.dll', 'release\fasttokenize.dll', 'src\main\resources\native\fasttokenize.dll' -ErrorAction SilentlyContinue" > nul 2>&1

echo.
echo ===========================================
echo [SUCCESS] FastTokenize native DLL built!
echo Copied to release\, resources\, and .fastcore
echo ===========================================
