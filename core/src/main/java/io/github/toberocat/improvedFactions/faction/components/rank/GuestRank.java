package io.github.toberocat.improvedFactions.faction.components.rank;

import io.github.toberocat.improvedFactions.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.handler.ItemHandler;
import io.github.toberocat.improvedFactions.item.ItemStack;
import io.github.toberocat.improvedFactions.player.FactionPlayer;
import io.github.toberocat.improvedFactions.utils.CUtils;
import org.jetbrains.annotations.NotNull;

import java.net.URL;


public class GuestRank extends FactionRank {
    public static final String register = "Guest";


    public GuestRank() {
        super("ranks.guest.title", register,
                "68e73763824d8c19cb7192e2a6f1c62f9bbd028c6d4eb96e554a47c6125038f0",
                -1, false);
    }

    @Override
    public @NotNull Rank getEquivalent() {
        return this;
    }

    @Override
    public String[] description(FactionPlayer<?> player) {
        return player.getMessageBatch("ranks.guest.description");
    }
}
