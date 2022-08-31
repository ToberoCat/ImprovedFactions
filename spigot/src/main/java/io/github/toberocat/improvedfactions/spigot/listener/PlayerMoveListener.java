package io.github.toberocat.improvedfactions.spigot.listener;

import io.github.toberocat.improvedFactions.core.command.component.AutoAreaCommand;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;

public class PlayerMoveListener extends SpigotEventListener {
    public PlayerMoveListener(@NotNull MainIF plugin) {
        super(plugin);
    }

    @EventHandler
    private void move(PlayerMoveEvent event) {
        if (event.getFrom().equals(event.getTo())) return;

        FactionPlayer<?> player = ImprovedFactions.api().getPlayer(event.getPlayer().getUniqueId());
        if (player == null) return;

        if (event.getFrom().getChunk().equals(event.getTo().getChunk())) return;
        AutoAreaCommand.move(player);
    }
}
