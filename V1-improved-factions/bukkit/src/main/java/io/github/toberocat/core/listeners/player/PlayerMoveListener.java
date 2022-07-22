package io.github.toberocat.core.listeners.player;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.player.PlayerSettingHandler;
import io.github.toberocat.core.player.TitlePosition;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parseable;
import io.github.toberocat.core.utility.settings.type.EnumSetting;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Chunk;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

public class PlayerMoveListener implements Listener {

    @EventHandler
    public void OnMove(PlayerMoveEvent event) {
        if (Utility.isDisabled(event.getPlayer().getWorld())) return;

        Chunk from = event.getFrom().getChunk();
        Chunk to = Objects.requireNonNull(event.getTo()).getChunk();
        Player player = event.getPlayer();

        if (from.getWorld().getName().equals(to.getWorld().getName())) return;
        if (from.getX() == to.getX() && from.getZ() == to.getZ()) return;

        String fromRegistry = ClaimManager.getChunkRegistry(from);
        String toRegistry = ClaimManager.getChunkRegistry(to);

        if (toRegistry == null) { // No claim on the next chunk
            if (fromRegistry != null) showWilderness(player); // Claim on the previous one
            return; // Skip anyways, because can't display null
        }

        if (toRegistry.equals(fromRegistry)) return; // Same faction / zone

        String display = getClaimDisplay(toRegistry, player);
        if (display == null) return;

        showDisplay(display, player);

    }

    private @Nullable String getClaimDisplay(@NotNull String registry, @NotNull Player player) {
        if (ClaimManager.isManageableZone(registry)) return Language.getMessage(
                ClaimManager.getZoneDisplay(registry), player);

        try {
            Faction<?> faction = FactionManager.getFactionByRegistry(registry);
            return applyRelationColor(faction.getDisplay(), faction, player);
        } catch (FactionNotInStorage e) {
            return null;
        }
    }

    private void showWilderness(@NotNull Player player) {
        String message = Language.getMessage("territory.wilderness", player);
        if (message == null) return;

        showDisplay(message, player);
    }

    private void showDisplay(@NotNull String display, @NotNull Player player) {
        TitlePosition position = displayPosition(player);

        switch (position) {
            case HIDDEN -> {

            }
            case SUBTITLE -> player.sendTitle(" ", display, 5, 20, 5);
            case TITLE -> player.sendTitle(display, "", 5, 20, 5);
            case ACTIONBAR -> player.spigot().sendMessage(ChatMessageType.ACTION_BAR,
                    TextComponent.fromLegacyText(display));
            case CHAT -> Language.sendMessage("territory.entered.chat", player,
                    new Parseable("{territory}", display));
        }
    }

    private TitlePosition displayPosition(@NotNull Player player) {
        EnumSetting setting = (EnumSetting) PlayerSettingHandler.getSettings(player.getUniqueId())
                .get("titlePosition");
        return TitlePosition.values()[setting.getSelected()];
    }

    private @NotNull String applyRelationColor(@NotNull String display,
                                               @NotNull Faction<?> claimFaction,
                                               @NotNull Player player) {
        FileConfiguration config = MainIF.config();
        if (claimFaction.isAllied(player))
            return config.getString("faction.relation-color.ally") + display;
        if (claimFaction.isEnemy(player))
            return config.getString("faction.relation-color.enemy") + display;
        return config.getString("faction.relation-color.neutral") + display;
    }
}
