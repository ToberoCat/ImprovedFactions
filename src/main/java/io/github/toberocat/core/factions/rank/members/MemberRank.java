package io.github.toberocat.core.factions.rank.members;

import io.github.toberocat.core.factions.rank.Rank;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MemberRank extends Rank {
    public static final String registry = "Member";

    public MemberRank() {
        super("Member", registry, false);
    }

    @Override
    public String description(Player player) {
        return Language.getMessage("rank.member.description", player);
    }

    @Override
    public ItemStack getItem() {
        return Utility.createItem(Material.GRASS_BLOCK, getDisplayName());
    }
}
