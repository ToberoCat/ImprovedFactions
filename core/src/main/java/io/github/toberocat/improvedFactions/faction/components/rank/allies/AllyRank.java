package io.github.toberocat.improvedFactions.faction.components.rank.allies;

import io.github.toberocat.improvedFactions.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.handler.ItemHandler;
import io.github.toberocat.improvedFactions.item.ItemStack;
import io.github.toberocat.improvedFactions.player.FactionPlayer;
import io.github.toberocat.improvedFactions.utils.CUtils;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public abstract class AllyRank extends Rank {

    public static final String ALLY_IDENTIFIER = "Ally";

    private final String factionRank;
    private final String key;
    private final URL icon;

    public AllyRank(@NotNull String key, @NotNull String registry, @NotNull String base64Icon) {
        super(String.format("ranks.%s.ally.title", key), registry, -1, false);
        this.key = key;
        this.icon = CUtils.createUrl("https://textures.minecraft.net/texture/" + base64Icon);
        this.factionRank = registry.replace(ALLY_IDENTIFIER, "");
    }

    @Override
    public final @NotNull Rank getEquivalent() {
        return Rank.fromString(factionRank);
    }

    @Override
    public String[] description(FactionPlayer<?> player) {
        return player.getMessageBatch(String.format("ranks.%s.ally.description", key));
    }

    @Override
    public ItemStack getItem(FactionPlayer<?> player) {
        return ItemHandler.api().createSkull(icon,
                player.getMessage(displayKey),
                description(player));
    }
}
