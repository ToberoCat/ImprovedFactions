package io.github.toberocat.improvedfactions.listener;

import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import io.github.toberocat.improvedfactions.MainIF;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerJoinEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerJoinListener extends SpigotEventListener {

    public PlayerJoinListener(@NotNull MainIF plugin) {
        super(plugin);

        Bukkit.getOnlinePlayers().forEach(this::join);
    }

    @EventHandler
    private void event(PlayerJoinEvent event) {
        join(event.getPlayer());
    }

    private void join(@NotNull Player player) {
        FactionPlayer<?> factionPlayer = plugin.getPlayer(player.getUniqueId());
        if (factionPlayer == null) return;

        ImplementationHolder.playerJoin(factionPlayer);
    }
}
