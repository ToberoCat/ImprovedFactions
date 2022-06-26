package io.github.toberocat.core.utility.config;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.events.ConfigSaveEvent;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.logging.Level;

public record ConfigManager(MainIF plugin) {

    public void register() {
        addManager("config.yml", Material.BOOK, "&a&lConfig.yml");
        addManager("commands.yml", Material.COMMAND_BLOCK, "&a&lCommands.yml");

        addToDefaultConfig("debug.logLevel", new String[]{
                Level.INFO.toString(), Level.WARNING.toString(), Level.SEVERE.toString()
        });

        addToDefaultConfig("general.autoMigrate", true);
        addToDefaultConfig("general.mapViewDistanceW", 4);
        addToDefaultConfig("general.mapViewDistanceH", 4);
        addToDefaultConfig("general.sendCrashesToGithub", true);
        addToDefaultConfig("general.printStacktrace", true, Utility.createItem(Material.YELLOW_DYE, "&e&lPrint Stacktrace"));
        addToDefaultConfig("general.commandDescriptions", true);
        addToDefaultConfig("general.useSQL", false, Material.COBWEB, "&b&lUse sql",
                "&8Sql is a database", "&8I would recommend to use it", "&8when you have a lot players", "&8on your server", "", "&6&lPerformance: &cHeavy");
        addToDefaultConfig("general.colorConsole", true);
        addToDefaultConfig("general.debugMode", false, Material.COBWEB, "&b&lDebug mode",
                "&8Get extra infos", "&8Usefull when debugging,", "&8or needing help by moderators");
        addToDefaultConfig("general.disabledWorlds", new String[] {
                "FactionsWontWorkHere"
        });
        addToDefaultConfig("general.limit-chunks-to-power", false);

        addToDefaultConfig("gui.maxCps", 5);
        addToDefaultConfig("gui.closeGuiCps", 10);
        addToDefaultConfig("gui.wrapLength", 20);

        addToDefaultConfig("forbidden.checkFactionNames", true);
        addToDefaultConfig("forbidden.disbandAtPercent", 69.99f);
        addToDefaultConfig("forbidden.reportAtPercent", 39.99f);
        addToDefaultConfig("forbidden.checkLeetspeak", true);
        addToDefaultConfig("forbidden.factionNames", new String[]{
                "fuck", "ass", "stupid"
        });

        addToDefaultConfig("power.maxPowerPerPlayer", 5);
        addToDefaultConfig("power.powerPerPlayer", 5);
        addToDefaultConfig("power.maxDefaultFaction", 20);
        addToDefaultConfig("power.regenerationPerHour", 4);
        addToDefaultConfig("power.memberDeathConsume", 10);
        addToDefaultConfig("power.chunkPowerConsume", 3);
        addToDefaultConfig("power.enabled", true);

        addToDefaultConfig("history.territoryChange", false);

        addToDefaultConfig("faction.permanent", false);
        addToDefaultConfig("faction.joinTimeout", 24); //Measured in hours
        addToDefaultConfig("faction.allowExplosions", true);
        addToDefaultConfig("faction.maxNameLen", 10);
        addToDefaultConfig("faction.maxTagLen", 3);
        addToDefaultConfig("faction.invitationTimeout", 300, Material.ALLIUM, "&5",
                "&8Get extra infos", "&8Usefull when debugging,", "&8or needing help by moderators");
        addToDefaultConfig("faction.ranks.owner", "Owner");
        addToDefaultConfig("faction.ranks.admin", "Admin");
        addToDefaultConfig("faction.ranks.moderator", "Moderator");
        addToDefaultConfig("faction.ranks.elder", "Elder");
        addToDefaultConfig("faction.ranks.member", "Member");
        addToDefaultConfig("faction.ranks.ally-owner", "Ally owner");
        addToDefaultConfig("faction.ranks.ally-admin", "Ally admin");
        addToDefaultConfig("faction.ranks.ally-moderator", "Ally moderator");
        addToDefaultConfig("faction.ranks.ally-elder", "Ally elder");
        addToDefaultConfig("faction.ranks.ally-member", "Ally member");
        addToDefaultConfig("faction.ranks.guest", "Guest");

        addToDefaultConfig("commands.standby", new String[]{"tellraw @a {\"text\":\"Standby enabled\"}"}, Utility.createItem(Material.COMMAND_BLOCK, "&e&lStandyBy", new String[]{
                "&8Write a list of commands", "&8That should get executed, when", "&8the plugin goes in standby mode"}));
        addToDefaultConfig("commands.forbidden", new String[]{"tellraw @a {\"text\":\"This word, {word}, is maybe similar to {similar}. Used: {player_name}, {player_uuid} while {task}. {similarityPer}% similar \"}"}, Utility.createItem(Material.COMMAND_BLOCK, "&e&lForbidden name report", new String[]{
                "&8Write a list of commands", "&8That should get executed, when", "&8the plugin finds a maybe forbidden word"}));
    }

    public <T> Config<T> addConfig(String path, T value) {
        return addConfig(path, "config.yml", value);
    }

    public <T> Config addConfig(String path, String configFile, T value) {
        Config config = new Config<T>(path, configFile, null);
        config.write(value).Reload();
        plugin.getConfigMap().put(path, config);
        return plugin.getConfigMap().get(path);
    }

    public <T> Config<T> addConfig(String path, String configFile, T value, ItemStack icon) {
        Config config = new Config<T>(path, configFile, icon);
        config.write(value).Reload();
        plugin.getConfigMap().put(path, config);
        return plugin.getConfigMap().get(path);
    }


    public DataManager getDataManager(String managerPath) {
        return plugin.getDataManagers().get(managerPath);
    }

    public DataManager addManager(String dataManager) {
        DataManager manager = new DataManager(MainIF.getIF(), dataManager);
        return plugin.getDataManagers().put(dataManager, manager);
    }

    public DataManager addManager(String dataManager, Material icon, String name, String... lore) {
        DataManager manager = new DataManager(MainIF.getIF(), Utility.createItem(icon, name, lore), dataManager);
        return plugin.getDataManagers().put(dataManager, manager);
    }

    public <T> Config<T> addToDefaultConfig(String path, T defaultValue, ItemStack icon) {
        return addToDefaultConfig(path, "config.yml", defaultValue, icon);
    }

    public <T> Config<T> addToDefaultConfig(String path, T defaultValue, Material icon, String title, String... lore) {
        return addToDefaultConfig(path, "config.yml", defaultValue, Utility.createItem(icon, title, lore));
    }

    public <T> Config<T> addToDefaultConfig(String path, T defaultValue) {
        return addToDefaultConfig(path, "config.yml", defaultValue, null);
    }

    public <T> Config<T> addToDefaultConfig(String path, T defaultValue, String configFile) {
        return addToDefaultConfig(path, configFile, defaultValue, null);
    }

    public <T> Config<T> addToDefaultConfig(String path, String configFile, T defaultValue, ItemStack icon) {
        Config config = new Config<T>(path, configFile, icon);
        config.writeDefault(defaultValue).Reload();
        plugin.getConfigMap().put(path, config);
        return plugin.getConfigMap().get(path);
    }

    public <T> Config<T> addToDefaultConfig(String path, String configFile, T defaultValue, Material icon, String title, String... lore) {
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

    public static <T> T getValue(String path) {
        Config<T> config = MainIF.getConfigManager().getConfig(path);
        if (config == null)
            return null;
        return config.getValue();
    }

    public static <T> T getValue(String path, T def) {
        Config<T> config = MainIF.getConfigManager().getConfig(path);
        if (config == null)
            return def;
        return config.getValue();
    }
}
