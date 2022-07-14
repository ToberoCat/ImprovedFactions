package io.github.toberocat.core.factions.local.rank.allies;

import io.github.toberocat.core.factions.local.rank.Rank;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static io.github.toberocat.core.utility.config.ConfigManager.getValue;

public class AllyModeratorRank extends Rank {

    public static final String registry = "AllyModerator";

    public AllyModeratorRank(int priority) {
        super(getValue("faction.ranks.ally-moderator", "Ally moderator"), registry, priority, false);
    }

    @Override
    public String description(Player player) {
        return null;
    }

    @Override
    public ItemStack getItem(Player player) {
        return Utility.getSkull("https://textures.minecraft.net/texture/4a390a935c208a661b9594a069ed3024619875035d5bef97655a70e6efac56ef", 1,
                Language.getMessage("rank.ally-moderator.title", player),
                Language.getLore("rank.ally-moderator.lore", player));
    }
}
