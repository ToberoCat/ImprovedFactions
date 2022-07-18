package io.github.toberocat.core.factions.components.rank.allies;

import io.github.toberocat.core.factions.components.rank.Rank;
import io.github.toberocat.core.factions.components.rank.members.AdminRank;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static io.github.toberocat.MainIF.config;

public class AllyAdminRank extends Rank {

    public static final String registry = "AllyAdmin";

    public AllyAdminRank(int priority) {
        super(config().getString("faction.ranks.ally-admin", "Ally admin"), registry, priority, false);
    }

    @Override
    public @NotNull Rank getEquivalent() {
        return Rank.fromString(AdminRank.registry);
    }

    @Override
    public String description(Player player) {
        return null;
    }

    @Override
    public ItemStack getItem(Player player) {
        return Utility.getSkull("https://textures.minecraft.net/texture/cb5b0c77a2d41d8d7144e8a56bb9c456ea9812d1ce665c7867fae849e8c7c931", 1,
                Language.getMessage("rank.ally-admin.title", player),
                Language.getLore("rank.ally-admin.lore", player));
    }
}
