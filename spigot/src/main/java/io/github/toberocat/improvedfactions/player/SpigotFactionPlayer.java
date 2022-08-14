package io.github.toberocat.improvedfactions.player;

import io.github.toberocat.improvedFactions.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.faction.Faction;
import io.github.toberocat.improvedFactions.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.persistent.PersistentDataContainer;
import io.github.toberocat.improvedFactions.player.FactionPlayer;
import io.github.toberocat.improvedFactions.translator.Placeholder;
import io.github.toberocat.improvedfactions.MainIF;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SpigotFactionPlayer implements FactionPlayer<Player> {

    private final Player player;
    private final MainIF plugin;

    public SpigotFactionPlayer(Player player, MainIF plugin) {
        this.player = player;
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getMessage(@NotNull String key, Placeholder... placeholders) {
        return null;
    }

    @Override
    public @NotNull String[] getMessageBatch(@NotNull String parentNode, Placeholder... placeholders) {
        return new String[0];
    }

    @Override
    public @NotNull Faction<?> getFaction() throws PlayerHasNoFactionException, FactionNotInStorage {
        String registry = getFactionRegistry();
        if (registry == null) throw new PlayerHasNoFactionException(this);
        return FactionHandler.getFaction(getFactionRegistry());
    }

    @Override
    public @Nullable String getFactionRegistry() {
        return player.getPersistentDataContainer()
                .get(plugin.getNamespacedKey(PersistentDataContainer.FACTION_KEY),
                        PersistentDataType.STRING);
    }

    @Override
    public boolean inFaction() {
        return getFactionRegistry() != null;
    }

    @Override
    public void sendMessage(@NotNull String message) {
        player.sendMessage(message);
    }

    @Override
    public void sendTranslatable(@NotNull String key, Placeholder... placeholders) {

    }

    @Override
    public @Nullable FactionPlayer<?> getPlayer() {
        return this;
    }

    @Override
    public @NotNull UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public @NotNull String getName() {
        return player.getName();
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
        org.bukkit.persistence.PersistentDataContainer container =
                player.getPersistentDataContainer();
        return new PersistentDataContainer() {
            @Override
            public void set(@NotNull String key, @NotNull String value) {
                container.set(plugin.getNamespacedKey(key), PersistentDataType.STRING, value);
            }

            @Override
            public boolean hasString(@NotNull String key) {
                return container.has(plugin.getNamespacedKey(key), PersistentDataType.STRING);
            }

            @Override
            public @Nullable String getString(@NotNull String key) {
                return container.get(plugin.getNamespacedKey(key), PersistentDataType.STRING);
            }

            @Override
            public void remove(@NotNull String key) {
                container.remove(plugin.getNamespacedKey(key));
            }

            @Override
            public boolean isEmpty() {
                return container.isEmpty();
            }
        };
    }

    @Override
    public @NotNull Player getRaw() {
        return player;
    }
}
