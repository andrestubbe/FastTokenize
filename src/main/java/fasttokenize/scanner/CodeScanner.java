package fasttokenize.scanner;

import fasttokenize.Token;
import java.util.List;

/**
 * Common interface for language-specific token scanners.
 */
public interface CodeScanner {
    /**
     * Tokenizes the input source text into a list of Tokens.
     */
    List<Token> scan(CharSequence source);

    /**
     * Fills a zero-allocation byte array of style/type IDs matching character positions.
     */
    default byte[] scanStyles(CharSequence source) {
        List<Token> tokens = scan(source);
        byte[] styles = new byte[source.length()];
        for (Token t : tokens) {
            byte typeId = t.getType().toByteId();
            for (int i = t.getStart(); i < t.getEnd() && i < styles.length; i++) {
                styles[i] = typeId;
            }
        }
        return styles;
    }
}
