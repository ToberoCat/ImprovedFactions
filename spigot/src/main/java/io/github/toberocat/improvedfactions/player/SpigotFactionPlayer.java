package io.github.toberocat.improvedfactions.player;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.persistent.PersistentHandler;
import io.github.toberocat.improvedFactions.core.persistent.component.PersistentWrapper;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.translator.Translation;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.core.utils.ReturnConsumer;
import io.github.toberocat.improvedFactions.core.utils.StringUtils;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.UUID;

public class SpigotFactionPlayer implements FactionPlayer<Player> {

    private final Player player;
    private final Translation translation;
    private final PersistentWrapper data;

    public SpigotFactionPlayer(Player player) {
        this.player = player;
        this.translation = new Translation(player.getUniqueId());
        this.data = new PersistentWrapper(getUniqueId());
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
        return data.get(PersistentHandler.FACTION_KEY);
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
    public @NotNull PersistentWrapper getDataContainer() {
        return data;
    }

    @Override
    public @NotNull Player getRaw() {
        return player;
    }
}
