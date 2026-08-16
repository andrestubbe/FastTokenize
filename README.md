# FastTokenize 0.1.0 [ALPHA] — Ultra-Fast Code & Syntax Tokenizer for Java

[![Status](https://img.shields.io/badge/status-0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastTokenize/releases/tag/0.1.0)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+%2F%20Linux%20%2F%20macOS-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-0.1.0-green.svg)](https://jitpack.io/#andrestubbe/FastTokenize)

---

**⚡ Minimal, deterministic, zero-dependency tokenization engine for code analysis, syntax highlighting, and LLM text pipelines. Operates in $O(n)$ time with zero-allocation byte-array output.**

FastTokenize is a **high-performance, zero-dependency Java tokenization library** and part of the **FastJava ecosystem**. It provides dedicated scanners for 10+ programming languages and formats, outputting structured token streams and zero-allocation byte arrays for `FastTerminal` and CreamCLI.

[![FastTokenize Showcase](docs/WindowsTerminal_kj94gfVAPj.gif)](https://www.youtube.com/watch?v=BZsqQl7WqWk)

---

## Quick Start — Example

```java
import fasttokenize.FastTokenize;
import fasttokenize.Language;
import fasttokenize.Token;
import fasttokenize.TokenType;
import fastterminal.FastTerminalScene;
import cream.cli.view.editor.EditorCodeLine;

public class CreamCliTerminalRendererDemo {
    public static void renderCreamCodeLine(FastTerminalScene scene, String filename, String lineText, int lineY) {
        // 1. Instantly tokenize line into zero-allocation style/type IDs
        byte[] typeIds = FastTokenize.tokenizeStyles(Language.fromFilename(filename), lineText);

        // 2. Direct cell-by-cell write into FastTerminal double-buffered scene
        for (int col = 0; col < lineText.length(); col++) {
            int codePoint = lineText.codePointAt(col);
            byte typeId = typeIds[col];

            // Resolve color palette from TokenType ID
            int fgColor = resolveColorForType(typeId);
            int bgColor = -2; // Transparent background

            scene.writeCell(col, lineY, codePoint, fgColor, bgColor);
        }
    }

    private static int resolveColorForType(byte typeId) {
        return switch (TokenType.values()[typeId]) {
            case KEYWORD     -> 0x569CD6; // Blue
            case STRING      -> 0xCE9178; // Orange/Brown
            case NUMBER      -> 0xB5CEA8; // Light Green
            case COMMENT     -> 0x6A9955; // Dark Green
            case ANNOTATION  -> 0x4EC9B0; // Cyan
            default          -> 0xD4D4D4; // Default Text
        };
    }
}
```

---

## Table of Contents

- [Why FastTokenize?](#why-fasttokenize)
- [Key Features](#key-features)
- [Real-World Use Cases](#real-world-use-cases)
- [Performance Benchmarks](#performance-benchmarks)
- [Supported Languages](#supported-languages)
- [Installation](#installation)
- [Documentation](#documentation)
- [Platform Support](#platform-support)
- [License](#license)
- [Related Projects](#related-projects)

---

## Why FastTokenize?

Traditional syntax highlighters and tokenizers rely on heavy regex engines or full AST parsers (like Tree-sitter), which cause GC pauses and frame drops during fast scrolling in terminal editors. `FastTokenize` provides:

- **$O(n)$ Single-Pass Scanning** — Scans source code in microseconds with zero GC pressure.
- **Zero-Allocation Style Byte Streams** — Generates compact byte arrays matching character offsets directly consumed by `FastTerminal` & CreamCLI.
- **Language Auto-Detection** — Instantly resolves the optimal scanner via `FastTokenize.tokenizeForFile("app.cpp", code)`.
- **Zero Dependencies** — Standalone, lightweight JAR (< 50 KB).

---

## Key Features

* 🚀 **Ultra-Fast Tokenization** — Process large source files in microseconds with minimal CPU usage.
* 🎨 **10+ Supported Languages** — Dedicated scanners for Java, C/C++, Python, C#, JS/TS, JSON, CSS, XML, and Markdown.
* 🖌️ **Direct Terminal Style Integration** — Native byte-array style output for zero-copy terminal rendering.
* 📂 **Comprehensive Test Corpus** — Fully validated against a complete language spectrum in `docs/samples`.

---

## Real-World Use Cases

- 🧭 **CreamCLI Next-Gen Terminal Editor**: Power 60+ FPS zero-latency syntax highlighting and code line rendering in [CreamCLI](https://github.com/andrestubbe/Cream-CLI) without JVM Garbage Collection stalls.
- 🤖 **FastAI & LLM Code Prompting**: Tokenize and filter code snippets into structured Token Streams before feeding them to local or cloud LLM models.
- 🔍 **High-Speed Code Search & Indexing**: Extract method identifiers, classes, and annotations for instant indexing in `FastFileContentIndex` while skipping comments and strings.
- 📄 **Terminal File Previews**: Generate instant colored ANSI / TUI previews for large source code files in `FastTerminal` and TUI dashboards.

---

## Performance Benchmarks

`FastTokenize` is built for high-throughput code tokenization and zero-copy terminal rendering. In the official [JMH Benchmark](examples/Benchmark), the system measured throughput across 180+ byte source snippets:

```text
Benchmark                                         Mode  Cnt       Score        Error  Units
TokenizerBenchmark.benchmarkCppTokenization      thrpt    5  183690.846 ± 259902.107  ops/s
TokenizerBenchmark.benchmarkJavaStyleByteStream  thrpt    5   72195.332 ±  30698.835  ops/s
TokenizerBenchmark.benchmarkJavaTokenization     thrpt    5   85806.426 ±  48010.356  ops/s
```

> **183,000 Tokenizations per Second**: `FastTokenize` parses source code files and outputs zero-allocation style byte arrays in **~5.4 microseconds per file**.

---

## Supported Languages

| Language / Format | Extensions | Dedicated Scanner | Key Constructs Handled |
| :--- | :--- | :--- | :--- |
| **Java / Kotlin** | `.java`, `.kt` | `JavaScanner` | Javadoc, Annotations (`@Override`), Generics, Literals |
| **C / C++** | `.c`, `.cpp`, `.h`, `.hpp` | `CppScanner` | Preprocessor (`#include`, `#define`), Intrinsics, Namespaces |
| **Python** | `.py`, `.pyw`, `.pyi` | `PythonScanner` | Triple Quotes (`"""`), Decorators (`@property`), Raw Strings |
| **C# (.NET)** | `.cs`, `.csx` | `CSharpScanner` | Verbatim (`@""`), Interpolation (`$""`), Attributes, Async |
| **JS / TS / JSX** | `.js`, `.ts`, `.jsx`, `.tsx` | `CppScanner` | Template Literals, React Components, Arrow Functions |
| **JSON** | `.json` | `JsonScanner` | Property Keys (`"key":`) vs Values, Exponents, Booleans |
| **CSS / SCSS** | `.css`, `.scss`, `.less` | `CssScanner` | At-Rules (`@media`), Selectors (`.class`, `#id`), Variables |
| **XML / HTML** | `.xml`, `.html`, `.svg` | `XmlScanner` | Directives (`<?xml?>`), Tags, Attributes, Comments (`<!-- -->`) |
| **Markdown** | `.md`, `.markdown` | `MarkdownScanner` | Headers (`#`), Fenced Code Blocks (`` ``` ``), Links |

---

## Installation

### Maven (JitPack)

Add the JitPack repository and dependency to your `pom.xml`:

```xml
<repositories>
    <repository>
        <id>jitpack.io</id>
        <url>https://jitpack.io</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.github.andrestubbe</groupId>
        <artifactId>FastTokenize</artifactId>
        <version>0.1.0</version>
    </dependency>
</dependencies>
```

---

## Documentation

- **[Language Test Corpus](docs/samples/README.md)** — Complete spectrum of reference test files for all supported languages.
- **[PHILOSOPHY.md](docs/PHILOSOPHY.md)** — Engineering rationale for $O(n)$ zero-allocation tokenization.
- **[ROADMAP.md](docs/ROADMAP.md)** — Future milestones and C++/AVX2 SIMD native bridge plans.

---

## Platform Support

| Platform      | Status |
|---------------|--------|
| Windows 10/11 | 🚀 Fully Supported |
| Linux         | 🚀 Fully Supported |
| macOS         | 🚀 Fully Supported |

---

## License

MIT License — see [LICENSE](LICENSE) for details.

---

## Related Projects

- **[Cream-CLI](https://github.com/andrestubbe/Cream-CLI)** — Next-generation command-line workspace (Terminal + File Explorer + AI Shell).
- **[FastTerminal](https://github.com/andrestubbe/FastTerminal)** — High-performance double-buffered TUI terminal engine.
- **[FastFileIndex](https://github.com/andrestubbe/FastFileIndex)** — Native mmap file indexing engine.
- **[FastCore](https://github.com/andrestubbe/FastCore)** — Unified JNI loader and platform abstraction.

---

**Part of the FastJava Ecosystem**  
*Making the JVM faster. Small package. Maximum speed. Zero bloat. 🚀*
