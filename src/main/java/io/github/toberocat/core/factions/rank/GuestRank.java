package io.github.toberocat.core.factions.rank;

import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;

public class GuestRank extends Rank {
    public static final String register = "Guest";

    public GuestRank() {
        super("&e&lGuest", register, false);
    }

    @Override
    public String description(Player player) {
        return Language.getMessage("rank.guest.description", player);
    }
}
