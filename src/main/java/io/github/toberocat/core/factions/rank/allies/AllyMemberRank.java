package io.github.toberocat.core.factions.rank.allies;

import io.github.toberocat.core.factions.rank.Rank;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import static io.github.toberocat.core.utility.config.ConfigManager.getValue;

public class AllyMemberRank extends Rank {

    public static final String registry = "AllyMember";

    public AllyMemberRank(int priority) {
        super(getValue("faction.ranks.ally-member", "Ally member"), registry, priority, false);
    }

    @Override
    public String description(Player player) {
        return null;
    }

    @Override
    public ItemStack getItem(Player player) {
        return Utility.getSkull("https://textures.minecraft.net/texture/95492eb79fe0c306761fb7cfc3dfb054c0b7740adf8650ac5182062a05a783f1", 1,
                Language.getMessage("rank.ally-member.title", player),
                Language.getLore("rank.ally-member.lore", player));
    }
}
