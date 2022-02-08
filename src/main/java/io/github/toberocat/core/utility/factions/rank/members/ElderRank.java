package io.github.toberocat.core.utility.factions.rank.members;

import io.github.toberocat.core.utility.factions.rank.Rank;
import org.bukkit.entity.Player;

public class ElderRank extends Rank {

    public static final String registry = "ElderRank";

    public ElderRank() {
        super("Elder", registry, false);
    }

    @Override
    public String description(Player player) {
        return null;
    }
}
