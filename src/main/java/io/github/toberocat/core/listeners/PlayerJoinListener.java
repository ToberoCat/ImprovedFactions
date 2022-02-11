package io.github.toberocat.core.listeners;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.utility.async.AsyncCore;
import io.github.toberocat.core.utility.calender.TimeCore;
import io.github.toberocat.core.utility.factions.members.FactionMemberManager;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.settings.PlayerSettings;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class PlayerJoinListener implements Listener {

    public static Map<UUID, Long> PLAYER_JOINS = new HashMap<>();

    @EventHandler
    public void Join(PlayerJoinEvent event) {
        AsyncCore.Run(() -> {
            Player player = event.getPlayer();

            if (MainIF.getIF().isStandby() && Debugger.hasPermission(player, "factions.messages.standby")) {
                Language.sendMessage(LangMessage.PLUGIN_STANDBY_MESSAGE, player);
            }

            FactionMemberManager.PlayerJoin(event);
            PlayerSettings.PlayerJoined(player.getUniqueId());

            PLAYER_JOINS.put(player.getUniqueId(), System.currentTimeMillis());
        });
    }
}
