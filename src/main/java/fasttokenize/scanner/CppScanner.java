package fasttokenize.scanner;

import fasttokenize.Token;
import fasttokenize.TokenType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * High-performance lexical scanner for C, C++, and Header files.
 */
public class CppScanner implements CodeScanner {

    private static final Set<String> KEYWORDS = Set.of(
        "auto", "break", "case", "catch", "class", "const", "constexpr", "continue",
        "default", "delete", "do", "else", "enum", "explicit", "export", "extern",
        "for", "friend", "goto", "if", "inline", "namespace", "new", "noexcept",
        "operator", "private", "protected", "public", "register", "return", "sizeof",
        "static", "struct", "switch", "template", "this", "throw", "try", "typedef",
        "typeid", "typename", "union", "using", "virtual", "volatile", "while"
    );

    private static final Set<String> TYPES = Set.of(
        "int", "long", "double", "float", "char", "bool", "void", "short", "signed",
        "unsigned", "size_t", "uint8_t", "uint16_t", "uint32_t", "uint64_t", "int8_t",
        "int16_t", "int32_t", "int64_t", "uintptr_t", "intptr_t", "std", "string", "vector"
    );

    @Override
    public List<Token> scan(CharSequence source) {
        List<Token> tokens = new ArrayList<>();
        int len = source.length();
        int i = 0;

        while (i < len) {
            char c = source.charAt(i);

            // WHITESPACE
            if (Character.isWhitespace(c)) {
                int start = i++;
                while (i < len && Character.isWhitespace(source.charAt(i))) i++;
                tokens.add(new Token(TokenType.WHITESPACE, source.subSequence(start, i), start, i));
                continue;
            }

            // PREPROCESSOR DIRECTIVES (#include, #define, #ifdef, etc.)
            if (c == '#') {
                int start = i++;
                while (i < len && source.charAt(i) != '\n') {
                    if (source.charAt(i) == '\\' && i + 1 < len) i += 2;
                    else i++;
                }
                tokens.add(new Token(TokenType.PREPROCESSOR, source.subSequence(start, i), start, i));
                continue;
            }

            // COMMENTS
            if (c == '/' && i + 1 < len) {
                char next = source.charAt(i + 1);
                if (next == '/') {
                    int start = i;
                    i += 2;
                    while (i < len && source.charAt(i) != '\n') i++;
                    tokens.add(new Token(TokenType.COMMENT, source.subSequence(start, i), start, i));
                    continue;
                } else if (next == '*') {
                    int start = i;
                    i += 2;
                    while (i + 1 < len && !(source.charAt(i) == '*' && source.charAt(i + 1) == '/')) i++;
                    i = Math.min(i + 2, len);
                    tokens.add(new Token(TokenType.COMMENT, source.subSequence(start, i), start, i));
                    continue;
                }
            }

            // STRINGS & CHARACTER LITERALS
            if (c == '"' || c == '\'') {
                int start = i++;
                char quote = c;
                while (i < len) {
                    char ch = source.charAt(i);
                    if (ch == '\\' && i + 1 < len) i += 2;
                    else if (ch == quote) { i++; break; }
                    else i++;
                }
                tokens.add(new Token(TokenType.STRING, source.subSequence(start, i), start, i));
                continue;
            }

            // NUMBERS
            if (Character.isDigit(c) || (c == '.' && i + 1 < len && Character.isDigit(source.charAt(i + 1)))) {
                int start = i++;
                while (i < len) {
                    char ch = source.charAt(i);
                    if (Character.isLetterOrDigit(ch) || ch == '.' || ch == '_' || ch == '-' || ch == '+') i++;
                    else break;
                }
                tokens.add(new Token(TokenType.NUMBER, source.subSequence(start, i), start, i));
                continue;
            }

            // IDENTIFIERS / KEYWORDS / TYPES
            if (Character.isJavaIdentifierStart(c)) {
                int start = i++;
                while (i < len && (Character.isJavaIdentifierPart(source.charAt(i)) || source.charAt(i) == '_')) i++;
                CharSequence text = source.subSequence(start, i);
                String s = text.toString();

                TokenType type = KEYWORDS.contains(s) ? TokenType.KEYWORD :
                                TYPES.contains(s) ? TokenType.TYPE : TokenType.IDENTIFIER;
                tokens.add(new Token(type, text, start, i));
                continue;
            }

            // OPERATORS
            if ("+-*/=%!&|^<>?:".indexOf(c) >= 0) {
                int start = i++;
                while (i < len && "+-*/=%!&|^<>?:".indexOf(source.charAt(i)) >= 0) i++;
                tokens.add(new Token(TokenType.OPERATOR, source.subSequence(start, i), start, i));
                continue;
            }

            // PUNCTUATION
            if ("{}()[];,.".indexOf(c) >= 0) {
                int start = i++;
                tokens.add(new Token(TokenType.PUNCTUATION, source.subSequence(start, i), start, i));
                continue;
            }

            int start = i++;
            tokens.add(new Token(TokenType.UNKNOWN, source.subSequence(start, i), start, i));
        }

        return tokens;
    }
}
