package io.github.toberocat.improvedfactions.listeners;

import io.github.toberocat.improvedfactions.ImprovedFactionsMain;
import io.github.toberocat.improvedfactions.factions.Faction;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.world.WorldSaveEvent;

public class OnWorldSaveListener implements Listener {

    @EventHandler
    public void onWorldSave(WorldSaveEvent event) {
        Faction.SaveFactions(ImprovedFactionsMain.getPlugin());
    }

}
