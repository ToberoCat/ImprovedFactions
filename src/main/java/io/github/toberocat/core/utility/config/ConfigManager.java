package io.github.toberocat.core.utility.config;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.events.ConfigSaveEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public record ConfigManager(MainIF plugin) {

    public void register() {
        AddManager("config.yml", Material.BOOK, "&a&lConfig.yml");
        AddManager("commands.yml", Material.COMMAND_BLOCK, "&a&lCommands.yml");

        AddToDefaultConfig("debug.logLevel", new String[]{
                Level.INFO.toString(), Level.WARNING.toString(), Level.SEVERE.toString()
        });

        AddToDefaultConfig("general.prefix", "&e&lImprovedFactions", Utility.createItem(Material.NAME_TAG, "&e&lPrefix"));
        AddToDefaultConfig("general.printStacktrace", false, Utility.createItem(Material.YELLOW_DYE, "&e&lPrint Stacktrace"));
        AddToDefaultConfig("general.commandDescriptions", true);
        AddToDefaultConfig("general.useSQL", false, Material.COBWEB, "&b&lUse sql",
                "&8Sql is a database", "&8I would recommend to use it", "&8when you have a lot players", "&8on your server", "", "&6&lPerformance: &cHeavy");
        AddToDefaultConfig("general.colorConsole", true);
        AddToDefaultConfig("general.debugMode", false, Material.COBWEB, "&b&lDebug mode",
                "&8Get extra infos", "&8Usefull when debugging,", "&8or needing help by moderators");

        AddToDefaultConfig("gui.wrapLength", 20);

        AddToDefaultConfig("forbidden.checkFactionNames", true);
        AddToDefaultConfig("forbidden.disbandAtPercent", 69.99f);
        AddToDefaultConfig("forbidden.reportAtPercent", 39.99f);
        AddToDefaultConfig("forbidden.checkLeetspeak", true);
        AddToDefaultConfig("forbidden.factionNames", new String[]{
                "fuck", "ass", "stupid"
        });

        AddToDefaultConfig("power.maxPowerPerPlayer", 5);
        AddToDefaultConfig("power.maxDefaultFaction", 20);
        AddToDefaultConfig("power.regenerationPerHour", 4);
        AddToDefaultConfig("power.memberDeathConsume", 10);
        AddToDefaultConfig("power.chunkPowerConsume", 3);
        AddToDefaultConfig("power.enabled", true);

        AddToDefaultConfig("history.territoryChange", false);

        AddToDefaultConfig("faction.permanent", false);
        AddToDefaultConfig("faction.maxNameLen", 10);

        AddToDefaultConfig("commands.standby", new String[]{"tellraw @a {\"text\":\"Standby enabled\"}"}, Utility.createItem(Material.COMMAND_BLOCK, "&e&lStandyBy", new String[]{
                "&8Write a list of commands", "&8That should get executed, when", "&8the plugin goes in standby mode"}));
        AddToDefaultConfig("commands.forbidden", new String[]{"tellraw @a {\"text\":\"This word, {word}, is maybe similar to {similar}. Used: {player_name}, {player_uuid} while {task}. {similarityPer}% similar \"}"}, Utility.createItem(Material.COMMAND_BLOCK, "&e&lForbidden name report", new String[]{
                "&8Write a list of commands", "&8That should get executed, when", "&8the plugin finds a maybe forbidden word"}));
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

    public Config getConfig(String path) {
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
