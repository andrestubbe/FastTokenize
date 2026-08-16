package fasttokenize.scanner;

import fasttokenize.Token;
import fasttokenize.TokenType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * High-performance lexical scanner for Java and Kotlin.
 * Matches CreamCLI JavaSyntaxHighlighter logic with METHOD, THIS, CONSTANT, and FIELD awareness.
 */
public class JavaScanner implements CodeScanner {

    private static final Set<String> KEYWORDS = Set.of(
        "abstract", "assert", "boolean", "break", "byte", "case", "catch", "char",
        "class", "const", "continue", "default", "do", "double", "else", "enum",
        "extends", "final", "finally", "float", "for", "goto", "if", "implements",
        "import", "instanceof", "int", "interface", "long", "native", "new", "package",
        "private", "protected", "public", "return", "short", "static", "strictfp",
        "super", "switch", "synchronized", "this", "throw", "throws", "transient",
        "try", "void", "volatile", "while", "record", "sealed", "non-sealed", "permits",
        "val", "var", "fun", "when", "object"
    );

    private static final Set<String> TYPES = Set.of(
        "String", "Object", "Integer", "Long", "Double", "Float", "Boolean",
        "Byte", "Character", "Short", "List", "Set", "Map", "Class", "System", "Math"
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

            // STRINGS
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

            // ANNOTATION
            if (c == '@' && i + 1 < len && Character.isJavaIdentifierStart(source.charAt(i + 1))) {
                int start = i++;
                while (i < len && Character.isJavaIdentifierPart(source.charAt(i))) i++;
                tokens.add(new Token(TokenType.ANNOTATION, source.subSequence(start, i), start, i));
                continue;
            }

            // IDENTIFIER / KEYWORD / TYPE / METHOD / THIS / CONSTANT / FIELD
            if (Character.isJavaIdentifierStart(c)) {
                int start = i++;
                while (i < len && Character.isJavaIdentifierPart(source.charAt(i))) i++;
                CharSequence text = source.subSequence(start, i);
                String s = text.toString();

                int nextIdx = i;
                while (nextIdx < len && Character.isWhitespace(source.charAt(nextIdx))) nextIdx++;
                boolean isMethodCall = (nextIdx < len && source.charAt(nextIdx) == '(') || (start >= 2 && source.subSequence(start - 2, start).toString().equals("::"));
                boolean isThisAccess = (start >= 5 && source.subSequence(start - 5, start).toString().equals("this.")) || (start >= 6 && source.subSequence(start - 6, start).toString().equals("super."));

                TokenType type;
                if (s.equals("this") || s.equals("super")) {
                    type = TokenType.THIS;
                } else if (KEYWORDS.contains(s)) {
                    type = TokenType.KEYWORD;
                } else if (isThisAccess) {
                    type = TokenType.FIELD;
                } else if (isConstantName(s)) {
                    type = TokenType.CONSTANT;
                } else if (isMethodCall && !isControlFlowKeyword(s)) {
                    type = TokenType.METHOD;
                } else if (TYPES.contains(s) || (s.length() > 1 && Character.isUpperCase(s.charAt(0)) && !isConstantName(s))) {
                    type = TokenType.TYPE;
                } else {
                    type = TokenType.IDENTIFIER;
                }

                tokens.add(new Token(type, text, start, i));
                continue;
            }

            // OPERATOR
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

            // UNKNOWN
            int start = i++;
            tokens.add(new Token(TokenType.UNKNOWN, source.subSequence(start, i), start, i));
        }

        return tokens;
    }

    private static boolean isConstantName(String s) {
        if (s == null || s.length() < 2) return false;
        boolean hasUpper = false;
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (Character.isUpperCase(ch)) hasUpper = true;
            else if (!Character.isDigit(ch) && ch != '_') return false;
        }
        return hasUpper;
    }

    private static boolean isControlFlowKeyword(String s) {
        return s.equals("if") || s.equals("for") || s.equals("while") || s.equals("switch") || s.equals("catch");
    }
}
