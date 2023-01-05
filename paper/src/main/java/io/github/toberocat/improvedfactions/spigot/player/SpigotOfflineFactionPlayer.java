package io.github.toberocat.improvedfactions.spigot.player;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.persistent.PersistentHandler;
import io.github.toberocat.improvedFactions.core.persistent.component.PersistentWrapper;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedfactions.spigot.handler.message.MessageHandler;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;
import java.util.function.Function;

public class SpigotOfflineFactionPlayer implements OfflineFactionPlayer<OfflinePlayer> {

    private final OfflinePlayer player;
    private final PersistentWrapper data;


    public SpigotOfflineFactionPlayer(OfflinePlayer player) {
        this.player = player;
        data = new PersistentWrapper(getUniqueId());
    }

    @Override
    public @NotNull Faction<?> getFaction() throws PlayerHasNoFactionException, FactionNotInStorage {
        String registry = getFactionRegistry();
        if (registry == null) throw new PlayerHasNoFactionException(this);

        return FactionHandler.getFaction(registry);
    }

    @Override
    public @Nullable String getFactionRegistry() {
        return data.get(PersistentHandler.FACTION_KEY) instanceof String registry ? registry : null;
    }

    @Override
    public boolean inFaction() {
        return data.has(PersistentHandler.FACTION_KEY);
    }

    @Override
    public void sendMessage(@NotNull String message) {
        FactionPlayer<?> on = getPlayer();
        if (on != null) on.sendMessage(message);
        else MessageHandler.api.sendMessage(player.getUniqueId(), message);
    }

    @Override
    public void sendTranslatable(@NotNull Function<Translatable, String> query, Placeholder... placeholders) {
        sendMessage(query, placeholders);
    }

    @Override
    public void sendMessage(@NotNull Function<Translatable, String> query, Placeholder... placeholders) {
        FactionPlayer<?> on = getPlayer();
        if (on != null)
            on.sendMessage(query, placeholders);
        else
            MessageHandler.api.sendMessage(player.getUniqueId(), query, placeholders);
    }

    @Override
    public @Nullable FactionPlayer<?> getPlayer() {
        Player u = player.getPlayer();
        if (u == null) return null;

        return ImprovedFactions.api().getPlayer(player.getUniqueId());
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
    public @NotNull PersistentWrapper getDataContainer() {
        return data;
    }

    @Override
    public @NotNull OfflinePlayer getRaw() {
        return player;
    }
}
