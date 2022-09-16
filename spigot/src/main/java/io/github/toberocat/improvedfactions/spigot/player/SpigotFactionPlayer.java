package io.github.toberocat.improvedfactions.spigot.player;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.location.Location;
import io.github.toberocat.improvedFactions.core.persistent.PersistentHandler;
import io.github.toberocat.improvedFactions.core.persistent.component.PersistentWrapper;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.translator.Translation;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.core.utils.StringUtils;
import io.github.toberocat.improvedFactions.core.utils.PermissionFileTool;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public class SpigotFactionPlayer implements FactionPlayer<Player> {

    private final Player player;
    private final Translation translation;
    private final PersistentWrapper data;
    private final Function<Translatable, String> PREFIX_QUERY = Translatable::getPrefix;

    public SpigotFactionPlayer(Player player) {
        this.player = player;
        this.translation = new Translation(player.getLocale());
        this.data = new PersistentWrapper(getUniqueId());
    }

    @Override
    public @Nullable String getMessage(@NotNull Function<Translatable, String> query, Placeholder... placeholders) {
        return StringUtils.replaceAll(translation.getMessage(query), placeholders);
    }

    @Override
    public @Nullable String[] getMessageBatch(@NotNull Function<Translatable, String[]> query,
                                              Placeholder... placeholders) {
        return StringUtils.replaceAll(translation.getMessages(query), placeholders);
    }

    @Override
    public @NotNull Location getLocation() {
        org.bukkit.Location l = player.getLocation();

        return new Location(l.getX(), l.getY(), l.getZ(),
                Objects.requireNonNull(l.getWorld()).getName());
    }

    @Override
    public @NotNull String getLocal() {
        return player.getLocale();
    }

    @Override
    public void sendTitle(@NotNull String title, @NotNull String subtitle) {
        player.sendTitle(title, subtitle, 0, 20, 0);
    }

    @Override
    public void sendTitle(@NotNull Function<Translatable, String> query) {
        String title = getMessage(query);
        player.sendTitle(title, "", 0, 20, 0);
    }

    @Override
    public @NotNull Faction<?> getFaction() throws PlayerHasNoFactionException, FactionNotInStorage {
        String registry = getFactionRegistry();
        if (registry == null) throw new PlayerHasNoFactionException(this);
        return FactionHandler.getFaction(getFactionRegistry());
    }

    @Override
    public @Nullable String getFactionRegistry() {
        return data.get(PersistentHandler.FACTION_KEY) instanceof String registry ? registry : null;
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
    public void sendTranslatable(@NotNull Function<Translatable, String> query, Placeholder... placeholders) {
        String msg = getMessage(query, placeholders);
        if (msg != null)
            player.sendMessage(translation.getMessage(PREFIX_QUERY) + msg);
    }

    @Override
    public void sendClickableTranslatable(@NotNull Function<Translatable, String> query, @NotNull String command, Placeholder... placeholders) {
        String msg = getMessage(query, placeholders);
        if (msg == null) return;

        TextComponent component = new TextComponent(TextComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes('&', msg)));
        component.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/" + command));
        player.spigot().sendMessage(component);
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

    @Override
    public void runCommand(@NotNull String command) {
        player.performCommand(command);
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return player.hasPermission(permission);
    }
}
