package io.github.toberocat.improvedFactions.faction.components.rank;

import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;

import static io.github.toberocat.MainIF.config;

public class GuestRank extends Rank {
    public static final String register = "Guest";

    public GuestRank(int priority) {
        super(config().getString("faction.ranks.guest", "Guest"), register, priority, false);
    }

    @Override
    public @NotNull Rank getEquivalent() {
        return this;
    }

    @Override
    public String description(Player player) {
        return Language.getMessage("rank.guest.description", player);
    }

    @Override
    public ItemStack getItem(Player player) {
        return Utility.getSkull("https://textures.minecraft.net/texture/68e73763824d8c19cb7192e2a6f1c62f9bbd028c6d4eb96e554a47c6125038f0", 1,
                Language.getMessage("rank.guest.title", player),
                Language.getLore("rank.guest.lore", player));
    }
}
