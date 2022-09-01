package io.github.toberocat.improvedfactions.spigot.listener;

import io.github.toberocat.improvedFactions.core.command.component.AutoAreaCommand;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.listener.MoveListener;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.world.Chunk;
import io.github.toberocat.improvedFactions.core.world.World;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class PlayerMoveListener extends SpigotEventListener {

    private final MoveListener moveListener = new MoveListener();
    private final ImprovedFactions<?> api = ImprovedFactions.api();

    public PlayerMoveListener(@NotNull MainIF plugin) {
        super(plugin);
    }

    @EventHandler
    private void move(PlayerMoveEvent event) {
        if (event.getFrom().equals(event.getTo())) return;

        FactionPlayer<?> player = api.getPlayer(event.getPlayer().getUniqueId());
        if (player == null) return;

        if (event.getFrom().getChunk().equals(event.getTo().getChunk())) return;

        AutoAreaCommand.move(player);

        try {
            moveListener.move(player, chunkFromChunk(event.getFrom()), chunkFromChunk(event.getTo()));
        } catch (Exception ignored) {
        }
    }

    private @NotNull Chunk<?> chunkFromChunk(@Nullable Location location) throws Exception {
        if (location == null) throw new Exception();

        org.bukkit.World spigotWorld = location.getWorld();
        if (spigotWorld == null) throw new Exception();

        World<?> world = api.getWorld(spigotWorld.getName());
        if (world == null) throw new Exception();

        return world.getChunkAt(location.getChunk().getX(), location.getChunk().getZ());
    }
}
