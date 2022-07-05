package io.github.toberocat.improvedfactions.listener;

import io.github.toberocat.improvedfactions.CacheCoreExtension;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldInitEvent;
import org.bukkit.event.world.WorldLoadEvent;
import org.bukkit.event.world.WorldUnloadEvent;

public class WorldListener implements Listener {
    @EventHandler
    public void createWorld(WorldInitEvent event) {
        CacheCoreExtension.meshCache.createWorldCache(event.getWorld(), true);
    }

    @EventHandler
    public void loadWorld(WorldLoadEvent event) {
        CacheCoreExtension.meshCache.createWorldCache(event.getWorld(), true);
    }

    @EventHandler
    public void unloadWorld(WorldUnloadEvent event) {
        CacheCoreExtension.meshCache.unloadWorldCache(event.getWorld());
    }
}
