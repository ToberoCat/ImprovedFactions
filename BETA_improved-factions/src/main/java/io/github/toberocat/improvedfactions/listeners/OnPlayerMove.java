package io.github.toberocat.improvedfactions.listeners;

import io.github.toberocat.improvedfactions.event.chunk.OnChunkEnterEvent;
import io.github.toberocat.improvedfactions.event.chunk.OnChunkLeaveEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class OnPlayerMove implements Listener {

    @EventHandler
    public void PlayerMove(PlayerMoveEvent event){
        Player player = event.getPlayer();

        if (!event.getFrom().getChunk().equals(event.getTo().getChunk())) {
            Bukkit.getServer().getPluginManager().callEvent(new OnChunkEnterEvent(player, event.getTo().getChunk()));
            Bukkit.getServer().getPluginManager().callEvent(new OnChunkLeaveEvent(player, event.getFrom().getChunk()));
        }
    }
}
