package fasttokenize.scanner;

import fasttokenize.Token;
import fasttokenize.TokenType;
import java.util.ArrayList;
import java.util.List;

/**
 * High-performance lexical scanner for Markdown (.md).
 */
public class MarkdownScanner implements CodeScanner {

    @Override
    public List<Token> scan(CharSequence source) {
        List<Token> tokens = new ArrayList<>();
        int len = source.length();
        int i = 0;

        while (i < len) {
            char c = source.charAt(i);

            // HEADERS (# Header)
            if (c == '#' && (i == 0 || source.charAt(i - 1) == '\n')) {
                int start = i++;
                while (i < len && source.charAt(i) == '#') i++;
                tokens.add(new Token(TokenType.KEYWORD, source.subSequence(start, i), start, i));
                continue;
            }

            // CODE BLOCKS (```code``` or `code`)
            if (c == '`') {
                int start = i++;
                boolean isBlock = (i + 1 < len && source.charAt(i) == '`' && source.charAt(i + 1) == '`');
                if (isBlock) i += 2;

                while (i < len) {
                    if (isBlock) {
                        if (source.charAt(i) == '`' && i + 2 < len && source.charAt(i + 1) == '`' && source.charAt(i + 2) == '`') {
                            i += 3;
                            break;
                        }
                    } else {
                        if (source.charAt(i) == '`') {
                            i++;
                            break;
                        }
                    }
                    i++;
                }
                tokens.add(new Token(TokenType.STRING, source.subSequence(start, i), start, i));
                continue;
            }

            // LINKS & IMAGES ([text](url) or ![alt](url))
            if (c == '[' || (c == '!' && i + 1 < len && source.charAt(i + 1) == '[')) {
                int start = i;
                while (i < len && source.charAt(i) != ')') i++;
                if (i < len) i++;
                tokens.add(new Token(TokenType.ANNOTATION, source.subSequence(start, i), start, i));
                continue;
            }

            // BOLD & ITALIC (*text* or **text**)
            if (c == '*' || c == '_') {
                int start = i++;
                while (i < len && source.charAt(i) != c && source.charAt(i) != '\n') i++;
                if (i < len && source.charAt(i) == c) i++;
                tokens.add(new Token(TokenType.PROPERTY, source.subSequence(start, i), start, i));
                continue;
            }

            // REGULAR TEXT LINE
            int textStart = i++;
            while (i < len && "\n#`*_[!".indexOf(source.charAt(i)) < 0) i++;
            tokens.add(new Token(TokenType.IDENTIFIER, source.subSequence(textStart, i), textStart, i));
        }

        return tokens;
    }
}
