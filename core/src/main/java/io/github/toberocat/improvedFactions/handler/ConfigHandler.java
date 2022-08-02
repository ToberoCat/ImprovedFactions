package io.github.toberocat.improvedFactions.handler;

import io.github.toberocat.improvedFactions.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.registry.ImplementationHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface ConfigHandler {

    static @NotNull ConfigHandler api() {
        ConfigHandler implementation = ImplementationHolder.configHandler;
        if (implementation == null) throw new NoImplementationProvidedException("config handler implementation");
        return implementation;
    }

    @Nullable String getString(@NotNull String path);
    @Nullable String getString(@NotNull String path, @NotNull String def);

    int getInt(@NotNull String path);
    int getInt(@NotNull String path, int def);

    boolean getBool(@NotNull String path);
    boolean getBool(@NotNull String path, boolean def);
}
