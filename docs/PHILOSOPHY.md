# FastTokenize Philosophy

FastTokenize is engineered around three core principles:

1. **$O(n)$ Single-Pass Scanning**: Zero-backtracking lexical analysis that scans source code in a single linear pass, eliminating regular expression backtracking overhead.
2. **Zero-Allocation Byte-Array Outputs**: Direct memory style ID arrays (`byte[]`) mapping 1-to-1 with character offsets for zero-GC TUI rendering in `FastTerminal` and CreamCLI.
3. **Hardware Acceleration with Safe Fallback**: Leverages 256-bit AVX2 SIMD instructions on Windows x64 (`fasttokenize.dll`) while maintaining 100% pure Java fallbacks for seamless execution on macOS and Linux.
