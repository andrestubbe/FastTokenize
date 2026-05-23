# FastTokenizer — Lightweight Source Code Analysis [v0.1.0]

**Minimaler, deterministischer Tokenizer für Code- und Textstrukturen. Zero-Dependency, O(n), kleines stabiles TokenType-Set, sprachspezifische Scanner (Java/JSON/XML). Entwickelt für schnelle Preview- und Analyse-Pipelines.**

[![Status](https://img.shields.io/badge/status-v0.1.0-brightgreen.svg)](https://github.com/andrestubbe/FastTokenizer/releases/tag/v0.1.0)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![Platform](https://img.shields.io/badge/Platform-Windows%2010+-lightgrey.svg)]()
[![JitPack](https://img.shields.io/badge/JitPack-ready-green.svg)](https://jitpack.io/#andrestubbe)

---

## Languages (Backends)

| Language | Scanner | Status |
|----------|---------|--------|
| Java | SimpleScanner | ✅ Baseline |
| JSON | Deterministic | 🚧 Planned |
| XML | Tag-based | 🚧 Planned |
| Markdown | Inline AST | 🚧 Planned |
| Plain Text | Gutter/Words | ✅ Standard |

---

## Architecture
FastTokenizer follows the principle of **Structure over Grammar**:
`Source → CharScanner → TokenStream → FastPreview`

For details on the roadmap and Emoji policy, see [ROADMAP.md](ROADMAP.md).

## License
MIT
