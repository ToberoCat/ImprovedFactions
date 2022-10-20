package io.github.toberocat.improvedFactions.core.utils.faction;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;
import java.util.stream.Stream;

public final class RankUtils {

    /**
     * Get all member uuids of a faction that can be modified by the player you specified.
     *
     * @param faction The faction you want to get the players rank priority from
     * @param player  The player you want to get all relative manageable ranks
     * @return A stream container all member uuids of the faction,
     * that can be modified by this player, based on their rank priority
     */
    public static @NotNull Stream<UUID> getManageablePlayers(@NotNull Faction<?> faction,
                                                             @NotNull FactionPlayer<?> player) {
        int priority = Rank.getPriority(faction.getPlayerRank(player));
        return faction.getMembers().filter(x -> Rank.getPriority(faction.getPlayerRank(x)) < priority);
    }
}
