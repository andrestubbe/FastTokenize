# FastTokenize Compilation Guide

## Building FastTokenize JAR

Build the standalone Java library using Apache Maven:

```bash
mvn clean package -DskipTests
```

The compiled output will be generated at:
`target/FastTokenize-0.1.0.jar`

---

## Compiling Native C++/AVX2 SIMD Library

To compile the optional native hardware acceleration library (`fasttokenize.dll` on Windows x64):

1. Open **Visual Studio x64 Native Tools Command Prompt**.
2. Run the build script:

```cmd
compile.bat
```

This compiles `native/src/fasttokenize_simd.cpp` with `/arch:AVX2` optimization and outputs `src/main/resources/win32-x86-64/fasttokenize.dll`.
