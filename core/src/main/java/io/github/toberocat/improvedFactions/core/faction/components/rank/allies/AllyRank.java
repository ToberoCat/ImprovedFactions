package io.github.toberocat.improvedFactions.core.faction.components.rank.allies;

import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.handler.ItemHandler;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.CUtils;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public abstract class AllyRank extends Rank {

    public static final String ALLY_IDENTIFIER = "Ally";

    private final String factionRank;
    private final String key;
    private final URL icon;

    public AllyRank(@NotNull String key, @NotNull String registry, @NotNull String base64Icon) {
        super(translatable -> translatable.getRanks().get(key).getAlly().getTitle(),
                registry, -1, false);
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
        return player.getMessageBatch(translatable -> translatable.getRanks().get(key)
                .getAlly().getDescription().toArray(String[]::new));
    }

    @Override
    public ItemStack getItem(FactionPlayer<?> player) {
        return ItemHandler.api().createSkull(icon,
                player.getMessage(title),
                description(player));
    }
}
