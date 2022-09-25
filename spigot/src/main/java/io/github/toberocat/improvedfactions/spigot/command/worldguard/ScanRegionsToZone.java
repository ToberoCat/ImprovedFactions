package io.github.toberocat.improvedfactions.spigot.command.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.math.BlockVector3;
import com.sk89q.worldedit.regions.Polygonal2DRegion;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import io.github.toberocat.improvedFactions.core.claims.ClaimHandler;
import io.github.toberocat.improvedFactions.core.claims.zone.Zone;
import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.exceptions.chunk.ChunkAlreadyClaimedException;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.task.AsyncTask;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ScanRegionsToZone extends Command<ScanRegionsToZone.ScanPacket, ScanRegionsToZone.ScanConsolePacket> {
    @Override
    public boolean isAdmin() {
        return true;
    }

    @Override
    public @NotNull String label() {
        return "scanRegions";
    }

    @Override
    protected @NotNull CommandSettings createSettings() {
        return new CommandSettings(node)
                .setRequiredSpigotPermission(permission())
                .setAllowInConsole(true);
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return ClaimHandler.getZones().stream().toList();
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        return ClaimHandler.getZones().stream().toList();
    }

    @Override
    public void run(@NotNull ScanPacket packet) {
        AsyncTask<String> task = new AsyncTask<>(() -> {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            if (container == null) return null;

            Bukkit.getWorlds().forEach(bWorld -> {
                long ms = System.currentTimeMillis();
                World world = BukkitAdapter.adapt(bWorld);
                io.github.toberocat.improvedFactions.core.world.World<?> iWorld = ImprovedFactions
                        .api()
                        .getWorld(bWorld.getName());
                if (iWorld == null) return;

                RegionManager manager = container.get(world);
                if (manager == null) return;
                Collection<ProtectedRegion> regions = manager.getRegions().values();

                packet.player.sendMessage("Started scan for " + bWorld.getName());
                regions.forEach(x -> {
                    Polygonal2DRegion weRegion = new Polygonal2DRegion(world,
                            x.getPoints(), x.getMinimumPoint().getBlockY(), x.getMaximumPoint().getBlockY());
                    Set<Chunk> processed = new HashSet<>();
                    for (BlockVector3 vec : weRegion) {
                        Chunk chunk = bWorld.getBlockAt(vec.getBlockX(),
                                vec.getBlockY(), vec.getBlockZ()).getChunk();
                        if (processed.stream()
                                .anyMatch(c -> chunk.getX() == c.getX() && chunk.getZ() == c.getZ())) continue;
                        processed.add(chunk);

                        io.github.toberocat.improvedFactions.core.world.Chunk<?> iChunk =
                                iWorld.getChunkAt(chunk.getX(), chunk.getZ());

                        ClaimHandler.removeProtection(iChunk); // Make sure the chunk isn't claimed
                        try {
                            ClaimHandler.protectChunk(packet.zone.registry(), iChunk);
                        } catch (ChunkAlreadyClaimedException e) {
                            packet.player.sendMessage("Chunk §e" + chunk.getX() + ", " +
                                    chunk.getZ() + " §f failed claiming");
                        }
                    }
                });
                packet.player.sendMessage("Finished scan for " + bWorld.getName() +
                        ". Scan took §e" + (System.currentTimeMillis() - ms) + "§f ms");
            });
            return null;
        });
        task.then(s -> packet.player.sendMessage("Scan completed"));
        task.start();
    }

    @Override
    public void runConsole(@NotNull ScanConsolePacket packet) {
        AsyncTask<String> task = new AsyncTask<>(() -> {
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            if (container == null) return null;

            for (org.bukkit.World bWorld : Bukkit.getWorlds()) {
                long ms = System.currentTimeMillis();
                World world = BukkitAdapter.adapt(bWorld);
                io.github.toberocat.improvedFactions.core.world.World<?> iWorld = ImprovedFactions
                        .api()
                        .getWorld(bWorld.getName());
                if (iWorld == null) continue;

                RegionManager manager = container.get(world);
                if (manager == null) continue;
                Collection<ProtectedRegion> regions = manager.getRegions().values();

                Logger.api().logInfo("Started scan for " + bWorld.getName());
                regions.forEach(x -> {
                    Polygonal2DRegion weRegion = new Polygonal2DRegion(world,
                            x.getPoints(), x.getMinimumPoint().getBlockY(), x.getMaximumPoint().getBlockY());
                    Set<Chunk> processed = new HashSet<>();
                    for (BlockVector3 vec : weRegion) {
                        Chunk chunk = bWorld.getBlockAt(vec.getBlockX(),
                                vec.getBlockY(), vec.getBlockZ()).getChunk();
                        if (processed.stream()
                                .anyMatch(c -> chunk.getX() == c.getX() && chunk.getZ() == c.getZ())) continue;
                        processed.add(chunk);

                        io.github.toberocat.improvedFactions.core.world.Chunk<?> iChunk =
                                iWorld.getChunkAt(chunk.getX(), chunk.getZ());

                        ClaimHandler.removeProtection(iChunk); // Make sure the chunk isn't claimed
                        try {
                            ClaimHandler.protectChunk(packet.zone.registry(), iChunk);
                        } catch (ChunkAlreadyClaimedException e) {
                            Logger.api().logInfo("Chunk §e" + chunk.getX() + ", " +
                                    chunk.getZ() + " §f failed claiming");
                        }
                    }
                });
                Logger.api().logInfo("Finished scan for " + bWorld.getName() +
                        ". Scan took §e" + (System.currentTimeMillis() - ms) + "§f ms");
            }
            return null;
        });
        task.then(s -> Logger.api().logInfo("Scan completed"));
        task.start();
    }


    @Override
    public @Nullable ScanRegionsToZone.ScanPacket createFromArgs(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        if (args.length != 1) {
            executor.sendMessage("This command requires a zone");
            return null;
        }

        Zone zone = ClaimHandler.getZone(args[0]);
        if (zone == null) {
            executor.sendMessage("Zone wasn't to be found");
            return null;
        }
        return new ScanPacket(executor, zone);
    }

    @Override
    public @Nullable ScanRegionsToZone.ScanConsolePacket createFromArgs(@NotNull String[] args) {
        if (args.length != 1) {
            Logger.api().logInfo("This command requires a zone");
            return null;
        }

        Zone zone = ClaimHandler.getZone(args[0]);
        if (zone == null) {
            Logger.api().logInfo("Zone wasn't to be found");
            return null;
        }

        return new ScanConsolePacket(zone);
    }

    protected record ScanPacket(@NotNull FactionPlayer<?> player, @NotNull Zone zone) implements CommandPacket {

    }

    protected record ScanConsolePacket(@NotNull Zone zone) implements ConsoleCommandPacket {

    }
}
