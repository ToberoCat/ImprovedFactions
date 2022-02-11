package io.github.toberocat.core.utility.factions.rank.allies;

import io.github.toberocat.core.utility.factions.rank.Rank;
import org.bukkit.entity.Player;

public class AllyElderRank extends Rank {

    public static final String registry = "AllyElder";

    public AllyElderRank() {
        super("Ally Elder", registry, false);
    }

    @Override
    public String description(Player player) {
        return null;
    }
}
