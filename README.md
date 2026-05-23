# FastTokenizer — Lightweight Source Code Analysis [v0.1.0]

**Minimaler, deterministischer Tokenizer für Code- und Textstrukturen. Zero-Dependency, O(n), kleines stabiles TokenType-Set, sprachspezifische Scanner (Java/JSON/XML). Entwickelt für schnelle Preview- und Analyse-Pipelines.**

[![Build](https://img.shields.io/github/actions/workflow/status/andrestubbe/FastTokenizer/maven.yml?branch=main)](https://github.com/andrestubbe/FastTokenizer/actions)
[![Java](https://img.shields.io/badge/Java-17+-blue.svg)](https://www.java.com)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![JitPack](https://jitpack.io/v/andrestubbe/FastTokenizer.svg)](https://jitpack.io/#andrestubbe/FastTokenizer)

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
