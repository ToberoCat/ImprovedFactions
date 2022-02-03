package io.github.toberocat.core.utility.config;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.events.ConfigSaveEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ConfigManager {
    private MainIF plugin;

    public ConfigManager(MainIF plugin) {
        this.plugin = plugin;
    }

    public <T> Config<T> AddConfig(String path, T value) {
        return AddConfig(path, "config.yml", value);
    }

    public <T> Config AddConfig(String path, String configFile, T value) {
        Config config = new Config<T>(path, configFile, null);
        config.write(value).Reload();
        plugin.getConfigMap().put(path, config);
        return plugin.getConfigMap().get(path);
    }

    public <T> Config<T> AddConfig(String path, String configFile, T value, ItemStack icon) {
        Config config = new Config<T>(path, configFile, icon);
        config.write(value).Reload();
        plugin.getConfigMap().put(path, config);
        return plugin.getConfigMap().get(path);
    }


    public DataManager getDataManager(String managerPath) {
        return plugin.getDataManagers().get(managerPath);
    }

    public DataManager AddManager(String dataManager) {
        DataManager manager = new DataManager(MainIF.getIF(), dataManager);
        return plugin.getDataManagers().put(dataManager, manager);
    }

    public DataManager AddManager(String dataManager, Material icon, String name, String... lore) {
        DataManager manager = new DataManager(MainIF.getIF(), Utility.createItem(icon, name, lore), dataManager);
        return plugin.getDataManagers().put(dataManager, manager);
    }

    public <T> Config<T> AddToDefaultConfig(String path, T defaultValue, ItemStack icon) {
        return AddToDefaultConfig(path, "config.yml", defaultValue, icon);
    }
    public <T> Config<T> AddToDefaultConfig(String path, T defaultValue, Material icon, String title, String... lore) {
        return AddToDefaultConfig(path, "config.yml", defaultValue, Utility.createItem(icon, title, lore));
    }

    public <T> Config<T> AddToDefaultConfig(String path, T defaultValue) {
        return AddToDefaultConfig(path, "config.yml", defaultValue, null);
    }

    public <T> Config<T> AddToDefaultConfig(String path, T defaultValue, String configFile) {
        return AddToDefaultConfig(path, configFile, defaultValue, null);
    }

    public <T> Config<T> AddToDefaultConfig(String path, String configFile, T defaultValue, ItemStack icon) {
        Config config = new Config<T>(path, configFile, icon);
        config.writeDefault(defaultValue).Reload();
        plugin.getConfigMap().put(path, config);
        return plugin.getConfigMap().get(path);
    }

    public <T> Config<T> AddToDefaultConfig(String path, String configFile, T defaultValue, Material icon, String title, String... lore) {
        Config config = new Config<T>(path, configFile, Utility.createItem(icon, title, lore));
        config.writeDefault(defaultValue).Reload();
        plugin.getConfigMap().put(path, config);
        return plugin.getConfigMap().get(path);
    }

    public void addConfigListener(ConfigSaveEvent event) {
        plugin.getSaveEvents().add(event);
    }

    public boolean containConfig(String path) {
        return plugin.getConfigMap().containsKey(path);
    }

    public Config getConfig(String path)
    {
        if (!plugin.getConfigMap().containsKey(path)) {
            return null;
        }

        return plugin.getConfigMap().get(path);
    }

    public <T> T getValue(String path) {
        Config config = getConfig(path);
        if (config == null)
            return null;
        return (T) config.getValue();
    }
}
