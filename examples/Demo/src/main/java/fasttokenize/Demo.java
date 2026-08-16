package fasttokenize;

import fastansi.FastANSI;
import java.util.List;

public class Demo {

    // Extra Rich & Vivid Tokyo Night Blue Background (RGB: #1E2238 - Rich Slate Navy Blue)
    private static final int BG_R = 0x1E;
    private static final int BG_G = 0x22;
    private static final int BG_B = 0x38;

    // Gutter Background (Deeper Rich Blue #161828)
    private static final int GUTTER_BG_R = 0x16;
    private static final int GUTTER_BG_G = 0x18;
    private static final int GUTTER_BG_B = 0x28;

    // Exact CreamCLI TokyoNightTheme RGB Constants
    private static final int COLOR_KEYWORD  = 0xBB9AF7; // Light Violet
    private static final int COLOR_TYPE     = 0x2AC3DE; // Cyan
    private static final int COLOR_METHOD   = 0x7AA2F7; // Soft Blue
    private static final int COLOR_FIELD    = 0x73DACA; // Teal
    private static final int COLOR_STRING   = 0x9ECE6A; // Soft Green
    private static final int COLOR_NUMBER   = 0xFF9E64; // Orange
    private static final int COLOR_COMMENT  = 0x565F89; // Blue-Gray
    private static final int COLOR_OPERATOR = 0x89DDFF; // Light Cyan
    private static final int COLOR_PUNCT    = 0x9ABDF5; // Soft Purple Blue
    private static final int COLOR_ANNOT    = 0xE0AF68; // Yellow
    private static final int COLOR_THIS     = 0xF7768E; // Pinkish Red
    private static final int COLOR_CONSTANT = 0xBB9AF7; // Light Violet
    private static final int COLOR_DEFAULT  = 0xC0CAF5; // Foreground Blue-White

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   FastTokenize 0.1.0 Multi-Language Demo");
        System.out.println("=================================================\n");

        String javaCode = "package com.example.service;\n" +
                "\n" +
                "import java.util.List;\n" +
                "\n" +
                "/**\n" +
                " * FastTokenize CreamCLI Tokyo Night Highlighting Demo\n" +
                " */\n" +
                "public class UserProcessor {\n" +
                "    private static final int MAX_COUNT = 100;\n" +
                "    private final String name = \"Antigravity\";\n" +
                "\n" +
                "    @Override\n" +
                "    public String toString() {\n" +
                "        return \"Processor[\" + this.name + \"] count: \" + MAX_COUNT;\n" +
                "    }\n" +
                "}";

        System.out.println("--- 1. Auto-Detect Language & Tokenize Java File (UserProcessor.java) ---");
        List<Token> tokens = FastTokenize.tokenizeForFile("UserProcessor.java", javaCode);
        byte[] styles = FastTokenize.tokenizeStyles(Language.JAVA, javaCode);

        System.out.println("Extracted " + tokens.stream().filter(t -> t.getType() != TokenType.WHITESPACE).count() + " tokens & generated " + styles.length + " zero-allocation style byte IDs.\n");
        for (Token t : tokens) {
            if (t.getType() != TokenType.WHITESPACE) {
                System.out.printf("[%12s] %s%n", t.getType(), t.getText().toString().replace("\n", "\\n"));
            }
        }

        System.out.println("\n--- 2. CreamCLI Tokyo Night Full-Editor ANSI Terminal View (FastANSI) ---\n");

        String gutterBgCode = FastANSI.bg(GUTTER_BG_R, GUTTER_BG_G, GUTTER_BG_B);
        String codeBgCode = FastANSI.bg(BG_R, BG_G, BG_B);
        String resetCode = FastANSI.RESET;

        StringBuilder coloredOutput = new StringBuilder();
        String[] lines = javaCode.split("\n", -1);
        int lineNum = 1;

        for (String line : lines) {
            // 1. Gutter / Line Number with CreamCLI Tokyo Night gutter background
            coloredOutput.append(gutterBgCode)
                         .append(FastANSI.fg(0x59, 0x64, 0x91)) // Editor line numbers color
                         .append(String.format(" %2d | ", lineNum++));

            // 2. Code Area with CreamCLI Tokyo Night Editor background
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

                    // Apply foreground color AND re-enforce background color per token to prevent terminal reset overrides
                    coloredOutput.append(codeBgCode).append(FastANSI.fg(r, g, b));
                    if (t.getType() == TokenType.KEYWORD) {
                        coloredOutput.append(FastANSI.BOLD);
                    } else if (t.getType() == TokenType.THIS || t.getType() == TokenType.CONSTANT) {
                        coloredOutput.append(FastANSI.ITALIC);
                    }
                    coloredOutput.append(text);
                }
            }

            int pad = Math.max(0, 100 - line.length());
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
            case FIELD -> COLOR_FIELD;
            case STRING -> COLOR_STRING;
            case NUMBER -> COLOR_NUMBER;
            case COMMENT -> COLOR_COMMENT;
            case OPERATOR -> COLOR_OPERATOR;
            case PUNCTUATION -> COLOR_PUNCT;
            case ANNOTATION -> COLOR_ANNOT;
            case THIS -> COLOR_THIS;
            case CONSTANT -> COLOR_CONSTANT;
            default -> COLOR_DEFAULT;
        };
    }
}
