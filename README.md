# FastTokenizer v0.1.0 [ALPHA] — Lightweight Source Code Analysis for Java

[![Status](https://img.shields.io/badge/status-v0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastTokenizer/releases/tag/v0.1.0)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe)

**⚡ Minimal, deterministic tokenizer for code and text structures. Zero‑dependency, O(n), with a small and stable
TokenType set and language‑specific scanners (Java/JSON/XML). Designed for fast preview and analysis pipelines.**

[![FastKeyboard Showcase](docs/screenshot.png)](https://www.youtube.com/watch?v=BZsqQl7WqWk)


---

## Languages (Backends)

| Language   | Scanner       | Status     |
|------------|---------------|------------|
| Java       | SimpleScanner | ✅ Baseline |
| JSON       | Deterministic | 🚧 Planned |
| XML        | Tag-based     | 🚧 Planned |
| Markdown   | Inline AST    | 🚧 Planned |
| Plain Text | Gutter/Words  | ✅ Standard |

---

## Architecture

FastTokenizer follows the principle of **Structure over Grammar**:
`Source → CharScanner → TokenStream → FastPreview`

For details on the roadmap and Emoji policy, see [ROADMAP.md](ROADMAP.md).

---

## License

MIT License — See [LICENSE](LICENSE) file for details.

---

## Related Projects

- [FastCore](https://github.com/andrestubbe/FastCore) — Native Library Loader for Java
- [FastKeyboard](https://github.com/andrestubbe/FastKeyboard) — High-performance RawInput engine
- [FastTheme](https://github.com/andrestubbe/FastTheme) — Advanced UI styling engine

---
**Part of the FastJava Ecosystem** — *Making the JVM faster.*

