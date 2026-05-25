# Roadmap - FastTokenizer v0.1.0

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

## 1. Language Support

- [ ] **Java**: Complete the keyword set and handle edge cases (multi-line strings, local variables).
- [ ] **JSON**: Deterministic scanner for keys, values, and nesting.
- [ ] **XML/HTML**: Tag and attribute identification.
- [ ] **Markdown**: Inline tokenizer for Bold, Italic, Links, and Code blocks.

## 2. Core Features

- [ ] **Emoji Strategy**: Implement a minimalist Unicode handling policy (Tokenization vs. Preservation).
- [ ] **Off-heap CharSequence**: Optimize scanner to work directly on memory-mapped file buffers.
- [ ] **Streaming API**: Support for tokenizing massive files without loading them fully into the heap.

## 3. Integration

- [ ] **FastPreview Bridge**: Standardized interface for the rendering pipeline.
- [ ] **FastTheme Mapping**: TokenType to Color/Style mapping tables.

---
*FastTokenizer: Structure first, colors later.*
