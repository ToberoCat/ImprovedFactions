package io.github.toberocat.improvedfactions.spigot.placeholder.provided;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedfactions.spigot.player.SpigotFactionPlayer;
import io.github.toberocat.improvedfactions.spigot.player.SpigotOfflineFactionPlayer;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class RankPlaceholder implements FactionPlaceholder {
    @Override
    public @NotNull String run(@NotNull OfflinePlayer player, @NotNull Faction<?> faction) {
        Player on = player.getPlayer();
        if (on != null) {
            SpigotFactionPlayer factionPlayer = new SpigotFactionPlayer(on);
            return faction.getPlayerRank(factionPlayer).title(factionPlayer);
        }

        return faction.getPlayerRank(new SpigotOfflineFactionPlayer(player)).getRegistry();
    }
}
