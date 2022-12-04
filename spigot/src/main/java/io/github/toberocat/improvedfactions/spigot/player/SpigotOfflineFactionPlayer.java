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
        FactionPlayer<?> on = getPlayer();
        if (on != null) on.sendTranslatable(query, placeholders);
        else MessageHandler.api.sendTranslatable(player.getUniqueId(), query, placeholders);
    }

    @Override
    public void sendClickableTranslatable(@NotNull Function<Translatable, String> query, @NotNull String command, Placeholder... placeholders) {
        FactionPlayer<?> on = getPlayer();
        if (on != null) on.sendClickableTranslatable(query, command, placeholders);
        else MessageHandler.api.sendClickableTranslatable(player.getUniqueId(), query, command, placeholders);
    }

    /**
     * Fancy message
     * An utility to make sending messages with
     * hovering and clickable text easier to use
     * and more available to end users
     * <p>
     * Example messages:
     * <ul>
     *     <li>
     *         {text:&6Sample colored text}
     *     </li>
     *     <li>
     *         {text:Hover over the message; hover:&cHello}
     *     </li>
     *     <li>
     *         {text:Click here to say hi in chat; command:Hi everyone!}
     *     </li>
     *     <li>
     *         {text:Click here to change your gamemode; command:/gamemode creative}
     *     </li>
     *     <li>
     *         {text:Click here to go to youtube; url:https://youtube.com/}
     *     </li>
     *     <li>
     *         {text:Multiple attributes; hover:Hovering; suggest_command:This is a command suggestion}
     *     </li>
     *     <li>
     *         {text:First hover; hover:First} {text:Second hover; hover:Second} {text:Broadcast; command:/broadcast Hello!}
     *     </li>
     * </ul>
     *
     * @param placeholders
     * @author iDarkyy
     */
    @Override
    public void sendFancyMessage(@NotNull Function<Translatable, String> query, Placeholder... placeholders) {
        FactionPlayer<?> on = getPlayer();
        if (on != null) on.sendFancyMessage(query, placeholders);
        else MessageHandler.api.sendFancyMessage(player.getUniqueId(), query, placeholders);
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
