package io.github.toberocat.core.utility.factions.rank;

import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class OwnerRank extends Rank{
    public static final String registry = "Owner";
    public OwnerRank() {
        super("Owner", registry, true);
    }

    @Override
    public String description(Player player) {
        return Language.getMessage(LangMessage.RANK_OWNER_DESCRIPTION, player);
    }

    @Override
    public ItemStack getItem() {
        return Utility.createItem(Material.GRASS_BLOCK, getDisplayName());
    }
}
