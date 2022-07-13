package io.github.toberocat.factions.core.rank.allies;

import io.github.toberocat.core.factions.rank.Rank;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static io.github.toberocat.core.utility.config.ConfigManager.getValue;

public class AllyElderRank extends Rank {

    public static final String registry = "AllyElder";

    public AllyElderRank(int priority) {
        super(getValue("faction.ranks.ally-elder", "Ally elder"), registry, priority, false);
    }

    @Override
    public String description(Player player) {
        return null;
    }

    @Override
    public ItemStack getItem(Player player) {
        return Utility.getSkull("https://textures.minecraft.net/texture/898e2f854c2d2de7595b21bc2f9362006ea72cb61181d2a99e0611cd74907693", 1,
                Language.getMessage("rank.ally-elder.title", player),
                Language.getLore("rank.ally-elder.lore", player));
    }
}
