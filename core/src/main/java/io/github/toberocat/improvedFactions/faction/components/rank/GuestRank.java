package io.github.toberocat.improvedFactions.faction.components.rank;

import io.github.toberocat.improvedFactions.handler.ItemHandler;
import io.github.toberocat.improvedFactions.item.ItemStack;
import io.github.toberocat.improvedFactions.player.FactionPlayer;
import io.github.toberocat.improvedFactions.utils.CUtils;
import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

import static io.github.toberocat.MainIF.config;

public class GuestRank extends Rank {
    public static final String register = "Guest";
    private static final URL ICON_URL = CUtils.createUrl("https://textures.minecraft.net/texture/" +
            "68e73763824d8c19cb7192e2a6f1c62f9bbd028c6d4eb96e554a47c6125038f0");

    public GuestRank(int priority) {
        super(config().getString("faction.ranks.guest", "Guest"), register, priority, false);
    }

    @Override
    public @NotNull Rank getEquivalent() {
        return this;
    }

    @Override
    public String description(FactionPlayer<?> player) {
        return player.getMessage("rank.guest.description");
    }

    @Override
    public ItemStack getItem(FactionPlayer<?> player) {
        return ItemHandler.api().createSkull(ICON_URL,
                player.getMessage("rank.guest.title"),
                player.getMessageBatch("rank.guest.lore"));
    }
}
