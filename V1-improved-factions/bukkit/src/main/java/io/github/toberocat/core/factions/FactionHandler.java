package io.github.toberocat.core.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.database.DatabaseFactionHandler;
import io.github.toberocat.core.factions.local.LocalFaction;
import io.github.toberocat.core.factions.local.LocalFactionHandler;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public abstract class FactionHandler {
    private static FactionHandler handler;

    public FactionHandler() {
        handler = this;
    }

    protected abstract LocalFaction create(@NotNull String display, @NotNull Player owner);

    public static void register() {
        if (Boolean.TRUE.equals(MainIF.config().getBoolean("sql.useSql", false))) new DatabaseFactionHandler();
        new LocalFactionHandler();
    }

    public static LocalFaction createFaction(@NotNull String display,
                                             @NotNull Player owner) {
        return handler.create(display, owner);
    }
}
