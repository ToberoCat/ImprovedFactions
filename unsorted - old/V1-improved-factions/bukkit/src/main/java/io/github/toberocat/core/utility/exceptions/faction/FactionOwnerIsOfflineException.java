package io.github.toberocat.core.utility.exceptions.faction;

import io.github.toberocat.core.factions.Faction;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

public class FactionOwnerIsOfflineException extends Exception {

    private final Faction<?> faction;

    public FactionOwnerIsOfflineException(@NotNull Faction<?> faction) {
        super("Owner of " + faction.getRegistry() + " is offline");
        this.faction = faction;
    }

    public Faction<?> getFaction() {
        return faction;
    }
}
