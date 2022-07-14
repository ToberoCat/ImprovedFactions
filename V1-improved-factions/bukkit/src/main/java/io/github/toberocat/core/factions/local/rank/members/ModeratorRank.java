package io.github.toberocat.core.factions.local.rank.members;

import io.github.toberocat.core.factions.local.rank.Rank;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static io.github.toberocat.core.utility.config.ConfigManager.getValue;

public class ModeratorRank extends Rank {
    public static final String registry = "Moderator";

    public ModeratorRank(int priority) {
        super(getValue("faction.ranks.moderator", "moderator"), registry, priority, false);
    }

    @Override
    public String description(Player player) {
        return null;
    }

    @Override
    public ItemStack getItem(Player player) {
        return Utility.getSkull("https://textures.minecraft.net/texture/7c56bfd76e7b4bce7216d3489a4635e11c345de852faa0932159b4afde18b25c", 1,
                Language.getMessage("rank.moderator.title", player),
                Language.getLore("rank.moderator.lore", player));
    }
}
