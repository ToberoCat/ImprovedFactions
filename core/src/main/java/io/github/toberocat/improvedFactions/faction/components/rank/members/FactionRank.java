package io.github.toberocat.improvedFactions.faction.components.rank.members;

import io.github.toberocat.improvedFactions.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.faction.components.rank.allies.AllyRank;
import io.github.toberocat.improvedFactions.handler.ConfigHandler;
import io.github.toberocat.improvedFactions.handler.ItemHandler;
import io.github.toberocat.improvedFactions.item.ItemStack;
import io.github.toberocat.improvedFactions.player.FactionPlayer;
import io.github.toberocat.improvedFactions.utils.CUtils;
import org.jetbrains.annotations.NotNull;

import java.net.URL;

public abstract class FactionRank extends Rank {

    private final String key;
    private final URL icon;

    public FactionRank(@NotNull String key,
                       @NotNull String registry,
                       int priority,
                       boolean admin,
                       @NotNull String base64Icon) {
        super(String.format("ranks.%s.faction.title", key), registry, priority, admin);
        this.key = key;
        this.icon = CUtils.createUrl("https://textures.minecraft.net/texture/" + base64Icon);
    }

    public FactionRank(String displayKey, String registryName, String base64Icon,
                       int permissionPriority, boolean isAdmin) {
        super(displayKey, registryName, permissionPriority, isAdmin);
        this.key = "";
        this.icon = CUtils.createUrl("https://textures.minecraft.net/texture/" + base64Icon);
    }

    @Override
    public @NotNull Rank getEquivalent() {
        return Rank.fromString(AllyRank.ALLY_IDENTIFIER + registry);
    }

    @Override
    public String[] description(FactionPlayer<?> player) {
        return player.getMessageBatch(String.format("ranks.%s.faction.description", key));
    }

    @Override
    public ItemStack getItem(FactionPlayer<?> player) {
        return ItemHandler.api().createSkull(icon,
                player.getMessage(displayKey),
                description(player));
    }

}
