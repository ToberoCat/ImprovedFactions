package io.github.toberocat.improvedfactions.player;

import io.github.toberocat.improvedFactions.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.faction.Faction;
import io.github.toberocat.improvedFactions.player.FactionPlayer;
import io.github.toberocat.improvedFactions.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.translator.Placeholder;
import io.github.toberocat.improvedFactions.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.utils.ReturnConsumer;
import io.github.toberocat.improvedfactions.MainIF;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SpigotOfflineFactionPlayer implements OfflineFactionPlayer<OfflinePlayer> {

    private final OfflinePlayer player;
    private final MainIF plugin;

    public SpigotOfflineFactionPlayer(OfflinePlayer player, MainIF plugin) {
        this.player = player;
        this.plugin = plugin;
    }

    @Override
    public @NotNull Faction<?> getFaction() throws PlayerHasNoFactionException, FactionNotInStorage {
        return null;
    }

    @Override
    public @Nullable String getFactionRegistry() {
        return null;
    }

    @Override
    public boolean inFaction() {
        return false;
    }

    @Override
    public void sendMessage(@NotNull String message) {

    }

    @Override
    public void sendTranslatable(@NotNull ReturnConsumer<Translatable, String> query, Placeholder... placeholders) {

    }

    @Override
    public @Nullable FactionPlayer<?> getPlayer() {
        Player u = player.getPlayer();
        if (u == null) return null;

        return plugin.getPlayer(player.getUniqueId());
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public @NotNull String getName() {
        String name = player.getName();
        return name == null ? "" : name;
    }

    @Override
    public long getLastPlayed() {
        return player.getLastPlayed();
    }

    @Override
    public boolean isOnline() {
        return player.isOnline();
    }

    @Override
    public @NotNull PersistentDataContainer getDataContainer() {
        return null;
    }

    @NotNull
    @Override
    public OfflinePlayer getRaw() {
        return player;
    }
}
