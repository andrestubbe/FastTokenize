package fasttokenize;

import fastansi.FastANSI;
import java.util.List;

public class Demo {

    // Tokyo Night Palette Colors (TrueColor 24-Bit RGB)
    private static final int COLOR_KEYWORD  = 0x9D7CD8; // Purple/Violet
    private static final int COLOR_TYPE     = 0x2AC3DE; // Cyan
    private static final int COLOR_METHOD   = 0x7AA2F7; // Blue
    private static final int COLOR_STRING   = 0x9ECE6A; // Green
    private static final int COLOR_NUMBER   = 0xFF9E64; // Orange
    private static final int COLOR_COMMENT  = 0x565F89; // Muted Blue Gray
    private static final int COLOR_OPERATOR = 0x89DDFF; // Light Cyan
    private static final int COLOR_PUNCT    = 0xBB9AF7; // Light Purple
    private static final int COLOR_ANNOT    = 0xE0AF68; // Yellow
    private static final int COLOR_DEFAULT  = 0xC0CAF5; // Soft Foreground Blue-White

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   FastTokenize 0.1.0 Multi-Language Demo");
        System.out.println("=================================================\n");

        String javaCode = """
            package com.example.service;

            import java.util.List;

            /**
             * FastTokenize Tokyo Night Highlighting Demo
             */
            public class UserProcessor {
                private static final int MAX_COUNT = 100;
                private final String name = "Antigravity";

                @Override
                public String toString() {
                    return "Processor[" + this.name + "] count: " + MAX_COUNT;
                }
            }
            """;

        System.out.println("--- 1. Tokenizing Java Code ---");
        List<Token> tokens = FastTokenize.tokenize(Language.JAVA, javaCode);
        for (Token t : tokens) {
            if (t.getType() != TokenType.WHITESPACE) {
                System.out.printf("[%12s] %s%n", t.getType(), t.getText().toString().replace("\n", "\\n"));
            }
        }

        System.out.println("\n--- 2. High-Speed Zero-Allocation Style Byte Stream ---");
        byte[] styles = FastTokenize.tokenizeStyles(Language.JAVA, javaCode);
        System.out.println("Generated " + styles.length + " style IDs matching character offsets.");

        System.out.println("\n--- 3. Tokyo Night ANSI Colored Terminal Output (FastANSI) ---\n");

        StringBuilder coloredOutput = new StringBuilder();
        for (Token t : tokens) {
            String text = t.getText().toString();
            int rgb = mapTokenToColor(t.getType());
            int r = (rgb >> 16) & 0xFF;
            int g = (rgb >> 8) & 0xFF;
            int b = rgb & 0xFF;

            if (t.getType() == TokenType.KEYWORD) {
                coloredOutput.append(FastANSI.fg(r, g, b)).append(FastANSI.BOLD).append(text).append(FastANSI.RESET);
            } else if (t.getType() == TokenType.THIS || t.getType() == TokenType.CONSTANT) {
                coloredOutput.append(FastANSI.fg(r, g, b)).append(FastANSI.ITALIC).append(text).append(FastANSI.RESET);
            } else {
                coloredOutput.append(FastANSI.fg(r, g, b)).append(text).append(FastANSI.RESET);
            }
        }

        System.out.println(coloredOutput.toString());
    }

    private static int mapTokenToColor(TokenType type) {
        return switch (type) {
            case KEYWORD, PREPROCESSOR -> COLOR_KEYWORD;
            case TYPE, TAG, ATTRIBUTE -> COLOR_TYPE;
            case METHOD -> COLOR_METHOD;
            case STRING -> COLOR_STRING;
            case NUMBER -> COLOR_NUMBER;
            case COMMENT -> COLOR_COMMENT;
            case OPERATOR -> COLOR_OPERATOR;
            case PUNCTUATION -> COLOR_PUNCT;
            case ANNOTATION -> COLOR_ANNOT;
            case THIS -> COLOR_KEYWORD;
            case CONSTANT -> COLOR_NUMBER;
            default -> COLOR_DEFAULT;
        };
    }
}
