package io.github.toberocat.improvedfactions;

import java.io.File;

public class Test {
    public static void delete(String... relativePath) {
        String path = String.join(File.separator, relativePath);
        System.out.println(path);
    }

    public static void main(String[] args) {
        delete("factions", "factionRegistry.json");
    }
}
