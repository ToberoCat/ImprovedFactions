package io.github.toberocat.improvedFactions.core.persistent;

import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PersistentData {
    @Nullable String set(@NotNull UUID id, @NotNull String key, @NotNull String value);

    @Nullable String remove(@NotNull UUID id, @NotNull String key);

    @Nullable String get(@NotNull UUID id, @NotNull String key);

    boolean has(@NotNull UUID id, @NotNull String key);

    default void dispose() {

    }

    default void quit(@NotNull FactionPlayer<?> player) {

    }
}
