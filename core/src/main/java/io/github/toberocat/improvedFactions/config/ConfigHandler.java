package io.github.toberocat.improvedFactions.config;

import org.jetbrains.annotations.NotNull;

public class ConfigHandler {
    public static final ConfigHandler implementation = createImplementation();

    private static @NotNull ConfigHandler createImplementation() {
        return new ConfigHandler();
    }
}
