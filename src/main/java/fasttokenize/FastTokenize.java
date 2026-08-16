package fasttokenize;

import fasttokenize.scanner.*;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

/**
 * Unified Facade & Factory for FastTokenize.
 * High-performance, zero-dependency tokenization engine for Java.
 */
public final class FastTokenize {

    private static final Map<Language, CodeScanner> SCANNERS = new EnumMap<>(Language.class);

    static {
        JavaScanner javaScanner = new JavaScanner();
        CppScanner cppScanner = new CppScanner();
        PythonScanner pythonScanner = new PythonScanner();
        CSharpScanner csharpScanner = new CSharpScanner();
        JsonScanner jsonScanner = new JsonScanner();
        XmlScanner xmlScanner = new XmlScanner();
        MarkdownScanner markdownScanner = new MarkdownScanner();
        CssScanner cssScanner = new CssScanner();

        SCANNERS.put(Language.JAVA, javaScanner);
        SCANNERS.put(Language.CPP, cppScanner);
        SCANNERS.put(Language.PYTHON, pythonScanner);
        SCANNERS.put(Language.CSHARP, csharpScanner);
        SCANNERS.put(Language.JSON, jsonScanner);
        SCANNERS.put(Language.JAVASCRIPT, cppScanner);
        SCANNERS.put(Language.CSS, cssScanner);
        SCANNERS.put(Language.XML, xmlScanner);
        SCANNERS.put(Language.MARKDOWN, markdownScanner);
        SCANNERS.put(Language.PLAIN_TEXT, javaScanner);
    }

    private FastTokenize() {}

    /**
     * Tokenizes a source code string for a specified language.
     */
    public static List<Token> tokenize(Language language, CharSequence source) {
        if (source == null) {
            return List.of();
        }
        CodeScanner scanner = SCANNERS.getOrDefault(language, SCANNERS.get(Language.JAVA));
        return scanner.scan(source);
    }

    /**
     * Tokenizes a source code string automatically detecting language from filename.
     */
    public static List<Token> tokenizeForFile(String filename, CharSequence source) {
        Language lang = Language.fromFilename(filename);
        return tokenize(lang, source);
    }

    /**
     * Scans source code into a zero-allocation byte array of TokenType IDs matching character positions.
     */
    public static byte[] tokenizeStyles(Language language, CharSequence source) {
        if (source == null) {
            return new byte[0];
        }
        CodeScanner scanner = SCANNERS.getOrDefault(language, SCANNERS.get(Language.JAVA));
        return scanner.scanStyles(source);
    }
}
