package io.github.toberocat.improvedfactions.spigot.listener.world;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.listener.BlockListener;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.world.World;
import io.github.toberocat.improvedfactions.spigot.MainIF;
import io.github.toberocat.improvedfactions.spigot.listener.SpigotEventListener;
import org.bukkit.Chunk;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.jetbrains.annotations.NotNull;

public class SpigotBlockListener extends SpigotEventListener {

    private final BlockListener blockListener;
    private final ImprovedFactions<?> factions;

    public SpigotBlockListener(@NotNull MainIF plugin) {
        super(plugin);
        this.blockListener = new BlockListener();
        this.factions = ImprovedFactions.api();
    }

    @EventHandler
    private void breakBlock(BlockBreakEvent event) {
        FactionPlayer<?> player = factions.getPlayer(event.getPlayer().getUniqueId());
        if (player == null) return;

        World<?> world = factions.getWorld(event.getBlock().getWorld().getName());
        if (world == null) return;

        Chunk chunk = event.getBlock().getChunk();
        try {
            blockListener.breakBlock(player, world, chunk.getX(), chunk.getZ());
        } catch (FactionNotInStorage e) {
            e.printStackTrace();
        }
    }

    @EventHandler
    private void placeBlock(BlockPlaceEvent event) {
        FactionPlayer<?> player = factions.getPlayer(event.getPlayer().getUniqueId());
        if (player == null) return;

        World<?> world = factions.getWorld(event.getBlock().getWorld().getName());
        if (world == null) return;

        Chunk chunk = event.getBlock().getChunk();
        try {
            blockListener.placeBlock(player, world, chunk.getX(), chunk.getZ());
        } catch (FactionNotInStorage e) {
            e.printStackTrace();
        }
    }
}
