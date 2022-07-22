package io.github.toberocat.core.listeners.player;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.factions.local.managers.FactionMemberManager;
import io.github.toberocat.core.player.PlayerSettingHandler;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.data.PersistentDataUtility;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.settings.type.BoolSetting;
import io.github.toberocat.core.utility.settings.type.Setting;
import io.github.toberocat.core.utility.tips.TipOfTheDay;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class PlayerJoinListener implements Listener {

    public static Map<UUID, Long> PLAYER_JOINS = new HashMap<>();

    @EventHandler
    public void Join(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        registerPlayer(player);

        AsyncTask.run(() -> {
            sendMessages(player);
            sendTod(player);
        });
    }

    private void registerFaction(@NotNull Player player) {
        String registry = FactionHandler.getPlayerFactionRegistry(player);
        if (registry == null) return;

        try {
            FactionHandler.loadFromStorage(registry);
        } catch (FactionNotInStorage e) {
            Language.sendMessage("player.faction-got-deleted", player);
            FactionHandler.removeFactionCache(player);
        }
    }

    private void registerPlayer(@NotNull Player player) {
        FactionMemberManager.PlayerJoin(player);
        PlayerSettingHandler.createSetting(player.getUniqueId());
        PLAYER_JOINS.put(player.getUniqueId(), System.currentTimeMillis());
    }

    private void sendMessages(@NotNull Player player) {
        if (MainIF.getIF().isStandby() && Debugger.hasPermission(player, "factions.messages.standby"))
            Language.sendMessage("message.plugin-standby", player);
    }

    private void sendTod(@NotNull Player player) {
        if (Utility.isDisabled(player.getWorld())) return;

        Setting<?> setting = PlayerSettingHandler.getSettings(player.getUniqueId()).get("showTips");
        if (setting instanceof BoolSetting bool && !bool.getSelected()) return;

        TipOfTheDay.sendTipOfTheDay(player);
    }
}
