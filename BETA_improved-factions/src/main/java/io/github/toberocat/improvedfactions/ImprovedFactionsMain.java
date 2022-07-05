package io.github.toberocat.improvedfactions;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.toberocat.improvedfactions.bar.Bar;
import io.github.toberocat.improvedfactions.bstat.Metrics;
import io.github.toberocat.improvedfactions.commands.FDelCommand;
import io.github.toberocat.improvedfactions.commands.FDelPCommand;
import io.github.toberocat.improvedfactions.commands.FJoin;
import io.github.toberocat.improvedfactions.commands.FactionCommand;
import io.github.toberocat.improvedfactions.data.PlayerData;
import io.github.toberocat.improvedfactions.extentions.Extension;
import io.github.toberocat.improvedfactions.extentions.ExtensionContainer;
import io.github.toberocat.improvedfactions.extentions.list.ExtensionListLoader;
import io.github.toberocat.improvedfactions.factions.Faction;
import io.github.toberocat.improvedfactions.factions.FactionMember;
import io.github.toberocat.improvedfactions.factions.FactionSettings;
import io.github.toberocat.improvedfactions.gui.GuiListener;
import io.github.toberocat.improvedfactions.language.LangMessage;
import io.github.toberocat.improvedfactions.language.Language;
import io.github.toberocat.improvedfactions.listeners.*;
import io.github.toberocat.improvedfactions.papi.FactionExpansion;
import io.github.toberocat.improvedfactions.ranks.Rank;
import io.github.toberocat.improvedfactions.reports.Report;
import io.github.toberocat.improvedfactions.reports.Warn;
import io.github.toberocat.improvedfactions.tab.FactionCommandTab;
import io.github.toberocat.improvedfactions.utility.*;
import io.github.toberocat.improvedfactions.utility.ChunkUtils;
import io.github.toberocat.improvedfactions.utility.configs.DataManager;
import io.github.toberocat.improvedfactions.utility.configs.JsonUtility;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import org.xeustechnologies.jcl.JarClassLoader;
import org.xeustechnologies.jcl.JclObjectFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.concurrent.Callable;
import java.util.logging.Level;

public final class ImprovedFactionsMain extends JavaPlugin {

    public static Map<String, ExtensionContainer> extensions = new HashMap<>();

    public static Map<UUID, PlayerData> playerData = new HashMap<>();

    public static String VERSION = "BETAv5.0.7";

    public static UpdateChecker updateChecker;

    private static ImprovedFactionsMain INSTANCE;

    private PlayerMessages playerMessages;

    private SignMenuFactory signMenuFactory;
    private ProtocolManager protocolManager;
    private Economy economy;

    private DataManager languageData;
    private DataManager factionData;
    private DataManager messagesData;
    private DataManager extConfigData;
    private DataManager commandData;
    private DataManager chunkData;


    public static List<Report> REPORTS = new ArrayList<>();
    public static Warn WARNS;

    @Override
    public void onEnable() {
        INSTANCE = this;

        Bukkit.getLogger().log(Level.INFO, Language.format("[Factions] Running " + VERSION + " of Improved Factions (Factions)"));
        //Create extension folder
        File extensionFile = new File(getDataFolder().getPath() + "/Extensions");
        extensionFile.mkdir();

        GuiListener guiListener = new GuiListener();

        Rank.Init();

        //Data Managers / Config
        //Language.yml
        languageData = new DataManager(this, "language.yml");
        factionData = new DataManager(this, "Data/factions.yml");
        messagesData = new DataManager(this, "Data/messages.yml");
        chunkData = new DataManager(this, "Data/chunkData.yml");
        extConfigData = new DataManager(this, "extConfig.yml");
        commandData = new DataManager(this, "commands.yml");

        playerMessages = new PlayerMessages(this);
        //Config defaults
        getConfig().addDefault("factions.maxMembers", 50);
        getConfig().addDefault("factions.startClaimPower", 10);
        getConfig().addDefault("factions.powerPerPlayer", 5);
        getConfig().addDefault("factions.powerLossPerDeath", 5);
        getConfig().addDefault("factions.regenerationPerRate", 1);
        getConfig().addDefault("factions.regenerationRate", 3600000);
        getConfig().addDefault("factions.minPower", 0);

        getConfig().addDefault("factions.permanent", false);


        getConfig().addDefault("general.noFactionPapi", "None");
        getConfig().addDefault("general.updateChecker", true);
        getConfig().addDefault("general.mapViewDistanceW", 7);
        getConfig().addDefault("general.mapViewDistanceH", 5);

        getConfig().addDefault("general.disableProtectionWhenFirstMemberGetsOnline", true);
        getConfig().addDefault("general.protectionEnableTime", "900");

        getConfig().addDefault("general.commandDescriptions", true);
        getConfig().addDefault("general.connectedChunks", false);
        getConfig().addDefault("general.allowClaimProtection", true);
        getConfig().addDefault("general.debugMode", false);
        getConfig().addDefault("general.allowNegativeBalance", false);
        getConfig().addDefault("general.messageType", "TITLE"); //TITLE, SUBTITLE, ACTIONBAR, ITEM
        getConfig().addDefault("general.wildnessText", "&2Wildness");
        getConfig().addDefault("general.safezoneText", "&bSafezone");
        getConfig().addDefault("general.worlds", new String[]{
                "world", "world_nether", "world_the_end"
        });

        getConfig().addDefault("performance.threadingMethode", "SYNC"); // SYNC, THREADS, FUTURE_TASK, COMPLETABLE_FUTURE
        getConfig().addDefault("performance.stopwatch", false);

        getConfig().options().copyDefaults(true);
        saveConfig();

        //Language defaults
        languageData.getConfig().addDefault("prefix", "&c&lFactions:");

        languageData.getConfig().options().copyDefaults(true);
        languageData.saveConfig();

        //Add commands
        getServer().getPluginCommand("f").setExecutor(new FactionCommand());
        getServer().getPluginCommand("f").setTabCompleter(new FactionCommandTab());

        getServer().getPluginCommand("fdel").setExecutor(new FDelCommand());
        getServer().getPluginCommand("fdel").setTabCompleter(new FDelCommand());

        getServer().getPluginCommand("fdelP").setExecutor(new FDelPCommand());
        getServer().getPluginCommand("fdelP").setTabCompleter(new FDelPCommand());

        getServer().getPluginCommand("fjoin").setExecutor(new FJoin());
        getServer().getPluginCommand("fjoin").setTabCompleter(new FJoin());

        //Add listeners
        getServer().getPluginManager().registerEvents(new OnWorldSaveListener(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerMove(), this);
        getServer().getPluginManager().registerEvents(new OnChunkEntered(), this);

        getServer().getPluginManager().registerEvents(new OnBlockBreak(), this);
        getServer().getPluginManager().registerEvents(new OnBlockPlace(), this);
        getServer().getPluginManager().registerEvents(new OnInteract(), this);
        getServer().getPluginManager().registerEvents(new OnEntityInteract(), this);
        getServer().getPluginManager().registerEvents(new OnEntityDamage(), this);
        getServer().getPluginManager().registerEvents(new ArrowHitListener(), this);

        getServer().getPluginManager().registerEvents(new OnJoin(), this);
        getServer().getPluginManager().registerEvents(new OnLeave(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerDeathListener(), this);
        getServer().getPluginManager().registerEvents(new BlockExplosionListener(), this);


        getServer().getPluginManager().registerEvents(guiListener, this);

        ClickActions.init(this);

        if (Bukkit.getPluginManager().getPlugin("ProtocolLib") == null) {
            System.out.println("§cCan't load improved factions. Need to install protocolLib");
            Bukkit.getPluginManager().disablePlugin(INSTANCE);
            return;
        }

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            getLogger().info("Found PlaceholderAPI");
            FactionExpansion.init();
            new FactionExpansion().register();
        } else {
            getLogger().info("Found PlaceholderAPI");
        }

        Bukkit.getScheduler().runTaskLater(this, () -> {
            protocolManager = ProtocolLibrary.getProtocolManager();
            signMenuFactory = new SignMenuFactory(this);
            getLogger().info("IF enabled correctly");
        }, 1);

        //Load extentions
        try {
            ExtensionListLoader.RegenerateExtensionList();
        } catch (IOException e) {
            e.printStackTrace();
        }

        boolean exit = false;

        if (extensionFile.exists()) {
            //try {
            //ExtensionLoader<Extension> loader = new ExtensionLoader<Extension>();
            for (File jar : extensionFile.listFiles()) {
                if (jar.getName().endsWith(".jar")) {
                    JarClassLoader jcl = new JarClassLoader();
                    JclObjectFactory factory = JclObjectFactory.getInstance();
                    jcl.add(jar.getPath());
                    //Extension extension = loader.LoadClass(jar, "extension.Main", Extension.class);
                    Extension extension = (Extension) factory.create(jcl, "extension." + jar.getName().split("\\.")[0].toLowerCase() + ".Main");
                    if (!extension.preLoad(this)) {
                        getServer().getConsoleSender().sendMessage("§7[Factions] §cExtension §6" + extension.getRegistry().getName() + "§c disabled the loading. Remove it if this shouldn't happened");
                        exit = true;
                        return;
                    }
                    extensions.put(extension.getRegistry().getName(), new ExtensionContainer(extension, jcl));
                }
            }

            if (exit) return;
            //} catch (ClassNotFoundException e) {
            //  getServer().getConsoleSender().sendMessage("§7[Factions] §cDidn't find any extensions to be loaded");
            //}
        }

        ChunkUtils.Init();

        //Load language files
        File langFile = new File(getDataFolder().getPath() + "/lang");
        langFile.mkdir();

        try {
            File en_us = new File(langFile.getPath() + "/en_us.lang");
            if (en_us.createNewFile()) {
                LangMessage defaultMessages = new LangMessage();

                ObjectMapper mapper = new ObjectMapper();
                mapper.writerWithDefaultPrettyPrinter().writeValue(en_us, defaultMessages);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        Language.init(this, langFile);

        Bukkit.getScheduler().runTaskLater(this, () -> {
            if (!setupEconomy()) {
                getLogger().warning(Language.format("Disabled faction economy! Needs Vault and an Economy plugin installed to enable it"));
            } else {
                getLogger().info(Language.format("Enabled faction economy"));
            }
        }, 0);

        //Others
        FactionSettings.Init();
        Faction.LoadFactions(this);


        try {
            if (getConfig().getBoolean("general.updateChecker")) {
                updateChecker = new UpdateChecker(VERSION, new URL("https://raw.githubusercontent.com/ToberoCat/ImprovedFaction/master/version.json"));
                if (!updateChecker.isNewestVersion()) {
                    getConsoleSender().sendMessage(Language.getPrefix() +
                            "§fA newer version of this plugin is available. Check it out: https://www.spigotmc.org/resources/improved-factions.95617/");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (Player on : Bukkit.getOnlinePlayers()) {
            AddPlayerData(on);
        }

        int loaded = 0;
        for (ExtensionContainer container : extensions.values()) {
            container.getExtension().Enable(this);
            if (container.getExtension().isEnabled())
                loaded++;
        }

        if (loaded == 0) {
            getServer().getConsoleSender().sendMessage("§7[Factions] §cDidn't find any extensions to be loaded");
        } else {
            getServer().getConsoleSender().sendMessage("§7[Factions] §aSuccessfully loaded " + loaded +
                    (loaded == 1 ? " extension" : " extensions"));
        }

        File reports = new File(getDataFolder().getPath() + "/Data/reports.json");
        if (!reports.exists()) {
            try {
                reports.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        File warns = new File(getDataFolder().getPath() + "/Data/warns.json");
        if (!warns.exists()) {
            try {
                warns.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        REPORTS = new ArrayList<>();
        WARNS = new Warn();

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            REPORTS = Arrays.asList(objectMapper.readValue(reports, Report[].class));
        } catch (IOException e) {
            getServer().getConsoleSender().sendMessage(
                    "§7[Factions] §6Reports couldn't get loaded. File is probably empty");
            REPORTS = new ArrayList<>();
        }

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            WARNS = objectMapper.readValue(warns, Warn.class);
        } catch (IOException e) {
            getServer().getConsoleSender().sendMessage(
                    "§7[Factions] §6Warns couldn't get loaded. File is probably empty");
            WARNS = new Warn();
        }

        Metrics metrics = new Metrics(this, 14810);
    }
    @Override
    public void onDisable() {
        Faction.SaveFactions(this);
        ChunkUtils.Save();
        Bar.Disable();
        playerMessages.SavePlayerMessages();

        for (String key : extensions.keySet()) {
            ExtensionContainer container = extensions.get(key);
            container.getExtension().Disable(this);
        }

        File reports = new File(getDataFolder().getPath() + "/Data/reports.json");
        JsonUtility.SaveObject(reports, REPORTS.toArray(new Report[0]));

        File warns = new File(getDataFolder().getPath() + "/Data/warns.json");
        JsonUtility.SaveObject(warns, WARNS);
    }

    public DataManager getFactionData() {
        if (factionData == null) {
            Bukkit.getLogger().log(Level.WARNING, "Instance of factionData is null");
        }
        return factionData;
    }

    public DataManager getLanguageData() {
        if (languageData == null) {
            Bukkit.getLogger().log(Level.WARNING, "Instance of languageData is null");
        }
        return languageData;
    }

    public DataManager getMessagesData() {
        if (messagesData == null) {
            Bukkit.getLogger().log(Level.WARNING, "Instance of messageData is null");
        }
        return messagesData;
    }

    public DataManager getExtConfigData() {
        if (extConfigData == null) {
            Bukkit.getLogger().log(Level.WARNING, "Instance of extConfigDaat is null");
        }
        return extConfigData;
    }

    public static ImprovedFactionsMain getPlugin() {
        if (INSTANCE == null) {
            Bukkit.getLogger().log(Level.WARNING, "Instance of ImprovedFactionMain is null. Please wait until onEnable completed before calling");
        }
        return INSTANCE;
    }


    public static void AddPlayerData(Player player) {
        PersistentDataContainer container = player.getLocation().getChunk().getPersistentDataContainer();

        PlayerData data = new PlayerData();

        Debugger.LogInfo(player.getLocale());

        data.playerFaction = GetFaction(player);

        data.chunkData.isInClaimedChunk = container.has(ChunkUtils.FACTION_CLAIMED_KEY, PersistentDataType.STRING);
        if (data.chunkData.isInClaimedChunk)
            data.chunkData.factionRegistry = container.get(ChunkUtils.FACTION_CLAIMED_KEY, PersistentDataType.STRING);

        playerData.put(player.getUniqueId(), data);
    }

    private static Faction GetFaction(Player player) {
        for (Faction faction : Faction.getFACTIONS()) {
            for (FactionMember factionMember : faction.getMembers()) {
                if (factionMember != null && factionMember.getUuid().equals(player.getUniqueId())) {
                    return faction;
                }
            }
        }
        return null;
    }

    public void ReloadConfigs() {
        reloadConfig();
        languageData.reloadConfig();
        messagesData.reloadConfig();
        extConfigData.reloadConfig();
        factionData.reloadConfig();
    }

    public SignMenuFactory getSignMenuFactory() {
        return signMenuFactory;
    }

    public PlayerMessages getPlayerMessages() {
        return playerMessages;
    }

    public static String getVERSION() {
        return VERSION;
    }

    public static ConsoleCommandSender getConsoleSender() {
        return getPlugin().getServer().getConsoleSender();
    }

    public DataManager getCommandData() {
        return commandData;
    }

    public static void RemoveExtension(String extension) {
        ExtensionContainer container = extensions.get(extension);
        container.getExtension().Disable(getPlugin());
        extensions.remove(extension);
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager()
                .getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }

    public DataManager getChunkData() {
        return chunkData;
    }

    public Economy getEconomy() {
        return economy;
    }

    public ProtocolManager getProtocolManager() {
        return protocolManager;
    }

    public void setProtocolManager(ProtocolManager protocolManager) {
        this.protocolManager = protocolManager;
    }
}
