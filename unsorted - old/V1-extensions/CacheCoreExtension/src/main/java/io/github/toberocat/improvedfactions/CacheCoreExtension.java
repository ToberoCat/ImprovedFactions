package io.github.toberocat.improvedfactions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.extensions.Extension;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.improvedfactions.events.FullCacheCreatedEvent;
import io.github.toberocat.improvedfactions.listener.ChunkListener;
import io.github.toberocat.improvedfactions.listener.FactionListener;
import io.github.toberocat.improvedfactions.listener.PlayerListener;
import io.github.toberocat.improvedfactions.listener.WorldListener;
import io.github.toberocat.improvedfactions.mesh.MeshCache;

import java.util.List;
import java.util.logging.Level;

public class CacheCoreExtension extends Extension {
    public static MeshCache meshCache;

    @Override
    protected void onEnable(MainIF plugin) {
        meshCache = new MeshCache();

        AsyncTask.runLater(1, () -> {
            long start = System.currentTimeMillis();

            MainIF.logMessage(Level.INFO, "&aCaching particle meshes... this may take a while");
            createShapes(plugin);
            registerListeners(plugin);
            AsyncTask.callEventSync(new FullCacheCreatedEvent());
            MainIF.logMessage(Level.INFO, String.format("&aCaching particle meshes finished. Took &6%d ms",
                    System.currentTimeMillis() - start));
        });
    }

    @Override
    protected void onDisable(MainIF plugin) {
        meshCache.dispose();
    }

    private void registerListeners(MainIF plugin) {
        List.of(new PlayerListener(),
                        new ChunkListener(),
                        new FactionListener(),
                        new WorldListener())
                .forEach(x -> plugin.getServer().getPluginManager().registerEvents(x, plugin));
    }

    private void createShapes(MainIF plugin) {
        meshCache.cacheFactions();
        meshCache.cacheZones(false);
    }
}
