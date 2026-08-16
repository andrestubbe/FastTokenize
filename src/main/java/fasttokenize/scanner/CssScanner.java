package fasttokenize.scanner;

import fasttokenize.Token;
import fasttokenize.TokenType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * High-performance lexical scanner for CSS, SCSS, and LESS.
 */
public class CssScanner implements CodeScanner {

    private static final Set<String> KEYWORDS = Set.of(
        "@import", "@media", "@keyframes", "@font-face", "@charset", "@supports"
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

            // COMMENTS (/* ... */)
            if (c == '/' && i + 1 < len && source.charAt(i + 1) == '*') {
                int start = i;
                i += 2;
                while (i + 1 < len && !(source.charAt(i) == '*' && source.charAt(i + 1) == '/')) i++;
                i = Math.min(i + 2, len);
                tokens.add(new Token(TokenType.COMMENT, source.subSequence(start, i), start, i));
                continue;
            }

            // STRINGS
            if (c == '"' || c == '\'') {
                int start = i++;
                char quote = c;
                while (i < len && source.charAt(i) != quote) i++;
                if (i < len) i++;
                tokens.add(new Token(TokenType.STRING, source.subSequence(start, i), start, i));
                continue;
            }

            // AT-RULES (@media, @import)
            if (c == '@') {
                int start = i++;
                while (i < len && Character.isLetter(source.charAt(i))) i++;
                tokens.add(new Token(TokenType.KEYWORD, source.subSequence(start, i), start, i));
                continue;
            }

            // CLASS / ID / VARIABLE SELECTORS (.class, #id, --var)
            if (c == '.' || c == '#' || (c == '-' && i + 1 < len && source.charAt(i + 1) == '-')) {
                int start = i++;
                while (i < len && (Character.isLetterOrDigit(source.charAt(i)) || "-_".indexOf(source.charAt(i)) >= 0)) i++;
                TokenType type = (c == '-') ? TokenType.ANNOTATION : TokenType.PROPERTY;
                tokens.add(new Token(type, source.subSequence(start, i), start, i));
                continue;
            }

            // NUMBERS / UNITS (14px, 1.5em, 100%)
            if (Character.isDigit(c)) {
                int start = i++;
                while (i < len && (Character.isLetterOrDigit(source.charAt(i)) || ".%".indexOf(source.charAt(i)) >= 0)) i++;
                tokens.add(new Token(TokenType.NUMBER, source.subSequence(start, i), start, i));
                continue;
            }

            // IDENTIFIERS (PROPERTY / TAG)
            if (Character.isLetter(c)) {
                int start = i++;
                while (i < len && (Character.isLetterOrDigit(source.charAt(i)) || source.charAt(i) == '-')) i++;
                tokens.add(new Token(TokenType.IDENTIFIER, source.subSequence(start, i), start, i));
                continue;
            }

            // PUNCTUATION & OPERATORS
            if ("{}():;,".indexOf(c) >= 0) {
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
