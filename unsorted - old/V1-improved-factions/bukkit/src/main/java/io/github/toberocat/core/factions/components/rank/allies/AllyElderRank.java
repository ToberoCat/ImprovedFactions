package io.github.toberocat.core.factions.components.rank.allies;

import io.github.toberocat.core.factions.components.rank.Rank;
import io.github.toberocat.core.factions.components.rank.members.ElderRank;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static io.github.toberocat.MainIF.config;

public class AllyElderRank extends Rank {

    public static final String registry = "AllyElder";

    public AllyElderRank(int priority) {
        super(config().getString("faction.ranks.ally-elder", "Ally elder"), registry, priority, false);
    }

    @Override
    public @NotNull Rank getEquivalent() {
        return Rank.fromString(ElderRank.registry);
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
