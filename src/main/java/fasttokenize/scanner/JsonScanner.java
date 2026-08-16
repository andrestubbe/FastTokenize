package fasttokenize.scanner;

import fasttokenize.Token;
import fasttokenize.TokenType;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * High-performance lexical scanner for JSON data structures.
 */
public class JsonScanner implements CodeScanner {

    private static final Set<String> KEYWORDS = Set.of("true", "false", "null");

    @Override
    public List<Token> scan(CharSequence source) {
        List<Token> tokens = new ArrayList<>();
        int len = source.length();
        int i = 0;
        boolean expectKey = false;

        while (i < len) {
            char c = source.charAt(i);

            // WHITESPACE
            if (Character.isWhitespace(c)) {
                int start = i++;
                while (i < len && Character.isWhitespace(source.charAt(i))) i++;
                tokens.add(new Token(TokenType.WHITESPACE, source.subSequence(start, i), start, i));
                continue;
            }

            // JSON STRINGS (Keys vs String Values)
            if (c == '"') {
                int start = i++;
                while (i < len) {
                    char ch = source.charAt(i);
                    if (ch == '\\' && i + 1 < len) i += 2;
                    else if (ch == '"') { i++; break; }
                    else i++;
                }
                
                // Peek next non-whitespace character to see if it's a property key
                int peek = i;
                while (peek < len && Character.isWhitespace(source.charAt(peek))) peek++;
                boolean isKey = (peek < len && source.charAt(peek) == ':');
                
                TokenType type = isKey ? TokenType.PROPERTY : TokenType.STRING;
                tokens.add(new Token(type, source.subSequence(start, i), start, i));
                continue;
            }

            // NUMBERS
            if (c == '-' || Character.isDigit(c)) {
                int start = i++;
                while (i < len) {
                    char ch = source.charAt(i);
                    if (Character.isDigit(ch) || ch == '.' || ch == 'e' || ch == 'E' || ch == '+' || ch == '-') i++;
                    else break;
                }
                tokens.add(new Token(TokenType.NUMBER, source.subSequence(start, i), start, i));
                continue;
            }

            // KEYWORDS (true, false, null)
            if (Character.isLetter(c)) {
                int start = i++;
                while (i < len && Character.isLetter(source.charAt(i))) i++;
                CharSequence text = source.subSequence(start, i);
                String s = text.toString();

                TokenType type = KEYWORDS.contains(s) ? TokenType.KEYWORD : TokenType.IDENTIFIER;
                tokens.add(new Token(type, text, start, i));
                continue;
            }

            // PUNCTUATION
            if ("{}[]:,".indexOf(c) >= 0) {
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
