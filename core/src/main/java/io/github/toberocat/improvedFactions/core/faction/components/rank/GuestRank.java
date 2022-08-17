package io.github.toberocat.improvedFactions.core.faction.components.rank;

import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;


public class GuestRank extends FactionRank {
    public static final String REGISTRY = "Guest";


    public GuestRank() {
        super(translatable -> translatable.getRanks().get("guest").getFaction().getTitle(), REGISTRY,
                "68e73763824d8c19cb7192e2a6f1c62f9bbd028c6d4eb96e554a47c6125038f0",
                -1, false);
    }

    @Override
    public @NotNull Rank getEquivalent() {
        return this;
    }

    @Override
    public String[] description(FactionPlayer<?> player) {
        return player.getMessageBatch(translatable -> translatable.getRanks().get("guest")
                .getFaction().getDescription().toArray(String[]::new));
    }
}
