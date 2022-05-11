package io.github.toberocat.core.factions.rank.allies;

import io.github.toberocat.core.factions.rank.Rank;
import org.bukkit.entity.Player;

public class AllyAdminRank extends Rank {

    public static final String registry = "AllyAdmin";

    public AllyAdminRank() {
        super("Admin Ally", registry, false);
    }

    @Override
    public String description(Player player) {
        return null;
    }
}
