package io.github.toberocat;

import io.github.toberocat.core.bstat.Bstat;
import io.github.toberocat.core.commands.FactionCommand;
import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.extensions.Extension;
import io.github.toberocat.core.extensions.ExtensionLoader;
import io.github.toberocat.core.extensions.ExtensionRegistry;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionUtility;
import io.github.toberocat.core.factions.permission.FactionPerm;
import io.github.toberocat.core.factions.rank.Rank;
import io.github.toberocat.core.listeners.*;
import io.github.toberocat.core.papi.FactionExpansion;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.bossbar.SimpleBar;
import io.github.toberocat.core.utility.calender.TimeCore;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.config.Config;
import io.github.toberocat.core.utility.config.ConfigManager;
import io.github.toberocat.core.utility.config.DataManager;
import io.github.toberocat.core.utility.data.DataAccess;
import io.github.toberocat.core.utility.dynamic.loaders.DynamicLoader;
import io.github.toberocat.core.utility.events.ConfigSaveEvent;
import io.github.toberocat.core.utility.events.bukkit.PlayerJoinOnReloadEvent;
import io.github.toberocat.core.utility.gui.Gui;
import io.github.toberocat.core.utility.items.ItemCore;
import io.github.toberocat.core.utility.jackson.JsonUtility;
import io.github.toberocat.core.utility.jackson.YmlUtility;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.map.MapHandler;
import io.github.toberocat.core.utility.messages.MessageSystem;
import io.github.toberocat.core.utility.settings.FactionSettings;
import io.github.toberocat.core.utility.settings.PlayerSettings;
import io.github.toberocat.core.utility.version.Version;
import io.github.toberocat.versions.nms.NMSFactory;
import io.github.toberocat.versions.nms.NMSInterface;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalTime;
import java.util.*;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getPluginManager;

/**
 * This is the main class of the Improved Factions plugin
 */
public final class MainIF extends JavaPlugin {

    public static final Version VERSION = Version.from("1.3.2");

    public static final HashMap<String, Extension> LOADED_EXTENSIONS = new HashMap<>();

    //<editor-fold desc="Variables">
    private static MainIF INSTANCE;
    private static Economy economy;
    private static ConfigManager configManager;
    private final List<ConfigSaveEvent> saveEvents = new ArrayList<>();
    private final Map<String, Config> configMap = new HashMap<>();
    private final Map<String, ArrayList<String>> backupFile = new HashMap<>(); // Delete the backup map after backup got restored
    private final Map<String, DataManager> dataManagers = new HashMap<>();
    private NMSInterface nms;
    private boolean standby = false;
    private ClaimManager claimManager;
    //</editor-fold>

    //<editor-fold desc="Overrides">

    /**
     * Send a message to the server.
     * This won't send a message, if the level isn't represented in debug.logLevel (config.yml)
     *
     * @param level   That's the level you want to log
     * @param message The message you want to get logged
     */
    public static void logMessage(Level level, String message) {
        Utility.run(() -> {
            List<String> values;
            if (!INSTANCE.isEnabled()) {
                values = Arrays.asList("INFO", "WARNING", "SEVERE");
            } else {
                values = configManager.getValue("debug.logLevel");
            }

            if (!values.contains(level.toString())) return;

            if (configManager.getValue("general.colorConsole")) {
                //Bukkit.getLogger().log(level, );
                Bukkit.getLogger().log(level, Language.format("&7[&e&lImprovedFactions&7] " + message));
            } else {
                Bukkit.getLogger().log(level,
                        ChatColor.stripColor(Language.format("&7[&e&lImprovedFactions&7] " + message)));
            }
        });
    }

    /**
     * Get the instance of this plugin
     *
     * @return The instance of this plugin
     */
    public static MainIF getIF() {
        return INSTANCE;
    }
    //</editor-fold>

    //<editor-fold desc="Public functions">

    /**
     * Get the economy for this plugin
     * The economy can be null, if Vault + A economy supoorting pluign (e.g.: Essentails) is not found
     */
    public static Economy getEconomy() {
        return economy;
    }

    /**
     * Get the current plugin version
     *
     * @return The version
     */
    public static Version getVersion() {
        return VERSION;
    }

    /**
     * Get the manager to add, load and reload config data
     *
     * @return The configManager instance
     */
    public static ConfigManager getConfigManager() {
        return configManager;
    }

    public static void registerPapi() {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            FactionExpansion.init();
            new FactionExpansion().register();
            MainIF.logMessage(Level.INFO, "&aSuccessfully loaded &6PlaceholderAPI&a support");
        }
    }

    /**
     * Don't call this manually.
     * This will get called by the minecraft server
     */
    @Override
    public void onEnable() {
        Utility.run(() -> {
            INSTANCE = this;

            generateConfigs();

            loadListeners();

            if (!initializeCores()) return;
            if (!loadPluginVersion()) return;

            loadPluginDependencies();

            FactionCommand command = new FactionCommand();
            getServer().getPluginCommand("faction").setExecutor(command);
            getServer().getPluginCommand("faction").setTabCompleter(command);

            for (Player player : getServer().getOnlinePlayers()) {
                Bukkit.getPluginManager().callEvent(new PlayerJoinOnReloadEvent(player));
                PlayerJoinListener.PLAYER_JOINS.put(player.getUniqueId(), System.currentTimeMillis());
            }


            Bukkit.getScheduler().scheduleSyncRepeatingTask(this, () -> {
                Debugger.log("Unloading unused factions...");
                List<String> unused = new ArrayList<>();
                for (Faction faction : Faction.getLoadedFactions().values())
                    if (faction.getFactionMemberManager().getOnlinePlayers().size() == 0)
                        unused.add(faction.getRegistryName());


                for (String registry : unused) Faction.getLoadedFactions().remove(registry);

            }, 0, 20 * 60 * 5);

            AsyncTask.runLaterSync(1, DynamicLoader::enable);

            if (Boolean.TRUE.equals(getConfigManager().getValue("general.autoMigrate"))) tryMigration();
        });
    }

    //</editor-fold>

    //<editor-fold desc="Loading functions">

    /**
     * Don't call this manually.
     * This will get called by the minecraft server
     */
    @Override
    public void onDisable() {
        Utility.run(() -> {
            AsyncTask.cleanup();

            for (Extension extension : LOADED_EXTENSIONS.values()) {
                if (extension.isEnabled()) extension.disable(this);
            }

            LOADED_EXTENSIONS.clear();

            saveConfigs();
            DataAccess.disable();
            DynamicLoader.disable();
            for (Player player : Bukkit.getOnlinePlayers()) player.closeInventory();

            cleanup();

            INSTANCE = null;
        });
    }

    private void cleanup() {
        saveEvents.clear();
        backupFile.clear();
        dataManagers.clear();
        configMap.clear();
        SimpleBar.cleanup();
        PlayerJoinListener.PLAYER_JOINS.clear();
    }

    private void tryMigration() {
        if (new File(getDataFolder().getPath() + "/Data/factions.yml").exists() &&
                new File(getDataFolder().getPath() + "/Data/chunkData.yml").exists()) {
            Faction.migrateFaction();
            logMessage(Level.INFO, "Please don't stop the server. Chunks are getting migrated. This can take some time, depending on your file size. If you don't want it, disable &6general.autoMigrate &7in the config.yml and reload the server");
            ClaimManager.migrate();

            logMessage(Level.INFO, "&aAuto-Migrated your old beta files to the newest version. This process will restart every server reload if you don't delete factions.yml and chunkData.yml in the Data folder only if no errors / warnings appeared while migration. Please fix them before deleting your old files");
        }
    }

    /**
     * This saves everything that can be saved to prevent data loss when an error happens.
     * In a normal case, it will only put the plugin in standby, to enable simple land protection
     * This formats the error message
     *
     * @param shutdownMessage This message will be printed to console before disabling the plugin
     * @see Language#format(String)
     */
    public void saveShutdown(String shutdownMessage) {
        Utility.run(() -> {
            if (standby) {
                logMessage(Level.SEVERE, "&c" + shutdownMessage);
                return;
            }
            standby = true;

            logMessage(Level.SEVERE, "&c" + shutdownMessage);
            logMessage(Level.WARNING, "ImprovedFactions put it self in standby. All commands will be disabled. Only simple claim protection is working");

            for (Player player : Bukkit.getOnlinePlayers()) {
                if (standby && player.hasPermission("factions.messages.standby")) {
                    Language.sendMessage("message.plugin-standby", player);
                }
            }

            ArrayList<String> standbyCommands = configManager.getValue("commands.standby");

            if (!isEnabled()) return;
            if (standbyCommands == null) return;

            AsyncTask.runLaterSync(0, () -> {
                for (String command : standbyCommands) {
                    Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(), command);
                }
            });
        });
    }

    public boolean loadExtensions() throws IOException, ClassNotFoundException {
        File extFolder = new File(getDataFolder().getPath() + "/Extensions");
        extFolder.mkdir();

        if (!extFolder.exists()) return true;

        File[] extensions = extFolder.listFiles();
        if (extensions == null) return true;

        List<LangMessage> langMessages = new ArrayList<>();
        for (File jar : extensions) {
            if (!jar.getName().endsWith(".jar")) continue;

            ExtensionRegistry registry = loadRegistry(jar);
            if (registry == null) continue;

            LangMessage extensionLang = getExtensionLangFile(jar);
            if (extensionLang != null) langMessages.add(extensionLang);


            //Extension extension = loader.LoadClass(jar, "extension.Main", Extension.class);
            Extension extension = ExtensionLoader.loadClass(jar, registry.main(), Extension.class);
            extension.enable(registry, this);

            if (!extension.isEnabled()) continue;

            LOADED_EXTENSIONS.put(extension.getRegistry().registry(), extension);
        }

        for (LangMessage message : langMessages) LangMessage.addDefault(message);

        return true;
    }

    private LangMessage getExtensionLangFile(File file) throws MalformedURLException {
        String path = "jar:file:\\" + file.getAbsolutePath() + "!/en_us.lang";

        URL inputURL = new URL(path);
        JarURLConnection conn = null;
        try {
            conn = (JarURLConnection) inputURL.openConnection();
            InputStream in = conn.getInputStream();
            return JsonUtility.readObject(in, LangMessage.class);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    private ExtensionRegistry loadRegistry(File file) throws MalformedURLException {
        String path = "jar:file:\\" + file.getAbsolutePath() + "!/extension.yml";

        URL inputURL = new URL(path);
        JarURLConnection conn;
        try {
            conn = (JarURLConnection) inputURL.openConnection();
            InputStream in = conn.getInputStream();
            return YmlUtility.loadYml(in, ExtensionRegistry.class);
        } catch (IOException e) {
            e.printStackTrace();
            logMessage(Level.SEVERE, "&6" + file.getName() + " &ccouldn't get loaded. Please make sure this extension isn't for a beta version, else redownload it please");
        }

        return null;
    }

    /**
     * Save all configs and extra data. If something happens while saving, it will save a backup.
     * That can seen when using /f config backup ingame
     *
     * @return Returns a list of all successfully saved configs
     */
    public List<String> saveConfigs() {
        List<String> savedConfigs = new ArrayList<>();

        for (Config config : configMap.values()) {
            boolean autoSave = config.isAutoSave();
            config.setAutoSave(false);
            config.setChanges(false);

            if (!callSaveEvents(config)) {
                saveConfigBackup(config);
            } else if (!savedConfigs.contains(config.getConfigFile())) {
                savedConfigs.add(config.getConfigFile());
            }

            if (config.hasChanged()) {
                config.getManager().saveConfig();
            }
            config.setChanges(false);
            config.setAutoSave(autoSave);
        }

        for (ConfigSaveEvent event : saveEvents) {
            if (event.isSingleCall() == ConfigSaveEvent.SaveType.DataAccess) {
                Result result = event.Save(null);
                if (!result.isSuccess())
                    saveDataAccessBackup(result.getMachineMessage(), result.getPaired());
                else savedConfigs.add(result.getMachineMessage());
            }
        }

        return savedConfigs;
    }

    private <T> void saveDataAccessBackup(String file, T value) {
        logMessage(Level.WARNING, "&cCouldn't save &6" + file + "&c. File got saved in datAcc_backup folder. Please restart the plugin so the files can be compared without data loss");
        File pathAsFile = new File(getDataFolder().getPath() + "/.temp/datAcc_backups");

        if (!pathAsFile.exists()) {
            Utility.run(() -> {
                if (!pathAsFile.mkdirs()) {
                    logMessage(Level.SEVERE, "&cCouldn't save &6" + pathAsFile.getPath() + "&c to backups");
                }
            });
        }

        if (pathAsFile.exists()) JsonUtility.saveObject(new File(pathAsFile.getPath() + "/" + file), value);
    }

    private void saveConfigBackup(Config config) {
        Utility.run(() -> {
            logMessage(Level.WARNING, "&cCouldn't save &6" + config.getPath() + "&c. File got saved in config_backup folder. Please restart the plugin so the files can be compared without data loss");
            File pathAsFile = new File(getDataFolder().getPath() + "/.temp/config_backups");

            if (!Files.exists(Paths.get(pathAsFile.getPath()))) {
                Utility.run(() -> {
                    if (!pathAsFile.mkdirs()) {
                        logMessage(Level.SEVERE, "&cCouldn't save &6" + pathAsFile.getPath() + "&c to backups");
                    }
                });
            }

            File backupFile = new File(pathAsFile.getPath() + "/" + config.getManager().getFileName() + "_" + LocalTime.now().toSecondOfDay() + ".backup");

            List<String> paths = null;
            if (Files.exists(Paths.get(backupFile.getPath()))) {
                paths = (List<String>) JsonUtility.readObject(backupFile, List.class);
            }
            paths = paths == null ? new ArrayList<>() : paths;

            String toSave = config.getPath() + ":" + config.getValue();

            paths.add(toSave);

            JsonUtility.saveObject(backupFile, paths);
        });
    }

    private boolean callSaveEvents(Config config) {
        for (ConfigSaveEvent event : saveEvents) {
            if (event.isSingleCall() == ConfigSaveEvent.SaveType.Config && !event.Save(config).isSuccess())
                return false;
        }
        return true;
    }

    private void generateConfigs() {
        Utility.run(() -> {
            DataManager oldConfig = new DataManager(this, "config.yml");
            String version = oldConfig.getConfig().getString("version");
            if (version != null && version.toLowerCase().contains("betav")) {
                logMessage(Level.INFO, "&cDeleting old config files, as they aren't needed anymore. Lang files will stay, but please consider cleaning them from the unused paths. Compare them to up-to-date ones or delete them completely, if they aren't needed anymore");
                new File(getDataFolder().getPath() + "/config.yml").delete();
                new File(getDataFolder().getPath() + "/language.yml").delete();
                new File(getDataFolder().getPath() + "/extConfig.yml").delete();
                new File(getDataFolder().getPath() + "/commands.yml").delete();
            }

            configManager = new ConfigManager(this);
            configManager.register();

            //<editor-fold desc="Loading backups">
            File backupFolder = new File(getDataFolder().getPath() + "/.temp/backups");

            if (!backupFolder.exists()) backupFolder.mkdirs();

            for (File file : backupFolder.listFiles()) {
                ArrayList<String> data = (ArrayList<String>) JsonUtility.readObject(file, ArrayList.class);

                logMessage(Level.WARNING, "&cLoaded " + file.getName() + " backup. Please use &7/f config backup&c to decide what should be finally used");
                backupFile.put(file.getName(), data);

                file.delete();
            }
            //</editor-fold>
        });
    }

    private void loadListeners() {
        Arrays.asList(
                        new PlayerJoinListener(),
                        new PlayerLeaveListener(),
                        new GuiListener(),
                        new PlayerMoveListener(),
                        new BlockBreakListener(),
                        new BlockPlaceListener(),
                        new InteractListener(),
                        new PlayerMountListener())
                .forEach(listener -> getPluginManager().registerEvents(listener, this));
    }

    //</editor-fold>

    //<editor-fold desc="Getters and Setters">

    private boolean loadPluginVersion() {
        String sVersion = Bukkit.getBukkitVersion();
        NMSInterface nms;

        if (sVersion.contains("1.18")) {
            nms = NMSFactory.create_1_18();
        } else if (sVersion.contains("1.17")) {
            nms = NMSFactory.create_1_17();
        } else if (sVersion.contains("1.16")) {
            nms = NMSFactory.create_1_16();
        } else {
            saveShutdown("§cCouldn't load ImprovedFactions &6" + VERSION +
                    "&c. The plugin didn't find a version for your server. Your server version: &6"
                    + sVersion + "&c. Available versions: &6" + Arrays.toString(NMSFactory.versions));
            getPluginManager().disablePlugin(this);
            return false;
        }

        nms.EnableInterface();
        return true;
    }

    private void loadPluginDependencies() {
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!setupEconomy()) {
                    logMessage(Level.WARNING, "&eDisabled faction economy! Needs Vault and an Economy plugin" +
                            " installed to enable it");
                } else {
                    logMessage(Level.INFO, "&aEnabled faction economy");
                }
            }
        }.runTaskLater(this, 0);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> eco = getServer().getServicesManager().getRegistration(Economy.class);
        if (eco != null) {
            economy = eco.getProvider();
        }
        return economy != null;
    }

    private boolean initializeCores() throws IOException, ClassNotFoundException {
        if (!loadExtensions()) return false;

        if (!Language.init(this, getDataFolder())) return false;
        if (!TimeCore.init()) return false;
        if (!DataAccess.init()) return false;


        FactionSettings.register();
        PlayerSettings.register();
        FactionPerm.register();
        ItemCore.register();
        MapHandler.register();
        Bstat.register(this);
        registerPapi();

        claimManager = new ClaimManager();
        new FactionUtility();
        new MessageSystem();

        Rank.Init();

        return true;
    }

    /**
     * Add a listener for events while the plugin is running
     * NOTE: This could cause some troubles if the event is getting called while adding
     *
     * @param listener The listener that should be added
     */
    public void registerListener(Listener listener) {
        getPluginManager().registerEvents(listener, this);
    }

    /**
     * Get the data managers. These are needed to load config settings
     *
     * @return A map of String:Datamanager pairs. The string is the datamanager file name
     */
    public Map<String, DataManager> getDataManagers() {
        return dataManagers;
    }

    /**
     * Get if the plugin is in standby. If it is, disable everything that could load data, access factions and manage settings
     *
     * @return A value that tells if the plugin ha put itself into standby
     */
    public boolean isStandby() {
        return standby;
    }

    /**
     * Get the list of backups read while enabling / reloading the plugin
     *
     * @return This will be empty if no .backup will be found
     */
    public Map<String, ArrayList<String>> getBackupFile() {
        return backupFile;
    }

    /**
     * Get all loaded config settings
     *
     * @return config map. String is for path. E.g: general.prefix
     */
    public Map<String, Config> getConfigMap() {
        return configMap;
    }

    /**
     * Get the save events that will be called when something gets saved.
     * You can tell if the file should be saved as backup, or add your own backup system
     *
     * @return a list of conifg save events
     */
    public List<ConfigSaveEvent> getSaveEvents() {
        return saveEvents;
    }

    public ClaimManager getClaimManager() {
        return claimManager;
    }

    //</editor-fold>
}
