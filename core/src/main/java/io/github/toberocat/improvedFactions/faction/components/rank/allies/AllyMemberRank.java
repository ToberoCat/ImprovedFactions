package io.github.toberocat.improvedFactions.faction.components.rank.allies;

import io.github.toberocat.core.factions.components.rank.Rank;
import io.github.toberocat.core.factions.components.rank.members.MemberRank;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static io.github.toberocat.MainIF.config;

public class AllyMemberRank extends Rank {

    public static final String registry = "AllyMember";

    public AllyMemberRank(int priority) {
        super(config().getString("faction.ranks.ally-member", "Ally member"), registry, priority, false);
    }

    @Override
    public @NotNull Rank getEquivalent() {
        return Rank.fromString(MemberRank.registry);
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
