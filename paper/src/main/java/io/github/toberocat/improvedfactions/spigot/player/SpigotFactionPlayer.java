package io.github.toberocat.improvedfactions.spigot.player;

import io.github.toberocat.improvedFactions.core.exceptions.TranslatableException;
import io.github.toberocat.improvedFactions.core.exceptions.TranslatableRuntimeException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.item.ItemStack;
import io.github.toberocat.improvedFactions.core.location.Location;
import io.github.toberocat.improvedFactions.core.persistent.PersistentHandler;
import io.github.toberocat.improvedFactions.core.persistent.component.PersistentWrapper;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Translatable;
import io.github.toberocat.improvedFactions.core.translator.Translation;
import io.github.toberocat.improvedfactions.spigot.item.SpigotItemStack;
import io.github.toberocat.improvedfactions.spigot.utils.ComponentUtility;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.title.Title;
import net.kyori.adventure.title.TitlePart;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;

public class SpigotFactionPlayer implements FactionPlayer {
    public static final @NotNull String PREFIX_QUERY = "prefix";

    private final Player player;
    private final Translation translation;
    private final PersistentWrapper data;

    public SpigotFactionPlayer(Player player) {
        this.player = player;
        translation = new Translation(player.locale());
        data = new PersistentWrapper(getUniqueId());
    }

    @Override
    public @Nullable String getMessage(@NotNull String query,
                                       @NotNull Map<String, Function<Translatable, String>> placeholders) {
        return translation.getMessage(query, placeholders);
    }

    @Override
    public @Nullable String[] getMessages(@NotNull String query,
                                          @NotNull Map<String, Function<Translatable, String>> placeholders) {
        return translation.getMessages(query, placeholders);
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
    public @NotNull ItemStack getMainItem() {
        return new SpigotItemStack(player.getInventory().getItemInMainHand());
    }

    @Override
    public void sendTitle(@NotNull String title, @NotNull String subtitle) {
        player.sendTitle(title, subtitle, 10, 40, 10);
    }

    @Override
    public void sendTitle(@NotNull String titleQuery,
                          @NotNull String subtitleQuery,
                          @NotNull Map<String, Function<Translatable, String>> placeholders) {
        Component title = ComponentUtility.create(getMessage(titleQuery, placeholders));
        Component subtitle = ComponentUtility.create(getMessage(subtitleQuery, placeholders));

        player.sendTitlePart(TitlePart.TIMES, Title.Times.times(
                Duration.ofMillis(500),
                Duration.ofSeconds(2),
                Duration.ofMillis(500)
        ));
        player.showTitle(Title.title(title, subtitle));
    }

    @Override
    public void sendActionBar(@NotNull String actionbarQuery,
                              @NotNull Map<String, Function<Translatable, String>> placeholders) {
        player.sendActionBar(ComponentUtility.create(getMessage(actionbarQuery, placeholders)));
    }

    @Override
    public void closeGuis() {
        player.closeInventory();
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
    public void sendMessage(@NotNull String query, @NotNull Map<String, Function<Translatable, String>> placeholders) {
        player.sendMessage(getPrefix()
                .append(Component.space())
                .append(ComponentUtility.create(getMessage(query, placeholders))));
    }

    @Override
    public @Nullable FactionPlayer getPlayer() {
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
    public long getPower() {
        return 0; // ToDo: Implement player power
    }

    @Override
    public long getMaxPower() {
        return 0; // ToDo: Implement player power
    }

    @Override
    public void runCommand(@NotNull String command) {
        player.performCommand(command);
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        return player.hasPermission(permission);
    }

    @Override
    public void sendException(@NotNull TranslatableException e) {
        sendMessage(e.getTranslationKey(), e.getPlaceholders());
    }

    @Override
    public void sendException(@NotNull TranslatableRuntimeException e) {
        sendMessage(e.getTranslationKey(), e.getPlaceholders());
    }

    private @NotNull Component getPrefix() {
        return ComponentUtility.create(translation.getMessage(PREFIX_QUERY, new HashMap<>()));
    }
}
