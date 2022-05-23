package io.github.toberocat.improvedfactions.module;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionModule;
import org.bukkit.Location;

public class HomeModule extends FactionModule {
    private Location home;

    public HomeModule() {
        super(null);
    }

    public HomeModule(Faction faction) {
        super(faction);
    }

    public Location getHome() {
        return home;
    }

    public HomeModule setHome(Location home) {
        this.home = home;
        return this;
    }
}
