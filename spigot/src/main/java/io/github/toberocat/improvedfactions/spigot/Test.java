package io.github.toberocat.improvedfactions.spigot;

import io.github.toberocat.improvedFactions.core.translator.XmlLoader;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;

public class Test {
    public static void main(String[] args) throws Exception {
        Translatable translatable = XmlLoader.read(Translatable.class,
                Test.class.getResource("/lang/en_us.xml"));
        System.out.println(translatable.getMessages().getCommand());
    }
}
