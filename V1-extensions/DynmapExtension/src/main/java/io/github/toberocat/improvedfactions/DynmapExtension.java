package io.github.toberocat.improvedfactions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.extensions.Extension;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.claim.Claim;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.improvedfactions.dynmap.DynmapHandle;
import io.github.toberocat.improvedfactions.listener.ChunkListener;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.World;
import org.bukkit.plugin.Plugin;
import org.dynmap.DynmapAPI;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.logging.Level;

public final class DynmapExtension extends Extension {

    public DynmapHandle handle;
    public double opacity;

    @Override
    protected void onEnable(MainIF plugin) {

        AsyncTask.runLaterSync(1, () -> {
            Plugin dynmap = plugin.getServer().getPluginManager().getPlugin("dynmap");
            if (dynmap == null) return;

            if (dynmap instanceof DynmapAPI api) handle = new DynmapHandle(this, api);

            reloadConfigs();
            registerListeners(plugin);
            loadExistingChunks(plugin);
        });
    }

    private void loadExistingChunks(MainIF plugin) {
        Map<String, ArrayList<Claim>> claims = plugin.getClaimManager().CLAIMS;
        for (String worldName : claims.keySet()) {
            World world = Bukkit.getWorld(worldName);
            if (world == null) continue;

            for (Claim claim : claims.get(worldName)) {
                Chunk chunk = world.getChunkAt(claim.getX(), claim.getY());
                String registry = claim.getRegistry();
                if (registry == null) continue;

                claim(chunk, registry);
            }
        }
    }

    public void log(Level level, String msg) {
        MainIF.logMessage(level, msg);
    }

    public void claim(@NotNull Chunk chunk, @NotNull String registry) {
        Faction faction = FactionUtility.getFactionByRegistry(registry);
        if (faction == null) {
            try {
                int color = ClaimManager.getRegistryColor(registry);
                handle.claim(chunk, color, ClaimManager.getDisplay(registry));
            } catch (IllegalArgumentException ignored) {
            }
        } else handle.claim(chunk, faction);
    }

    private void registerListeners(MainIF plugin) {
        if (handle != null) plugin.getServer().getPluginManager().registerEvents(new ChunkListener(this), plugin);
    }

    @Override
    public void reloadConfigs() {
        setConfigDefaultValue("opacity", 0.6d);
        opacity = configValue("opacity");
    }
}
