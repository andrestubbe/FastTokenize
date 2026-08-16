package fasttokenize;

import java.util.List;

public class Demo {
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   FastTokenize 0.1.0 Multi-Language Demo");
        System.out.println("=================================================\n");

        String javaCode = """
            package com.example;

            /**
             * HelloWorld example class
             */
            public class HelloWorld {
                @Override
                public String toString() {
                    return "Hello, FastTokenize! Count: " + 42;
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

        System.out.println("\n--- 3. Auto-Detect Language from Filename (.cpp) ---");
        String cppCode = "#include <iostream>\nint main() { std::cout << \"FastTokenize AVX2\"; return 0; }";
        List<Token> cppTokens = FastTokenize.tokenizeForFile("main.cpp", cppCode);
        cppTokens.stream()
            .filter(t -> t.getType() != TokenType.WHITESPACE)
            .forEach(t -> System.out.printf("[%12s] %s%n", t.getType(), t.getText()));
    }
}
