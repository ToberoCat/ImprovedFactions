package io.github.toberocat.core.utility.config;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Utility;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class Config<T> {

    private String path;
    private T value;

    private boolean autoSave;
    private boolean changes;

    private ItemStack itemIcon;

    private String configFile;

    public Config(String path, String configFile, ItemStack icon) {
        this.path = path;
        this.configFile = configFile;
        this.autoSave = true;
        this.changes = false;
        this.itemIcon = icon == null ? Utility.createItem(Material.GRASS_BLOCK, "&e&l" + path) : icon;
    }

    public Config write(T obj) {
        MainIF.getConfigManager().getDataManager(configFile).getConfig().set(path, obj);
        if (autoSave) MainIF.getConfigManager().getDataManager(configFile).saveConfig();
        changes = true;
        return this;
    }

    public Config writeDefault(T obj) {
        MainIF.getConfigManager().getDataManager(configFile).getConfig().addDefault(path, obj);
        MainIF.getConfigManager().getDataManager(configFile).getConfig().options().copyDefaults(true);
        if (autoSave) MainIF.getConfigManager().getDataManager(configFile).saveConfig();
        changes = true;
        return this;
    }

    public Config Reload() {
        MainIF.getConfigManager().getDataManager(configFile).reloadConfig();
        value = (T) MainIF.getConfigManager().getDataManager(configFile).getConfig().get(path);
        return this;
    }

    public String getPath() {
        return path;
    }

    public DataManager getManager() {
        return MainIF.getConfigManager().getDataManager(configFile);
    }

    public String getConfigFile() {
        return configFile;
    }

    public T getValue() {
        return value;
    }

    public boolean isAutoSave() {
        return autoSave;
    }

    public void setAutoSave(boolean autoSave) {
        this.autoSave = autoSave;
    }

    public boolean hasChanged() {
        return changes;
    }

    public void setChanges(boolean changes) {
        this.changes = changes;
    }

    public ItemStack getItemIcon() {
        return itemIcon;
    }
}
