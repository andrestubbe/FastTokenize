# FastTokenize Reference Manual

## Core API

### `FastTokenize`
The central facade and factory entry point for high-performance source code tokenization.

```java
import fasttokenize.FastTokenize;
import fasttokenize.Language;
import fasttokenize.Token;
import java.util.List;

// 1. Tokenize code string for a specified language
List<Token> tokens = FastTokenize.tokenize(Language.JAVA, codeString);

// 2. Automatically resolve language from filename
List<Token> fileTokens = FastTokenize.tokenizeForFile("sample_cpp.cpp", cppCode);

// 3. Generate zero-allocation style byte-array for CreamCLI / FastTerminal
byte[] styleIds = FastTokenize.tokenizeStyles(Language.PYTHON, pyCode);
```

---

## Token Model

### `Token`
Immutable token representation containing text content, type classification, and character index boundaries.

| Method | Return Type | Description |
| :--- | :--- | :--- |
| `getType()` | `TokenType` | Gets the enum token classification type. |
| `getText()` | `CharSequence` | Gets the sliced text sequence of the token. |
| `getStart()` | `int` | Character start index offset (inclusive). |
| `getEnd()` | `int` | Character end index offset (exclusive). |
| `getLength()` | `int` | Total character count of the token. |

---

## Token Types

### `TokenType`
Compact set of 15 fundamental token types mapped to 1-byte IDs for high-speed terminal rendering:

```java
public enum TokenType {
    KEYWORD(0),
    TYPE(1),
    IDENTIFIER(2),
    STRING(3),
    NUMBER(4),
    COMMENT(5),
    OPERATOR(6),
    PUNCTUATION(7),
    WHITESPACE(8),
    ANNOTATION(9),
    PREPROCESSOR(10),
    TAG(11),
    ATTRIBUTE(12),
    PROPERTY(13),
    UNKNOWN(14);
}
```

---

## Language Enum

### `Language`
Supported programming languages and data formats with automatic file extension matching.

- `Language.JAVA` (`.java`, `.kt`)
- `Language.CPP` (`.c`, `.cpp`, `.h`, `.hpp`, `.cc`, `.cxx`)
- `Language.PYTHON` (`.py`, `.pyw`, `.pyi`)
- `Language.CSHARP` (`.cs`, `.csx`)
- `Language.JAVASCRIPT` (`.js`, `.jsx`, `.ts`, `.tsx`, `.mjs`, `.cjs`)
- `Language.JSON` (`.json`)
- `Language.CSS` (`.css`, `.scss`, `.less`)
- `Language.XML` (`.xml`, `.html`, `.svg`, `.fxml`)
- `Language.MARKDOWN` (`.md`, `.markdown`)
- `Language.PLAIN_TEXT` (`.txt`)
