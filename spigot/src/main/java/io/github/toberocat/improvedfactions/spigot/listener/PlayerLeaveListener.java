package io.github.toberocat.improvedfactions.spigot.listener;

import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerQuitEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerLeaveListener extends SpigotEventListener {

    public PlayerLeaveListener(@NotNull MainIF plugin) {
        super(plugin);
    }

    @EventHandler
    private void leave(PlayerQuitEvent event) {
        FactionPlayer<?> player = plugin.getPlayer(event.getPlayer().getUniqueId());
        if (player == null) return;

        ImplementationHolder.playerLeave(player);
    }
}
