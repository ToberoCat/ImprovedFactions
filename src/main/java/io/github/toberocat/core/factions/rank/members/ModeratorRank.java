package io.github.toberocat.core.factions.rank.members;

import io.github.toberocat.core.factions.rank.Rank;
import org.bukkit.entity.Player;

public class ModeratorRank extends Rank {
    public static final String registry = "Moderator";

    public ModeratorRank() {
        super("Moderator", registry, false);
    }

    @Override
    public String description(Player player) {
        return null;
    }
}
