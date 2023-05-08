package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.handler.ConfigFile;
import org.jetbrains.annotations.NotNull;

public interface OptionFactory {
    void create(@NotNull ConfigFile file, @NotNull Options options);
}
