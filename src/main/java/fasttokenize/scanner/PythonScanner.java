package fasttokenize.scanner;

import fasttokenize.Token;
import fasttokenize.TokenType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * High-performance lexical scanner for Python.
 */
public class PythonScanner implements CodeScanner {

    private static final Set<String> KEYWORDS = Set.of(
        "and", "as", "assert", "async", "await", "break", "class", "continue",
        "def", "del", "elif", "else", "except", "finally", "for", "from",
        "global", "if", "import", "in", "is", "lambda", "nonlocal", "not",
        "or", "pass", "raise", "return", "try", "while", "with", "yield",
        "True", "False", "None"
    );

    private static final Set<String> TYPES = Set.of(
        "int", "float", "str", "bool", "list", "dict", "set", "tuple", "bytes",
        "object", "type", "Optional", "List", "Dict", "Set", "Tuple", "Union", "Any"
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

            // COMMENTS (#)
            if (c == '#') {
                int start = i++;
                while (i < len && source.charAt(i) != '\n') i++;
                tokens.add(new Token(TokenType.COMMENT, source.subSequence(start, i), start, i));
                continue;
            }

            // TRIPLE QUOTE STRINGS (''' or """)
            if ((c == '"' || c == '\'') && i + 2 < len && source.charAt(i + 1) == c && source.charAt(i + 2) == c) {
                int start = i;
                char quote = c;
                i += 3;
                while (i + 2 < len) {
                    if (source.charAt(i) == quote && source.charAt(i + 1) == quote && source.charAt(i + 2) == quote) {
                        i += 3;
                        break;
                    }
                    i++;
                }
                tokens.add(new Token(TokenType.STRING, source.subSequence(start, i), start, i));
                continue;
            }

            // REGULAR STRINGS
            if (c == '"' || c == '\'' || ((c == 'f' || c == 'r' || c == 'b') && i + 1 < len && (source.charAt(i + 1) == '"' || source.charAt(i + 1) == '\''))) {
                int start = i;
                if (c == 'f' || c == 'r' || c == 'b') i++;
                char quote = source.charAt(i++);
                while (i < len) {
                    char ch = source.charAt(i);
                    if (ch == '\\' && i + 1 < len) i += 2;
                    else if (ch == quote) { i++; break; }
                    else i++;
                }
                tokens.add(new Token(TokenType.STRING, source.subSequence(start, i), start, i));
                continue;
            }

            // DECORATORS (@decorator)
            if (c == '@' && i + 1 < len && Character.isJavaIdentifierStart(source.charAt(i + 1))) {
                int start = i++;
                while (i < len && (Character.isJavaIdentifierPart(source.charAt(i)) || source.charAt(i) == '.')) i++;
                tokens.add(new Token(TokenType.ANNOTATION, source.subSequence(start, i), start, i));
                continue;
            }

            // NUMBERS
            if (Character.isDigit(c) || (c == '.' && i + 1 < len && Character.isDigit(source.charAt(i + 1)))) {
                int start = i++;
                while (i < len) {
                    char ch = source.charAt(i);
                    if (Character.isLetterOrDigit(ch) || ch == '.' || ch == '_') i++;
                    else break;
                }
                tokens.add(new Token(TokenType.NUMBER, source.subSequence(start, i), start, i));
                continue;
            }

            // IDENTIFIERS / KEYWORDS / TYPES
            if (Character.isJavaIdentifierStart(c)) {
                int start = i++;
                while (i < len && Character.isJavaIdentifierPart(source.charAt(i))) i++;
                CharSequence text = source.subSequence(start, i);
                String s = text.toString();

                TokenType type = KEYWORDS.contains(s) ? TokenType.KEYWORD :
                                TYPES.contains(s) ? TokenType.TYPE : TokenType.IDENTIFIER;
                tokens.add(new Token(type, text, start, i));
                continue;
            }

            // OPERATORS
            if ("+-*/=%!&|^<>?:~".indexOf(c) >= 0) {
                int start = i++;
                while (i < len && "+-*/=%!&|^<>?:~".indexOf(source.charAt(i)) >= 0) i++;
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
