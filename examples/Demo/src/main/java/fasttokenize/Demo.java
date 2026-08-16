package fasttokenize;

import fastansi.FastANSI;
import java.util.List;

public class Demo {

    // Catppuccin Macchiato / Mocha Cappuccino Dark Canvas (24-Bit RGB: #1E1E2E - Deep Espresso Mocha Dark)
    private static final int PAPER_BG_R = 0x1E;
    private static final int PAPER_BG_G = 0x1E;
    private static final int PAPER_BG_B = 0x2E;

    // Gutter Background (Slightly deeper Crust / Mantle #181825)
    private static final int GUTTER_BG_R = 0x18;
    private static final int GUTTER_BG_G = 0x18;
    private static final int GUTTER_BG_B = 0x25;

    // Catppuccin Mocha / Cappuccino Dark Palette Foreground Colors (24-Bit RGB)
    private static final int COLOR_KEYWORD  = 0xCBA6F7; // Mauve Light Lavender
    private static final int COLOR_TYPE     = 0x89DCEB; // Sky / Light Cyan
    private static final int COLOR_METHOD   = 0x89B4FA; // Soft Royal Blue
    private static final int COLOR_STRING   = 0xA6E3A1; // Warm Soft Green
    private static final int COLOR_NUMBER   = 0xF9E2AF; // Warm Creamy Yellow / Peach
    private static final int COLOR_COMMENT  = 0x6C7086; // Muted Gray-Blue
    private static final int COLOR_OPERATOR = 0x89DCEB; // Light Cyan
    private static final int COLOR_PUNCT    = 0x9399B2; // Soft Lavender Gray
    private static final int COLOR_ANNOT    = 0xF9E2AF; // Warm Peach Gold
    private static final int COLOR_DEFAULT  = 0xCDD6F4; // Soft Creamy Text

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   FastTokenize 0.1.0 Multi-Language Demo");
        System.out.println("=================================================\n");

        String javaCode = """
            package com.example.service;

            import java.util.List;

            /**
             * FastTokenize Catppuccin Cappuccino Dark Highlighting Demo
             */
            public class UserProcessor {
                private static final int MAX_COUNT = 100;
                private final String name = "Antigravity";

                @Override
                public String toString() {
                    return "Processor[" + this.name + "] count: " + MAX_COUNT;
                }
            }""";

        System.out.println("--- 1. Auto-Detect Language & Tokenize Java File (UserProcessor.java) ---");
        List<Token> tokens = FastTokenize.tokenizeForFile("UserProcessor.java", javaCode);
        byte[] styles = FastTokenize.tokenizeStyles(Language.JAVA, javaCode);

        System.out.println("Extracted " + tokens.stream().filter(t -> t.getType() != TokenType.WHITESPACE).count() + " tokens & generated " + styles.length + " zero-allocation style byte IDs.\n");
        for (Token t : tokens) {
            if (t.getType() != TokenType.WHITESPACE) {
                System.out.printf("[%12s] %s%n", t.getType(), t.getText().toString().replace("\n", "\\n"));
            }
        }

        System.out.println("\n--- 2. Catppuccin Cappuccino Dark ANSI Terminal View (FastANSI) ---\n");

        String gutterBgCode = FastANSI.bg(GUTTER_BG_R, GUTTER_BG_G, GUTTER_BG_B);
        String codeBgCode = FastANSI.bg(PAPER_BG_R, PAPER_BG_G, PAPER_BG_B);
        String resetCode = FastANSI.RESET;

        StringBuilder coloredOutput = new StringBuilder();
        String[] lines = javaCode.split("\n", -1);
        int lineNum = 1;

        for (String line : lines) {
            // 1. Gutter / Line Number with dark crust background (#181825)
            coloredOutput.append(gutterBgCode)
                         .append(FastANSI.fg(0x6C, 0x70, 0x86))
                         .append(String.format(" %2d | ", lineNum++))
                         .append(resetCode);

            // 2. Code Area with Catppuccin Mocha / Cappuccino Dark Background (#1E1E2E)
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
