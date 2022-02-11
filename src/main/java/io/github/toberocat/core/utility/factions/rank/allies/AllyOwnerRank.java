package io.github.toberocat.core.utility.factions.rank.allies;

import io.github.toberocat.core.utility.factions.rank.Rank;
import org.bukkit.entity.Player;

public class AllyOwnerRank extends Rank {

    public static final String registry = "AllyOwner";

    public AllyOwnerRank() {
        super("Ally Owner", registry, false);
    }

    @Override
    public String description(Player player) {
        return null;
    }
}
