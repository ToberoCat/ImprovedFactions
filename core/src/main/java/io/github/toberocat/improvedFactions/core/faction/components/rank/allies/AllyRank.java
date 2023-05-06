package io.github.toberocat.improvedFactions.core.faction.components.rank.allies;

import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.handler.ItemHandler;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.CUtils;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public abstract class AllyRank extends Rank {

    public static final String ALLY_IDENTIFIER = "Ally";

    private final String factionRank;
    private final String key;
    private final URL icon;

    public AllyRank(@NotNull String key, @NotNull String registry, @NotNull String base64Icon) {
        super("ranks.ally." + key + ".title",
                registry, -1, false);
        this.key = key;
        icon = CUtils.createUrl("https://textures.minecraft.net/texture/" + base64Icon);
        factionRank = registry.replace(ALLY_IDENTIFIER, "");
    }

    @Override
    public final @NotNull Rank getEquivalent() {
        return Rank.fromString(factionRank);
    }

    @Override
    public String[] description(FactionPlayer player) {
        return player.getMessages("ranks.ally." + key + ".description", new HashMap<>());
    }

    @Override
    public @NotNull String title(FactionPlayer player) {
        return Objects.requireNonNullElse(player.getMessage(title, new HashMap<>()), "");
    }

    @Override
    public ItemStack getItem(FactionPlayer player) {
        return ItemHandler.api().createSkull(icon,
                title(player),
                description(player));
    }
}
