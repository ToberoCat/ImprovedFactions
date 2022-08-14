package io.github.toberocat.improvedFactions.persistent;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PersistentDataContainer {

    String CLAIM_KEY = "faction_claims";
    String FACTION_KEY = "faction_registry";

    void set(@NotNull String key, @NotNull String value);

    boolean hasString(@NotNull String key);

    @Nullable String getString(@NotNull String key);

    @Nullable String remove(@NotNull String key);

    boolean isEmpty();
}
