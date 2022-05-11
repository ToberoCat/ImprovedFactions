package io.github.toberocat.core.factions.rank.allies;

import io.github.toberocat.core.factions.rank.Rank;
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
