package io.github.toberocat.improvedfactions.listener;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.utility.events.faction.FactionCreateEvent;
import io.github.toberocat.core.utility.events.faction.FactionLoadEvent;
import io.github.toberocat.improvedfactions.HomeExtension;
import io.github.toberocat.improvedfactions.module.HomeModule;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class LoadFactionListener implements Listener {

    @EventHandler
    public void load(FactionLoadEvent event) {
        Faction faction = event.getFaction();

        if (faction.getModules().containsKey(HomeExtension.HOME_EXTENSION_REGISTRY)) return;
        faction.getModules().put(HomeExtension.HOME_EXTENSION_REGISTRY, new HomeModule(faction));
    }
}
