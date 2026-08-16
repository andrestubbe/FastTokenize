package fasttokenize;

import fastansi.FastANSI;
import java.util.List;

public class Demo {

    // Tokyo Purple Light Theme Background (24-Bit RGB: #F4F0F9 - Soft Pastel Lavender / Lilac Light)
    private static final int PAPER_BG_R = 0xF4;
    private static final int PAPER_BG_G = 0xF0;
    private static final int PAPER_BG_B = 0xF9;

    // Gutter Background (Slightly deeper pastel purple #E9E1F3)
    private static final int GUTTER_BG_R = 0xE9;
    private static final int GUTTER_BG_G = 0xE1;
    private static final int GUTTER_BG_B = 0xF3;

    // Tokyo Purple Light Palette Foreground Colors (24-Bit RGB)
    private static final int COLOR_KEYWORD  = 0x7E22CE; // Vibrant Purple / Violet
    private static final int COLOR_TYPE     = 0x0284C7; // Deep Sky Blue
    private static final int COLOR_METHOD   = 0x6B21A8; // Deep Royal Purple
    private static final int COLOR_STRING   = 0x15803D; // Rich Emerald Green
    private static final int COLOR_NUMBER   = 0xC2410C; // Warm Burnt Orange
    private static final int COLOR_COMMENT  = 0x8B7A9F; // Soft Lavender Gray
    private static final int COLOR_OPERATOR = 0x581C87; // Dark Purple Umber
    private static final int COLOR_PUNCT    = 0x9333EA; // Bright Purple
    private static final int COLOR_ANNOT    = 0xB45309; // Warm Amber Gold
    private static final int COLOR_DEFAULT  = 0x332940; // Deep Dark Violet Text

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   FastTokenize 0.1.0 Multi-Language Demo");
        System.out.println("=================================================\n");

        String javaCode = """
            package com.example.service;

            import java.util.List;

            /**
             * FastTokenize Tokyo Purple Light Theme Highlighting Demo
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

        System.out.println("--- 1. Auto-Detect Language & Tokenize Java File (UserProcessor.java) ---");
        List<Token> tokens = FastTokenize.tokenizeForFile("UserProcessor.java", javaCode);
        byte[] styles = FastTokenize.tokenizeStyles(Language.JAVA, javaCode);

        System.out.println("Extracted " + tokens.stream().filter(t -> t.getType() != TokenType.WHITESPACE).count() + " tokens & generated " + styles.length + " zero-allocation style byte IDs.\n");
        for (Token t : tokens) {
            if (t.getType() != TokenType.WHITESPACE) {
                System.out.printf("[%12s] %s%n", t.getType(), t.getText().toString().replace("\n", "\\n"));
            }
        }

        System.out.println("\n--- 2. Tokyo Purple Light Theme ANSI Terminal View (FastANSI) ---\n");

        String gutterBgCode = FastANSI.bg(GUTTER_BG_R, GUTTER_BG_G, GUTTER_BG_B);
        String codeBgCode = FastANSI.bg(PAPER_BG_R, PAPER_BG_G, PAPER_BG_B);
        String resetCode = FastANSI.RESET;

        StringBuilder coloredOutput = new StringBuilder();
        String[] lines = javaCode.split("\n", -1);
        int lineNum = 1;

        for (String line : lines) {
            // 1. Gutter / Line Number with deeper pastel purple background (#E9E1F3)
            coloredOutput.append(gutterBgCode)
                         .append(FastANSI.fg(0x8B, 0x7A, 0x9F))
                         .append(String.format(" %2d | ", lineNum++))
                         .append(resetCode);

            // 2. Code Area with Tokyo Purple Light Pastel Background (#F4F0F9)
            coloredOutput.append(codeBgCode);

            String trimmed = line.trim();
            boolean isCommentLine = trimmed.startsWith("/*") || trimmed.startsWith("/**") || trimmed.startsWith("*") || trimmed.startsWith("*/") || trimmed.startsWith("//");

            if (isCommentLine) {
                int r = (COLOR_COMMENT >> 16) & 0xFF;
                int g = (COLOR_COMMENT >> 8) & 0xFF;
                int b = COLOR_COMMENT & 0xFF;
                coloredOutput.append(FastANSI.fg(r, g, b)).append(FastANSI.ITALIC).append(line);
            } else {
                List<Token> currentLineTokens = FastTokenize.tokenize(Language.JAVA, line);
                for (Token t : currentLineTokens) {
                    String text = t.getText().toString();
                    int rgb = mapTokenToColor(t.getType());
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;

                    coloredOutput.append(FastANSI.fg(r, g, b));
                    if (t.getType() == TokenType.KEYWORD) {
                        coloredOutput.append(FastANSI.BOLD);
                    } else if (t.getType() == TokenType.THIS || t.getType() == TokenType.CONSTANT) {
                        coloredOutput.append(FastANSI.ITALIC);
                    }
                    coloredOutput.append(text);
                }
            }

            int pad = Math.max(0, 120 - line.length());
            coloredOutput.append(" ".repeat(pad));
            coloredOutput.append(resetCode).append("\n");
        }

        System.out.print(coloredOutput.toString());
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
