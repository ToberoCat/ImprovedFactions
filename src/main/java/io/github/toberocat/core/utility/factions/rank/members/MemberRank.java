package io.github.toberocat.core.utility.factions.rank.members;

import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.factions.rank.Rank;
import io.github.toberocat.core.utility.language.LangMessage;
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
        return Language.getMessage(LangMessage.RANK_MEMBER_DESCRIPTION, player);
    }

    @Override
    public ItemStack getItem() {
        return Utility.createItem(Material.GRASS_BLOCK, getDisplayName());
    }
}
