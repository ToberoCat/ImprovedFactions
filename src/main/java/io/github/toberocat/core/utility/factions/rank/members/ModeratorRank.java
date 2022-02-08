package io.github.toberocat.core.utility.factions.rank.members;

import io.github.toberocat.core.utility.factions.rank.Rank;
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
