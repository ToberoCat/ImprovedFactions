package io.github.toberocat.versions;

import java.io.IOException;


public class Test {
    public static void main(String[] args) throws IOException {
        long l = Long.parseLong("ffffff", 16);
        System.out.println((int)l);
    }
}
