package fasttokenize;

import fastansi.FastANSI;
import java.util.List;

public class Demo {

    // Warm Paper Light Background (RGB: #F5EFEB - Soft Creamy Warm Paper)
    private static final int PAPER_BG_R = 0xF5;
    private static final int PAPER_BG_G = 0xEF;
    private static final int PAPER_BG_B = 0xEB;

    // Gutter Background (Slightly darker warm sepia #E8DFD8)
    private static final int GUTTER_BG_R = 0xE8;
    private static final int GUTTER_BG_G = 0xDF;
    private static final int GUTTER_BG_B = 0xD8;

    // Paper / Warm Sepia Light Palette Foreground Colors (24-Bit RGB)
    private static final int COLOR_KEYWORD  = 0x8C3B14; // Deep Terracotta / Rust Brown
    private static final int COLOR_TYPE     = 0x2B5B84; // Deep Ink Blue
    private static final int COLOR_METHOD   = 0x6E4A25; // Warm Coffee Brown
    private static final int COLOR_STRING   = 0x3B6B35; // Olive Forest Green
    private static final int COLOR_NUMBER   = 0xA0522D; // Sienna Warm Brown
    private static final int COLOR_COMMENT  = 0x928275; // Muted Sepia Gray
    private static final int COLOR_OPERATOR = 0x5C4033; // Dark Umber Brown
    private static final int COLOR_PUNCT    = 0x705040; // Warm Chestnut
    private static final int COLOR_ANNOT    = 0xB85D18; // Warm Amber / Copper
    private static final int COLOR_DEFAULT  = 0x3D322C; // Dark Espresso Text

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   FastTokenize 0.1.0 Multi-Language Demo");
        System.out.println("=================================================\n");

        String javaCode = """
            package com.example.service;

            import java.util.List;

            /**
             * FastTokenize Warm Paper Light Theme Highlighting Demo
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

        System.out.println("\n--- 2. Warm Paper Light Theme ANSI Terminal View (FastANSI) ---\n");

        String gutterBgCode = FastANSI.bg(GUTTER_BG_R, GUTTER_BG_G, GUTTER_BG_B);
        String codeBgCode = FastANSI.bg(PAPER_BG_R, PAPER_BG_G, PAPER_BG_B);
        String resetCode = FastANSI.RESET;

        StringBuilder coloredOutput = new StringBuilder();
        String[] lines = javaCode.split("\n", -1);
        int lineNum = 1;

        for (String line : lines) {
            // 1. Gutter / Line Number with warm sepia background (#E8DFD8)
            coloredOutput.append(gutterBgCode)
                         .append(FastANSI.fg(0x92, 0x82, 0x75))
                         .append(String.format(" %2d | ", lineNum++))
                         .append(resetCode);

            // 2. Code Area with Warm Creamy Paper Background (#F5EFEB)
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
