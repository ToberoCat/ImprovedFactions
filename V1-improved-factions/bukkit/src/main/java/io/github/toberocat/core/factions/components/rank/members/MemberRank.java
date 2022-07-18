package io.github.toberocat.core.factions.components.rank.members;

import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MemberRank extends FactionRank {
    public static final String registry = "Member";

    public MemberRank(int priority) {
        super("faction.ranks.member", registry, priority, false);
    }

    @Override
    public String description(Player player) {
        return Language.getMessage("rank.member.description", player);
    }

    @Override
    public ItemStack getItem(Player player) {
        return Utility.getSkull("https://textures.minecraft.net/texture/fe8fc22eb8a994f669fb64ff8c5bb153874f471a159c34f8916c7adea998ff", 1,
                Language.getMessage("rank.member.title", player),
                Language.getLore("rank.member.lore", player));
    }
}
