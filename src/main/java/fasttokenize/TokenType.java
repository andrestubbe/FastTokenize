package fasttokenize;

/**
 * Fundamental token types for code analysis, syntax highlighting, and LLM pipelines.
 */
public enum TokenType {
    KEYWORD,
    TYPE,
    IDENTIFIER,
    STRING,
    NUMBER,
    COMMENT,
    OPERATOR,
    PUNCTUATION,
    WHITESPACE,
    ANNOTATION,
    PREPROCESSOR,
    TAG,
    ATTRIBUTE,
    PROPERTY,
    METHOD,
    FIELD,
    CONSTANT,
    THIS,
    UNKNOWN;

    public byte toByteId() {
        return (byte) ordinal();
    }
}
