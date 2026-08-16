package fasttokenize;

/**
 * Immutable token representation containing type, text content, and character offsets.
 */
public final class Token {

    private final TokenType type;
    private final CharSequence text;
    private final int start;
    private final int end;

    public Token(TokenType type, CharSequence text, int start, int end) {
        this.type = type;
        this.text = text;
        this.start = start;
        this.end = end;
    }

    public TokenType getType() {
        return type;
    }

    public CharSequence getText() {
        return text;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public int getLength() {
        return end - start;
    }

    @Override
    public String toString() {
        return "Token{" +
                "type=" + type +
                ", text='" + text + '\'' +
                ", range=[" + start + ", " + end + "]}";
    }
}
