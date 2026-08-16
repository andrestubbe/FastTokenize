// FastTokenizer Spectrum Test Sample: Java
package com.fastjava.example;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Multi-line Javadoc Comment
 * @author FastJava Team
 * @version 1.0.0
 */
@Deprecated
public class SampleJava<T extends Number> implements Runnable {
    private static final double CONSTANT_VAL = 3.14159e-10;
    private final String greeting = "Hello, FastTokenizer! \u0041\n";
    private int counter = 0;

    // Single line comment
    public SampleJava(int initial) {
        this.counter = initial;
    }

    @Override
    public void run() {
        boolean flag = true;
        char symbol = 'Z';
        long address = 0xDEADBEEFL;
        
        if (flag && counter > 0) {
            System.out.println(greeting + " Count: " + counter);
        } else {
            /* Block comment inside method */
            counter++;
        }
    }
}
