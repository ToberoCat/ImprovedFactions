package io.github.toberocat.core.migration;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.config.DataManager;

import java.io.File;

public class Migrator {

    private DataManager manager;
    private MainIF plugin;

    public Migrator(MainIF plugin) {
        this.plugin = plugin;
        String path = plugin.getDataFolder().getParentFile().getPath() + "/ImprovedFactions/Data/factions.yml";
        if (!new File(path).exists()) return;

        this.manager = new DataManager(plugin, path);
    }

    public void migrate() {
        if (manager == null) return;

        for (String key : manager.getConfig().getConfigurationSection("f").getKeys(false)) {

        }
    }

}
