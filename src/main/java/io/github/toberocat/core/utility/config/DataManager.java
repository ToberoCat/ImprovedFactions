package io.github.toberocat.core.utility.config;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Utility;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class DataManager {

    private final JavaPlugin plugin;
    private FileConfiguration dataConfig = null;
    private File configFile = null;
    private final String fileName;
    private ItemStack itemIcon;

    public DataManager(JavaPlugin plugin, ItemStack itemIcon, String fileName) {
        this.plugin = plugin;
        this.fileName = fileName;

        if (itemIcon != null) {
            List<String> lore = itemIcon.getItemMeta().getLore();
            if (lore == null) {
                lore = new ArrayList<>();
            }
            lore.add("&8Click to configure");

            this.itemIcon = Utility.modiflyItem(itemIcon, itemIcon.getItemMeta().getDisplayName(), lore.toArray(String[]::new));
        } else {
            this.itemIcon = Utility.createItem(Material.GRASS_BLOCK, "&e&l" + fileName);
        }

        saveDefaultConfig();
        saveConfig();
    }

    public DataManager(MainIF plugin, String dataManager) {
        this(plugin, null, dataManager);
    }

    public void reloadConfig() {
        if (this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), fileName);
        this.dataConfig = YamlConfiguration.loadConfiguration(this.configFile);

        InputStream defaultStream = this.plugin.getResource(fileName);
        if (defaultStream != null) {
            YamlConfiguration defaultConfig = YamlConfiguration.loadConfiguration(new InputStreamReader(defaultStream));
            this.dataConfig.setDefaults(defaultConfig);
        }
    }

    public FileConfiguration getConfig() {
        if (this.dataConfig == null)
            reloadConfig();
        return this.dataConfig;
    }

    public void saveConfig() {
        if (this.dataConfig == null || this.configFile == null)
            return;

        try {
            this.getConfig().set("version", MainIF.getVersion().toString());
            this.getConfig().save(this.configFile);
        } catch (IOException e) {
            Utility.except(e);
        }
    }

    public void saveDefaultConfig() {
        if(this.configFile == null)
            this.configFile = new File(this.plugin.getDataFolder(), fileName);
        if (!this.configFile.exists()) {
            this.plugin.saveResource(fileName, false);
        }
    }

    public String getFileName() {
        return fileName;
    }

    public ItemStack getItemIcon() {
        return itemIcon;
    }
}
