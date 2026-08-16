# FastTokenizer Documentation & Language Spectrum Index

> **Directory:** `FastTokenizer/docs/samples/`  
> **Purpose:** Reference test corpus for validating C++/AVX2 SIMD tokenization across all supported programming languages and formats.

---

## 📚 Language Test Corpus Index

Below is the complete spectrum of language test files created for benchmark validation and syntax highlighter parity testing against CreamCLI:

| File Name | Format / Language | Key Syntax Constructs Tested | Link |
| :--- | :--- | :--- | :--- |
| **[`sample_java.java`](file:///c:/Users/andre/Documents/2026-06-14-Work-FastJava/FastTokenizer/docs/samples/sample_java.java)** | Java 17+ | Javadoc, Annotations (`@Deprecated`), Generics, Literals (Hex/Escapes), Control Flow | [sample_java.java](file:///c:/Users/andre/Documents/2026-06-14-Work-FastJava/FastTokenizer/docs/samples/sample_java.java) |
| **[`sample_cpp.cpp`](file:///c:/Users/andre/Documents/2026-06-14-Work-FastJava/FastTokenizer/docs/samples/sample_cpp.cpp)** | C / C++17 | Preprocessor (`#define`, `#ifdef`), Namespaces, Templates, Inline AVX2 Intrinsic Types (`__m256`) | [sample_cpp.cpp](file:///c:/Users/andre/Documents/2026-06-14-Work-FastJava/FastTokenizer/docs/samples/sample_cpp.cpp) |
| **[`sample_python.py`](file:///c:/Users/andre/Documents/2026-06-14-Work-FastJava/FastTokenizer/docs/samples/sample_python.py)** | Python 3.10+ | Type Hints, Decorators (`@property`), Docstrings, Raw Strings (`r"..."`), F-Strings | [sample_python.py](file:///c:/Users/andre/Documents/2026-06-14-Work-FastJava/FastTokenizer/docs/samples/sample_python.py) |
| **[`sample_csharp.cs`](file:///c:/Users/andre/Documents/2026-06-14-Work-FastJava/FastTokenizer/docs/samples/sample_csharp.cs)** | C# 10+ | Namespaces, Interfaces, Verbatim Strings (`@"..."`), String Interpolation (`$"..."`), Async/Await | [sample_csharp.cs](file:///c:/Users/andre/Documents/2026-06-14-Work-FastJava/FastTokenizer/docs/samples/sample_csharp.cs) |
| **[`sample_javascript.ts`](file:///c:/Users/andre/Documents/2026-06-14-Work-FastJava/FastTokenizer/docs/samples/sample_javascript.ts)** | JS / TS / JSX | TS Interfaces, React JSX Tags, Template Literals (``` `${val}` ```), Arrow Functions, Regex Literals | [sample_javascript.ts](file:///c:/Users/andre/Documents/2026-06-14-Work-FastJava/FastTokenizer/docs/samples/sample_javascript.ts) |
| **[`sample_json.json`](file:///c:/Users/andre/Documents/2026-06-14-Work-FastJava/FastTokenizer/docs/samples/sample_json.json)** | JSON / Schema | Nested Objects, Arrays, Booleans, Nulls, Floating Point Numbers | [sample_json.json](file:///c:/Users/andre/Documents/2026-06-14-Work-FastJava/FastTokenizer/docs/samples/sample_json.json) |
| **[`sample_css.css`](file:///c:/Users/andre/Documents/2026-06-14-Work-FastJava/FastTokenizer/docs/samples/sample_css.css)** | CSS3 / SCSS | Custom Variables (`--primary`), Pseudo-classes, `@media` Queries, Color Literals (`#007acc`) | [sample_css.css](file:///c:/Users/andre/Documents/2026-06-14-Work-FastJava/FastTokenizer/docs/samples/sample_css.css) |
| **[`sample_xml.xml`](file:///c:/Users/andre/Documents/2026-06-14-Work-FastJava/FastTokenizer/docs/samples/sample_xml.xml)** | XML / HTML5 | Directives (`<?xml ?>`), Comments (`<!-- -->`), Attributes, Namespaces (`xmlns:xsi`) | [sample_xml.xml](file:///c:/Users/andre/Documents/2026-06-14-Work-FastJava/FastTokenizer/docs/samples/sample_xml.xml) |

---

## 🎯 Coverage Strategy

Every sample file contains **100% of the lexical edge cases** that the `FastTokenizer` AVX2 SIMD engine must tokenize:
1. **Single-line & Multi-line Comments** (including Javadoc / XML Comments).
2. **Escaped & Multi-line Strings** (including verbatim `@""`, raw `r""`, template `` `${}` ``).
3. **Numeric Format Variants** (Decimals, Floats, Hexadecimal `0xDEADBEEF`, Exponents `1.5e-10`).
4. **Language Decorators & Annotations** (`@Override`, `@property`, `[Serializable]`).
5. **Punctuators & Operators** (`=>`, `::`, `->`, `&&`, `||`).
