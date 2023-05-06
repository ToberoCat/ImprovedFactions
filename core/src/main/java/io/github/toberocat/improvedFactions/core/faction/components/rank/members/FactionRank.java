package io.github.toberocat.improvedFactions.core.faction.components.rank.members;

import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.allies.AllyRank;
import io.github.toberocat.improvedFactions.core.handler.ItemHandler;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.CUtils;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.HashMap;
import java.util.Objects;

public abstract class FactionRank extends Rank {

    public static final String[] FACTION_RANKS = new String[] {
            "Owner",
            "Admin",
            "Moderator",
            "Elder",
            "Member"
    };
    private final String key;
    private final URL icon;

    public FactionRank(@NotNull String key,
                       @NotNull String registry,
                       int priority,
                       boolean admin,
                       @NotNull String base64Icon) {
        super("ranks.faction." + key + ".title", registry, priority, admin);
        this.key = key;
        icon = CUtils.createUrl("https://textures.minecraft.net/texture/" + base64Icon);
    }

    public FactionRank(@NotNull String title,
                       @NotNull String registryName,
                       @NotNull String base64Icon,
                       int permissionPriority,
                       boolean isAdmin) {
        super(title, registryName, permissionPriority, isAdmin);
        key = "";
        icon = CUtils.createUrl("https://textures.minecraft.net/texture/" + base64Icon);
    }

    @Override
    public @NotNull Rank getEquivalent() {
        return Rank.fromString(AllyRank.ALLY_IDENTIFIER + registry);
    }

    @Override
    public String[] description(FactionPlayer player) {
        return player.getMessages("ranks.faction." + key + ".description", new HashMap<>());
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
