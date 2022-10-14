package io.github.toberocat.improvedFactions.core.faction.components.rank.members;

import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.allies.AllyRank;
import io.github.toberocat.improvedFactions.core.handler.ItemHandler;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.core.utils.CUtils;
import org.jetbrains.annotations.NotNull;

import java.net.URL;
import java.util.Objects;
import java.util.function.Function;

public abstract class FactionRank extends Rank {

    private final String key;
    private final URL icon;

    public FactionRank(@NotNull String key,
                       @NotNull String registry,
                       int priority,
                       boolean admin,
                       @NotNull String base64Icon) {
        super(translatable -> translatable.getRanks().get(key).getFaction().getTitle(), registry, priority, admin);
        this.key = key;
        icon = CUtils.createUrl("https://textures.minecraft.net/texture/" + base64Icon);
    }

    public FactionRank(Function<Translatable, String> title, String registryName, String base64Icon,
                       int permissionPriority, boolean isAdmin) {
        super(title, registryName, permissionPriority, isAdmin);
        key = "";
        icon = CUtils.createUrl("https://textures.minecraft.net/texture/" + base64Icon);
    }

    @Override
    public @NotNull Rank getEquivalent() {
        return Rank.fromString(AllyRank.ALLY_IDENTIFIER + registry);
    }

    @Override
    public String[] description(FactionPlayer<?> player) {
        return player.getMessageBatch(translatable -> translatable.getRanks().get(key)
                .getFaction().getDescription().toArray(String[]::new));
    }

    @Override
    public @NotNull String title(FactionPlayer<?> player) {
        return Objects.requireNonNullElse(player.getMessage(title), "");
    }

    @Override
    public ItemStack getItem(FactionPlayer<?> player) {
        return ItemHandler.api().createSkull(icon,
                player.getMessage(title),
                description(player));
    }

}
