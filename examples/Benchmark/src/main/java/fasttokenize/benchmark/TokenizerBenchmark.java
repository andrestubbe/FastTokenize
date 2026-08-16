package fasttokenize.benchmark;

import fasttokenize.FastTokenize;
import fasttokenize.Language;
import fasttokenize.Token;
import org.openjdk.jmh.annotations.*;

import java.util.List;
import java.util.concurrent.TimeUnit;

@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.SECONDS)
@State(Scope.Thread)
@Warmup(iterations = 3, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 5, time = 1, timeUnit = TimeUnit.SECONDS)
@Fork(1)
public class TokenizerBenchmark {

    private String sampleJavaCode;
    private String sampleCppCode;

    @Setup
    public void setup() {
        sampleJavaCode = """
            package com.example.service;

            import java.util.List;
            import java.util.ArrayList;

            /**
             * Service component handling user authentication.
             */
            public class UserService {

                private final List<String> users = new ArrayList<>();

                @Override
                public String toString() {
                    return "UserService[users=" + users.size() + "]";
                }

                public boolean authenticate(String username, String password) {
                    if (username == null || password == null) {
                        return false;
                    }
                    return username.equalsIgnoreCase("admin") && password.length() >= 8;
                }
            }
            """;

        sampleCppCode = """
            #include <iostream>
            #include <vector>
            #include <string>

            namespace app {
                class Renderer {
                public:
                    void render(const std::vector<std::string>& lines) {
                        for (const auto& line : lines) {
                            std::cout << line << "\\n";
                        }
                    }
                };
            }

            int main() {
                app::Renderer r;
                r.render({"Hello", "FastTokenize", "AVX2"});
                return 0;
            }
            """;
    }

    @Benchmark
    public List<Token> benchmarkJavaTokenization() {
        return FastTokenize.tokenize(Language.JAVA, sampleJavaCode);
    }

    @Benchmark
    public byte[] benchmarkJavaStyleByteStream() {
        return FastTokenize.tokenizeStyles(Language.JAVA, sampleJavaCode);
    }

    @Benchmark
    public List<Token> benchmarkCppTokenization() {
        return FastTokenize.tokenize(Language.CPP, sampleCppCode);
    }
}
