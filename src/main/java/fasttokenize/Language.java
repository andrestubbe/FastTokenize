package fasttokenize;

/**
 * Supported programming languages and data formats for FastTokenize.
 * Comprehensive coverage matching CreamCLI FileCategory extensions.
 */
public enum Language {
    JAVA("java", "kt", "kts", "gradle", "groovy", "rs", "go", "sql"),
    CPP("c", "cpp", "h", "hpp", "cc", "cxx", "hh", "bat", "cmd", "sh", "ps1"),
    PYTHON("py", "pyw", "pyi"),
    CSHARP("cs", "csx"),
    JAVASCRIPT("js", "mjs", "cjs", "jsx", "ts", "mts", "cts", "tsx"),
    JSON("json", "yaml", "yml", "properties", "ini", "conf"),
    CSS("css", "scss", "less"),
    XML("xml", "html", "htm", "svg", "fxml"),
    MARKDOWN("md", "markdown"),
    PLAIN_TEXT("txt", "log", "gitignore", "env");

    private final String[] extensions;

    Language(String... extensions) {
        this.extensions = extensions;
    }

    public String[] getExtensions() {
        return extensions;
    }

    public static Language fromFilename(String filename) {
        if (filename == null || filename.isEmpty()) {
            return PLAIN_TEXT;
        }

        String lower = filename.toLowerCase();
        int dot = lower.lastIndexOf('.');
        if (dot < 0 || dot == lower.length() - 1) {
            return PLAIN_TEXT;
        }

        String ext = lower.substring(dot + 1);
        for (Language lang : values()) {
            for (String e : lang.extensions) {
                if (e.equals(ext)) {
                    return lang;
                }
            }
        }
        return PLAIN_TEXT;
    }
}
