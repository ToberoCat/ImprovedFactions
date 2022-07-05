package io.github.toberocat.improvedfactions.listener;

import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.improvedfactions.HomeExtension;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

public class PlayerMoveListener implements Listener {
    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!HomeExtension.RESET_ON_MOVE) return;


        Player player = event.getPlayer();
        UUID uuid = player.getUniqueId();
        if (!HomeExtension.TELEPORTING_PLAYERS.containsKey(uuid)) return;

        Location from = event.getFrom();
        Location to = event.getTo();

        if (to == null) {
            return;
        }

        if (from.getWorld() != to.getWorld()) {
            reset(player, uuid);
            return;
        }


        if (from.distanceSquared(to) > 0.001f) reset(player, uuid);

    }

    private void reset(Player player, UUID uuid) {
        Language.sendMessage("command.faction.home.teleport-cancelled", player);
        HomeExtension.TELEPORTING_PLAYERS.get(uuid).cancel();
    }
}
