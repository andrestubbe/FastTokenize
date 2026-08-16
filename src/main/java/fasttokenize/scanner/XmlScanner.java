package fasttokenize.scanner;

import fasttokenize.Token;
import fasttokenize.TokenType;
import java.util.ArrayList;
import java.util.List;

/**
 * High-performance lexical scanner for XML, HTML5, SVG, and FXML.
 */
public class XmlScanner implements CodeScanner {

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

            // COMMENTS (<!-- ... -->)
            if (c == '<' && i + 3 < len && source.charAt(i + 1) == '!' && source.charAt(i + 2) == '-' && source.charAt(i + 3) == '-') {
                int start = i;
                i += 4;
                while (i + 2 < len) {
                    if (source.charAt(i) == '-' && source.charAt(i + 1) == '-' && source.charAt(i + 2) == '>') {
                        i += 3;
                        break;
                    }
                    i++;
                }
                tokens.add(new Token(TokenType.COMMENT, source.subSequence(start, i), start, i));
                continue;
            }

            // DIRECTIVES (<?xml ... ?>, <!DOCTYPE ... >)
            if (c == '<' && i + 1 < len && (source.charAt(i + 1) == '?' || source.charAt(i + 1) == '!')) {
                int start = i;
                while (i < len && source.charAt(i) != '>') i++;
                if (i < len) i++;
                tokens.add(new Token(TokenType.PREPROCESSOR, source.subSequence(start, i), start, i));
                continue;
            }

            // TAGS (<tag>, </tag>, <tag/>)
            if (c == '<') {
                int start = i++;
                if (i < len && source.charAt(i) == '/') i++;
                
                // Read tag name
                int tagStart = i;
                while (i < len && !Character.isWhitespace(source.charAt(i)) && "/>".indexOf(source.charAt(i)) < 0) {
                    i++;
                }
                tokens.add(new Token(TokenType.PUNCTUATION, source.subSequence(start, tagStart), start, tagStart));
                if (tagStart < i) {
                    tokens.add(new Token(TokenType.TAG, source.subSequence(tagStart, i), tagStart, i));
                }

                // Read attributes inside tag until '>'
                while (i < len && source.charAt(i) != '>') {
                    char tc = source.charAt(i);
                    if (Character.isWhitespace(tc)) {
                        int wsStart = i++;
                        while (i < len && Character.isWhitespace(source.charAt(i))) i++;
                        tokens.add(new Token(TokenType.WHITESPACE, source.subSequence(wsStart, i), wsStart, i));
                        continue;
                    }

                    if (tc == '/' && i + 1 < len && source.charAt(i + 1) == '>') {
                        int slashStart = i;
                        i += 2;
                        tokens.add(new Token(TokenType.PUNCTUATION, source.subSequence(slashStart, i), slashStart, i));
                        break;
                    }

                    // Attribute name
                    if (Character.isLetter(tc) || tc == '_' || tc == ':') {
                        int attrStart = i++;
                        while (i < len && (Character.isLetterOrDigit(source.charAt(i)) || "-_:.".indexOf(source.charAt(i)) >= 0)) {
                            i++;
                        }
                        tokens.add(new Token(TokenType.ATTRIBUTE, source.subSequence(attrStart, i), attrStart, i));
                        continue;
                    }

                    // Attribute value string ("..." or '...')
                    if (tc == '"' || tc == '\'') {
                        int strStart = i++;
                        char quote = tc;
                        while (i < len && source.charAt(i) != quote) i++;
                        if (i < len) i++;
                        tokens.add(new Token(TokenType.STRING, source.subSequence(strStart, i), strStart, i));
                        continue;
                    }

                    if (tc == '=') {
                        int eqStart = i++;
                        tokens.add(new Token(TokenType.OPERATOR, source.subSequence(eqStart, i), eqStart, i));
                        continue;
                    }

                    i++;
                }

                if (i < len && source.charAt(i) == '>') {
                    int endTag = i++;
                    tokens.add(new Token(TokenType.PUNCTUATION, source.subSequence(endTag, i), endTag, i));
                }
                continue;
            }

            // TEXT CONTENT
            int textStart = i;
            while (i < len && source.charAt(i) != '<') i++;
            tokens.add(new Token(TokenType.IDENTIFIER, source.subSequence(textStart, i), textStart, i));
        }

        return tokens;
    }
}
