package io.github.toberocat.core.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.members.FactionMemberManager;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.data.DataAccess;
import io.github.toberocat.core.utility.data.PersistentDataUtility;
import io.github.toberocat.core.utility.dynamic.loaders.PlayerJoinLoader;
import io.github.toberocat.core.utility.events.faction.FactionLoadEvent;
import io.github.toberocat.core.utility.events.faction.FactionMemberOfflineEvent;
import io.github.toberocat.core.utility.events.faction.FactionMemberOnlineEvent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.*;
import java.util.logging.Level;

/**
 * This class is responsible for handling the faction loading & unloading for RAM savage
 */
public class FactionUtility extends PlayerJoinLoader {

    public FactionUtility() {
        Subscribe(this);
    }

    /**
     * This will unload a faction if it is not in use anymore
     */
    public static AsyncTask<Boolean> unload(String registry) {
        return AsyncTask.run(() -> {
            Faction faction = getFactionByRegistry(registry);
            if (faction == null) return false;

            int online = 0;
            for (UUID uuid : faction.getFactionMemberManager().getMembers()) {
                if (Bukkit.getOfflinePlayer(uuid).isOnline()) online++;
                if (online >= 2) return false;
            }

            DataAccess.addFile("Factions", faction.getRegistryName(), faction);
            Faction.getLoadedFactions().remove(faction.getRegistryName());
            return true;
        });
    }

    /**
     * Get if a (online) player is in a faction.
     * This will only check the Persisten data of this player for performance
     * If you want to iterate throw all factions, you must write is yourself
     *
     * @param player The player you want to check
     * @return A boolean, if the player is in a faction
     */
    public static boolean isInFaction(Player player) {
        if (player == null || !player.isOnline()) return false;

        String faction = PersistentDataUtility.read(PersistentDataUtility.PLAYER_FACTION_REGISTRY,
                PersistentDataType.STRING, player.getPersistentDataContainer());
        return faction != null;
    }

    public static String getPlayerFactionRegistry(Player player) {
        return PersistentDataUtility.read(PersistentDataUtility.PLAYER_FACTION_REGISTRY, PersistentDataType.STRING,
                player.getPersistentDataContainer());
    }

    /**
     * Get the player faction a (online) player is in
     *
     * @param player The player you want to get the faction from
     * @return The faction the player is in
     */
    public static Faction getPlayerFaction(Player player) {
        if (!isInFaction(player)) return null;

        return getFactionByRegistry(PersistentDataUtility
                .read(PersistentDataUtility.PLAYER_FACTION_REGISTRY, PersistentDataType.STRING,
                        player.getPersistentDataContainer()));
    }

    /**
     * This will return the faction you were searching for. Will also load the faction if not loaded
     */
    public static Faction getFactionByRegistry(String registry) {
        if (registry == null) return null;

        if (Faction.getLoadedFactions().containsKey(registry)) return Faction.getLoadedFactions().get(registry);
        if (!doesFactionExist(registry)) return null;

        //Load faction
        Faction faction = DataAccess.getFile("Factions", registry, Faction.class);
        if (faction == null) return null;

        faction.getFactionMemberManager().setFaction(faction);
        faction.getPowerManager().setFaction(faction);
        faction.getRelationManager().setFaction(faction);
        faction.getFactionPerm().setFaction(faction);

        for (Map.Entry<String, FactionModule> module :
                new LinkedHashSet<>(faction.getModules().entrySet())) {
            if (module.getValue() == null) faction.getModules().remove(module.getKey());
        }
        for (FactionModule module : faction.getModules().values()) if (module != null) module.setFaction(faction);

        MainIF.logMessage(Level.INFO, "Loaded &e" + faction.getRegistryName());
        Faction.getLoadedFactions().put(registry, faction);
        return faction;
    }

    public static List<String> getAllFactions() {
        return Arrays.stream(DataAccess.listRawFiles("Factions")).map(registry -> registry.split("\\.")[0]).toList();
    }

    /**
     * Get if the plugin finds a file with the registry name
     * Use to check if a faction exists, but isn't loaded
     *
     * @param registry The registry name the faction should be known of
     * @return True if a file with the registryName exists in ImprovedFactions/Data/Factions
     */
    public static boolean doesFactionExist(String registry) {
        return Arrays.asList(DataAccess.listRawFiles("Factions")).contains(registry + ".json");
    }

    public static boolean isFactionLoaded(String registryName) {
        return Faction.getLoadedFactions().containsKey(registryName);
    }

    public static String factionDisplayToRegistry(String displayName) {
        return ChatColor.stripColor(displayName.replaceAll("[^a-zA-Z0-9]", " "));
    }

    @Override
    protected void loadPlayer(Player player) {
        AsyncTask.runLaterSync(0, () -> {
            String registry = getPlayerFactionRegistry(player);
            if (registry == null) return; // Player not in faction
            if (Faction.getLoadedFactions().containsKey(registry)) {
                Bukkit.getPluginManager().callEvent(new FactionMemberOnlineEvent(Faction
                        .getLoadedFactions().get(registry), player));
                return; // Faction already loaded
            }
            if (!doesFactionExist(registry)) { // Faction got deleted
                PersistentDataUtility.remove(PersistentDataUtility.PLAYER_FACTION_REGISTRY,
                        player.getPersistentDataContainer());
                return;
            }

            Faction faction = getFactionByRegistry(registry);
            if (faction == null) {
                MainIF.logMessage(Level.SEVERE, "Couldn't load faction " + registry + ". This faction should have get loaded, but had problems while doing so");
                return;
            }

            Bukkit.getPluginManager().callEvent(new FactionLoadEvent(faction));
        });
    }

    @Override
    protected void unloadPlayer(Player player) {
        Faction faction = getPlayerFaction(player);
        if (faction == null) return; // Player wasn't in a faction
        unload(faction.getRegistryName()).then((unloaded) -> {
            if (unloaded) return;

            AsyncTask.runSync(() -> Bukkit.getPluginManager()
                    .callEvent(new FactionMemberOfflineEvent(faction, player)));
        });
    }


    @Override
    protected void Disable() {
        for (Faction faction : Faction.getLoadedFactions().values()) {
            DataAccess.addFile("Factions", faction.getRegistryName(), faction);
        }

        Faction.getLoadedFactions().clear();
    }
}
