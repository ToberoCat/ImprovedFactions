package io.github.toberocat.factions.core;

import io.github.toberocat.factions.core.faction.Faction;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public abstract class FactionHandler {
    private static final HashMap<String, Faction> LOADED_FACTIONS = new HashMap<>();
    private static FactionHandler handlerInstance;

    public FactionHandler() {
        handlerInstance = this;
    }

    public static @NotNull Faction createFaction(@NotNull String display, @NotNull UUID player) {
        return handlerInstance.create(display, player);
    }

    protected abstract @NotNull Faction create(@NotNull String display, @NotNull UUID player);
}
