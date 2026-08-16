package fasttokenize;

import fastansi.FastANSI;
import java.util.List;

public class Demo {

    // Exact CreamCLI TokyoNightTheme 24-bit RGB Constants
    private static final int COLOR_KEYWORD  = 0xBB9AF7; // 0xBB9AF7 (Light Violet)
    private static final int COLOR_TYPE     = 0x2AC3DE; // 0x2AC3DE (Cyan)
    private static final int COLOR_METHOD   = 0x7AA2F7; // 0x7AA2F7 (Soft Blue)
    private static final int COLOR_FIELD    = 0x73DACA; // 0x73DACA (Teal)
    private static final int COLOR_STRING   = 0x9ECE6A; // 0x9ECE6A (Soft Green)
    private static final int COLOR_NUMBER   = 0xFF9E64; // 0xFF9E64 (Orange)
    private static final int COLOR_COMMENT  = 0x565F89; // 0x565F89 (Blue-Gray)
    private static final int COLOR_OPERATOR = 0x89DDFF; // 0x89DDFF (Light Cyan)
    private static final int COLOR_PUNCT    = 0x9ABDF5; // 0x9ABDF5 (Soft Purple Blue)
    private static final int COLOR_ANNOT    = 0xBB9AF7; // 0xBB9AF7
    private static final int COLOR_THIS     = 0xF7768E; // 0xF7768E (Pinkish Red)
    private static final int COLOR_CONSTANT = 0xBB9AF7; // 0xBB9AF7
    private static final int COLOR_DEFAULT  = 0xC0CAF5; // 0xC0CAF5 (Foreground Blue-White)

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   FastTokenize 0.1.0 Multi-Language Demo");
        System.out.println("=================================================\n");

        String javaCode = "package com.example.service;\n" +
                "\n" +
                "import java.util.List;\n" +
                "\n" +
                "/**\n" +
                " * FastTokenize Tokyo Night Highlighting Demo\n" +
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

        System.out.println("\n--- 2. CreamCLI Tokyo Night ANSI Syntax Highlighting (FastANSI) ---\n");

        String resetCode = FastANSI.RESET;
        StringBuilder coloredOutput = new StringBuilder();
        String[] lines = javaCode.split("\n", -1);
        int lineNum = 1;

        for (String line : lines) {
            // 1. Gutter / Line Number with muted line number color (0x3A4160)
            coloredOutput.append(FastANSI.fg(0x3A, 0x41, 0x60))
                         .append(String.format(" %2d | ", lineNum++))
                         .append(resetCode);

            // 2. Syntax-highlighted code line
            String trimmed = line.trim();
            boolean isCommentLine = trimmed.startsWith("/*") || trimmed.startsWith("/**") || trimmed.startsWith("*") || trimmed.startsWith("*/") || trimmed.startsWith("//");

            if (isCommentLine) {
                int r = (COLOR_COMMENT >> 16) & 0xFF;
                int g = (COLOR_COMMENT >> 8) & 0xFF;
                int b = COLOR_COMMENT & 0xFF;
                coloredOutput.append(FastANSI.fg(r, g, b)).append(FastANSI.ITALIC).append(line).append(resetCode);
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
                    coloredOutput.append(text).append(resetCode);
                }
            }

            coloredOutput.append("\n");
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
