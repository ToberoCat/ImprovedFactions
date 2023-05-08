package io.github.toberocat.improvedFactions.core.handler;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Optional;

public interface ConfigFile {

    @Nullable String getString(@NotNull String path);
    @NotNull String getString(@NotNull String path, @NotNull String def);

    @NotNull List<String> getList(@NotNull String path);
    @NotNull List<String> getSubSections(@NotNull String section);

    int getInt(@NotNull String path);
    int getInt(@NotNull String path, int def);

    long getLong(@NotNull String path);
    long getLong(@NotNull String path, long def);

    boolean getBool(@NotNull String path);
    boolean getBool(@NotNull String path, boolean def);

    boolean exists(@NotNull String path);
    @NotNull ConfigFile getSection(@NotNull String path);


    default <T extends Enum<T>> @NotNull Optional<T> getEnum(@NotNull String path, @NotNull Class<T> enumClass) {
        try {
            return Optional.of(Enum.valueOf(enumClass, getString(path, "")));
        } catch (IllegalArgumentException e) {
            return Optional.empty();
        }
    }
}
