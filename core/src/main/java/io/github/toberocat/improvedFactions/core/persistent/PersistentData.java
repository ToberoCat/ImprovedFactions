package io.github.toberocat.improvedFactions.core.persistent;

import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public interface PersistentData {
    @Nullable Object set(@NotNull UUID id, @NotNull String key, @NotNull Object value);

    @Nullable Object remove(@NotNull UUID id, @NotNull String key);

    @Nullable Object get(@NotNull UUID id, @NotNull String key);

    boolean has(@NotNull UUID id, @NotNull String key);

    default void dispose() {

    }

    default void quit(@NotNull FactionPlayer<?> player) {

    }
}
