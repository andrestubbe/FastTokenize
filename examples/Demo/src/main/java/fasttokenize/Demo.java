package fasttokenize;

import fastansi.FastANSI;
import java.util.List;

public class Demo {

    // Exact CreamCLI Theme Colors (Active default theme in CreamCLI is Rosé Pine Moon 0x232136)
    // Editor Background: 0x232136 (#232136 - Deep Rosé Pine Night Navy)
    // Gutter Background: 0x1B192B (#1B192B - Darker Sidebar Navy)

    private static final int BG_R = 0x23;
    private static final int BG_G = 0x21;
    private static final int BG_B = 0x36;

    private static final int GUTTER_BG_R = 0x1B;
    private static final int GUTTER_BG_G = 0x19;
    private static final int GUTTER_BG_B = 0x2B;

    // Exact CreamCLI RosePineMoonTheme RGB Constants
    private static final int COLOR_KEYWORD  = 0x3E8FB0; // Pine Teal / Blue Accent
    private static final int COLOR_TYPE     = 0xE49996; // Soft Rose Type
    private static final int COLOR_METHOD   = 0xEB6F92; // Rose Pink Method
    private static final int COLOR_FIELD    = 0xFDA5FF; // Soft Violet Field
    private static final int COLOR_STRING   = 0xF6C177; // Warm Gold String
    private static final int COLOR_NUMBER   = 0xEA9A97; // Coral Orange Number
    private static final int COLOR_COMMENT  = 0x6E6A86; // Muted Purple-Gray Comment
    private static final int COLOR_OPERATOR = 0x908CAA; // Slate Gray Operator
    private static final int COLOR_PUNCT    = 0x908CAA; // Slate Gray Punctuation
    private static final int COLOR_ANNOT    = 0x3E8FB0; // Pine Teal
    private static final int COLOR_THIS     = 0x3D8BAA; // Deep Teal This
    private static final int COLOR_CONSTANT = 0x3E8FB0; // Pine Teal Constant
    private static final int COLOR_DEFAULT  = 0xE0DEF4; // Main Text Soft Off-White

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   FastTokenize 0.1.0 Multi-Language Demo");
        System.out.println("=================================================\n");

        String javaCode = "package com.example.service;\n" +
                "\n" +
                "import java.util.List;\n" +
                "\n" +
                "/**\n" +
                " * FastTokenize CreamCLI Highlighting Demo (Rosé Pine Moon Theme)\n" +
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

        System.out.println("\n--- 2. CreamCLI Rosé Pine Moon ANSI Terminal View (FastANSI) ---\n");

        String gutterBgCode = FastANSI.bg(GUTTER_BG_R, GUTTER_BG_G, GUTTER_BG_B);
        String codeBgCode = FastANSI.bg(BG_R, BG_G, BG_B);
        String resetCode = FastANSI.RESET;

        StringBuilder coloredOutput = new StringBuilder();
        String[] lines = javaCode.split("\n", -1);
        int lineNum = 1;

        for (String line : lines) {
            // 1. Gutter / Line Number with CreamCLI Rosé Pine Moon gutter background
            coloredOutput.append(gutterBgCode)
                         .append(FastANSI.fg(0x6E, 0x6A, 0x86)) // Editor line numbers color
                         .append(String.format(" %2d | ", lineNum++));

            // 2. Code Area with CreamCLI Rosé Pine Moon Editor background (0x232136)
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
