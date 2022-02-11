package io.github.toberocat.core.utility.factions.rank.allies;

import io.github.toberocat.core.utility.factions.rank.Rank;
import org.bukkit.entity.Player;

public class AllyModeratorRank extends Rank {

    public static final String registry = "AllyModerator";

    public AllyModeratorRank() {
        super("Ally Moderator", registry, false);
    }

    @Override
    public String description(Player player) {
        return null;
    }
}
