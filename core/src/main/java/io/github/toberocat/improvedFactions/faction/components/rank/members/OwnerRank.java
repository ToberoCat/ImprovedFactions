package io.github.toberocat.improvedFactions.faction.components.rank.members;

import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class OwnerRank extends FactionRank {
    public static final String registry = "Owner";

    public OwnerRank(int priority) {
        super("faction.ranks.owner", registry, priority, true);
    }

    @Override
    public String description(Player player) {
        return Language.getMessage("rank.owner.description", player);
    }

    @Override
    public ItemStack getItem(Player player) {
        return Utility.getSkull("https://textures.minecraft.net/texture/e081e9b6034f715fd02f5b4d311a489f8f6aca72724d85bfd19649655d5cb257", 1,
                Language.getMessage("rank.owner.title", player),
                Language.getLore("rank.owner.lore", player));
    }
}
