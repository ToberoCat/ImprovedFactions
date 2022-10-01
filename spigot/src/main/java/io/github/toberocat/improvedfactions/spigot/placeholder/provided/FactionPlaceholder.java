package io.github.toberocat.improvedfactions.spigot.placeholder.provided;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedfactions.spigot.player.SpigotOfflineFactionPlayer;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.function.Function;

public interface FactionPlaceholder extends Function<OfflinePlayer, String> {
    @NotNull String run(@NotNull OfflinePlayer player, @NotNull Faction<?> faction);

    @Override
    default String apply(OfflinePlayer player) {
        OfflineFactionPlayer<?> fP = new SpigotOfflineFactionPlayer(player);
        try {
            return run(player, fP.getFaction());
        } catch (PlayerHasNoFactionException | FactionNotInStorage e) {
            return null;
        }
    }
}
