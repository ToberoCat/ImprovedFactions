package io.github.toberocat.improvedFactions.core.handler;

import io.github.toberocat.improvedFactions.core.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface ConfigHandler {

    static @NotNull ConfigHandler api() {
        ConfigHandler implementation = ImplementationHolder.configHandler;
        if (implementation == null) throw new NoImplementationProvidedException("config handler");
        return implementation;
    }

    @Nullable String getString(@NotNull String path);
    @NotNull String getString(@NotNull String path, @NotNull String def);

    @NotNull List<String> getList(@NotNull String path);

    int getInt(@NotNull String path);
    int getInt(@NotNull String path, int def);

    long getLong(@NotNull String path);
    long getLong(@NotNull String path, long def);

    boolean getBool(@NotNull String path);
    boolean getBool(@NotNull String path, boolean def);
}
