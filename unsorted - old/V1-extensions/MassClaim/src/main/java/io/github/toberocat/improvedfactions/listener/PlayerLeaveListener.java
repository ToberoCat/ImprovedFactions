package io.github.toberocat.improvedfactions.listener;

import io.github.toberocat.improvedfactions.wand.PositionWand;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerLeaveListener implements Listener {
    @EventHandler
    public void leave(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        PositionWand.removePlayer(player);
        PositionWand.USER_ITEMS.remove(player.getUniqueId());
    }
}
