package io.github.toberocat.improvedfactions;

import io.github.toberocat.improvedFactions.translator.XmlLoader;
import io.github.toberocat.improvedFactions.translator.layout.Translatable;

import java.io.IOException;
import java.util.Arrays;

public class Test {
    public static void main(String[] args) throws IOException {
        Translatable translatable = XmlLoader.read(Translatable.class, Test.class.getResource("/example.xml"));
    }
}
