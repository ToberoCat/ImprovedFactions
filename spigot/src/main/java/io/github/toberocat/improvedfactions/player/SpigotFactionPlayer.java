package io.github.toberocat.improvedfactions.player;

import io.github.toberocat.improvedFactions.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.faction.Faction;
import io.github.toberocat.improvedFactions.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.persistent.PersistentDataContainer;
import io.github.toberocat.improvedFactions.player.FactionPlayer;
import io.github.toberocat.improvedFactions.translator.Placeholder;
import io.github.toberocat.improvedFactions.translator.Translation;
import io.github.toberocat.improvedFactions.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.utils.ReturnConsumer;
import io.github.toberocat.improvedFactions.utils.StringUtils;
import io.github.toberocat.improvedfactions.MainIF;
import io.github.toberocat.improvedfactions.persistent.SpigotPersistentData;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SpigotFactionPlayer implements FactionPlayer<Player> {

    private final Player player;
    private final MainIF plugin;
    private final Translation translation;

    public SpigotFactionPlayer(Player player, MainIF plugin) {
        this.player = player;
        this.plugin = plugin;
        this.translation = new Translation(player.getUniqueId());
    }

    @Override
    public @Nullable String getMessage(@NotNull ReturnConsumer<Translatable, String> query, Placeholder... placeholders) {
        return StringUtils.replaceAll(translation.getMessage(query), placeholders);
    }

    @Override
    public @Nullable String[] getMessageBatch(@NotNull ReturnConsumer<Translatable, String[]> query,
                                              Placeholder... placeholders) {
        return StringUtils.replaceAll(translation.getMessages(query), placeholders);
    }

    @Override
    public @NotNull String getLocal() {
        return player.getLocale();
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
    public void sendTranslatable(@NotNull ReturnConsumer<Translatable, String> query, Placeholder... placeholders) {
        String msg = getMessage(query, placeholders);
        if (msg != null) player.sendMessage(msg);
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
        return new SpigotPersistentData(plugin, player.getPersistentDataContainer());
    }

    @Override
    public @NotNull Player getRaw() {
        return player;
    }
}
