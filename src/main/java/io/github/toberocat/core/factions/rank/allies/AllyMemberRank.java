package io.github.toberocat.core.factions.rank.allies;

import io.github.toberocat.core.factions.rank.Rank;
import org.bukkit.entity.Player;

public class AllyMemberRank extends Rank {

    public static final String registry = "AllyMember";

    public AllyMemberRank() {
        super("Ally Member", registry, false);
    }

    @Override
    public String description(Player player) {
        return null;
    }
}
