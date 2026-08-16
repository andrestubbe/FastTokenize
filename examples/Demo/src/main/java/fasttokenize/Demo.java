package fasttokenize;

import fastansi.FastANSI;
import java.util.List;

public class Demo {

    // Distinct Blue 8-Bit Index Backgrounds (Guaranteed compatibility across Windows cmd/PowerShell ANSI)
    // 17 = Deep Dark Navy Blue (Canvas #00005f)
    // 18 = Darker Midnight Gutter (#000087)
    // Or 4-bit standard ANSI: FastANSI.BG_BLUE (#0000AA)

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   FastTokenize 0.1.0 Multi-Language Demo");
        System.out.println("=================================================\n");

        String javaCode = "package com.example.service;\n" +
                "\n" +
                "import java.util.List;\n" +
                "\n" +
                "/**\n" +
                " * FastTokenize Deep Blue Highlighting Demo\n" +
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

        System.out.println("\n--- 2. Deep Blue ANSI Terminal View (FastANSI) ---\n");

        // Use 4-bit and 8-bit ANSI background codes for 100% reliable terminal rendering on Windows cmd
        String gutterBgCode = FastANSI.bg(18); // Dark Navy Blue Gutter
        String codeBgCode   = FastANSI.bg(17); // Deep Ocean Blue Editor Canvas
        String resetCode    = FastANSI.RESET;

        StringBuilder coloredOutput = new StringBuilder();
        String[] lines = javaCode.split("\n", -1);
        int lineNum = 1;

        for (String line : lines) {
            // 1. Gutter / Line Number with dark navy background
            coloredOutput.append(gutterBgCode)
                         .append(FastANSI.fg(14)) // Bright Cyan line numbers
                         .append(String.format(" %2d | ", lineNum++));

            // 2. Code Area with Deep Ocean Blue Background
            coloredOutput.append(codeBgCode);

            String trimmed = line.trim();
            boolean isCommentLine = trimmed.startsWith("/*") || trimmed.startsWith("/**") || trimmed.startsWith("*") || trimmed.startsWith("*/") || trimmed.startsWith("//");

            if (isCommentLine) {
                coloredOutput.append(FastANSI.fg(10)).append(FastANSI.ITALIC).append(line); // Bright Green Comments
            } else {
                List<Token> currentLineTokens = FastTokenize.tokenize(Language.JAVA, line);
                for (Token t : currentLineTokens) {
                    String text = t.getText().toString();
                    coloredOutput.append(mapTokenToAnsiFg(t.getType()));
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

    private static String mapTokenToAnsiFg(TokenType type) {
        return switch (type) {
            case KEYWORD, PREPROCESSOR -> FastANSI.fg(13); // Bright Magenta / Purple
            case TYPE, TAG, ATTRIBUTE -> FastANSI.fg(11);  // Bright Yellow / Cyan
            case METHOD -> FastANSI.fg(14);                // Bright Cyan
            case STRING -> FastANSI.fg(10);                // Bright Green
            case NUMBER -> FastANSI.fg(9);                 // Bright Red / Orange
            case COMMENT -> FastANSI.fg(8);                // Muted Gray
            case OPERATOR -> FastANSI.fg(14);               // Bright Cyan
            case PUNCTUATION -> FastANSI.fg(15);            // Bright White
            case ANNOTATION -> FastANSI.fg(11);             // Bright Yellow
            case THIS -> FastANSI.fg(13);                   // Bright Magenta
            case CONSTANT -> FastANSI.fg(9);                // Bright Red
            default -> FastANSI.fg(15);                     // Bright White Text
        };
    }
}
