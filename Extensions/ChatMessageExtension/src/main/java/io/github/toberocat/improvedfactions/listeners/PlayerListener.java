package io.github.toberocat.improvedfactions.listeners;


import io.github.toberocat.improvedfactions.ChatMessageExtension;
import io.github.toberocat.improvedfactions.types.ChatType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import static io.github.toberocat.improvedfactions.modules.ChatModule.send;
import static io.github.toberocat.improvedfactions.modules.ChatModule.sendAlly;

public class PlayerListener implements Listener {

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void chat(AsyncPlayerChatEvent event) {
        ChatType type = ChatMessageExtension.PLAYER_CHAT_TYPE.get(event.getPlayer().getUniqueId());
        if (type == ChatType.FACTION) {
            send(event.getPlayer(), event.getMessage());
            event.setCancelled(true);
        } else if (type == ChatType.ALLIES) {
            sendAlly(event.getPlayer(), event.getMessage());
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void PlayerJoin(PlayerJoinEvent event) {
        ChatMessageExtension.PLAYER_CHAT_TYPE.put(event.getPlayer().getUniqueId(), ChatType.PUBLIC);
    }

    @EventHandler
    public void PlayerLeave(PlayerQuitEvent event) {
        ChatMessageExtension.PLAYER_CHAT_TYPE.remove(event.getPlayer().getUniqueId(), ChatType.PUBLIC);
    }
}
