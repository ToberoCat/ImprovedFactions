package io.github.toberocat.core.factions.rank.members;

import io.github.toberocat.core.factions.rank.Rank;
import org.bukkit.entity.Player;

public class ElderRank extends Rank {

    public static final String registry = "Elder";

    public ElderRank() {
        super("Elder", registry, false);
    }

    @Override
    public String description(Player player) {
        return null;
    }
}
