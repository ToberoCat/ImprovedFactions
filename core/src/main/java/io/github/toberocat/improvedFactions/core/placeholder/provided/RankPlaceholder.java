package io.github.toberocat.improvedFactions.core.placeholder.provided;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

public class RankPlaceholder implements FactionPlaceholder {
    @Override
    public @NotNull String run(@NotNull OfflineFactionPlayer<?> player,
                               @NotNull Faction<?> faction) {
        FactionPlayer<?> factionPlayer = player.getPlayer();
        if (factionPlayer != null)
            return faction.getPlayerRank(factionPlayer).title(factionPlayer);

        return faction.getPlayerRank(player).getRegistry();
    }
}
