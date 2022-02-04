package io.github.toberocat.core.utility.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.utility.async.AsyncCore;
import io.github.toberocat.core.utility.data.DataAccess;
import io.github.toberocat.core.utility.data.PersistentDataUtility;
import io.github.toberocat.core.utility.dynamic.loaders.PlayerJoinLoader;
import io.github.toberocat.core.utility.factions.members.FactionMemberManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataType;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;

/**
 * This class is responsible for handling the faction loading & unloading for RAM savage
 */
public class FactionUtility extends PlayerJoinLoader {

    public FactionUtility() {
        Subscribe(this);
    }

    @Override
    protected void loadPlayer(Player player) {
        MainIF.LogMessage(Level.INFO, "Loading player");
        String registry = getPlayerFactionRegistry(player);
        if (registry == null) return; // Player not in faction
        if (Faction.getLoadedFactions().containsKey(registry)) return; // Faction already loaded
        if (!doesFactionExist(registry)) return; // Faction got deleted

        getFactionByRegistry(registry);
    }

    @Override
    protected void unloadPlayer(Player player) {
        Faction faction = getPlayerFaction(player);
        if (faction == null) return; // Player wasn't in a faction
        unload(faction.getRegistryName());
    }

    @Override
    protected void Disable() {
        for (Faction faction : Faction.getLoadedFactions().values()) {
            DataAccess.addFile("Factions", faction.getRegistryName(), faction);
            Faction.getLoadedFactions().remove(faction.getRegistryName());
        }

        Faction.getLoadedFactions().clear();
    }

    /**
     * This will unload a faction if it is not in use anymore
     */
    public static void unload(String registry) {
        AsyncCore.Run(() -> {
            Faction faction = getFactionByRegistry(registry);
            if (faction == null) return;

            for (UUID uuid : faction.getFactionMemberManager().getMembers()) {
                if (Bukkit.getOfflinePlayer(uuid).isOnline()) return;
            }

            DataAccess.addFile("Factions", faction.getRegistryName(), faction);
            Faction.getLoadedFactions().remove(faction.getRegistryName());
        });
    }

    /**
     * Get if a (online) player is in a faction.
     * This will only check the Persisten data of this player for performance
     * If you want to iterate throw all factions, you must write is yourself
     * @param player The player you want to check
     * @return A boolean, if the player is in a faction
     */
    public static boolean isInFaction(Player player) {
        String faction = PersistentDataUtility.read(PersistentDataUtility.PLAYER_FACTION_REGISTRY, PersistentDataType.STRING, player.getPersistentDataContainer());
        System.out.println(faction);
        return faction != null;
    }

    public static String getPlayerFactionRegistry(Player player) {
        if (!isInFaction(player)) return null;
        return PersistentDataUtility.read(PersistentDataUtility.PLAYER_FACTION_REGISTRY, PersistentDataType.STRING,
                        player.getPersistentDataContainer());
    }

    /**
     * Get the player faction a (online) player is in
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

        MainIF.LogMessage(Level.INFO, "Loaded &e" + faction.getRegistryName());
        Faction.getLoadedFactions().put(registry, faction);
        return faction;
    }

    public static List<String> getAllFactions() {
        return Arrays.stream(DataAccess.listRawFiles("Factions")).map(registry -> registry.split("\\.")[0]).toList();
    }

    /**
     * Get if the plugin finds a file with the registry name
     * Use to check if a faction exists, but isn't loaded
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
        return  ChatColor.stripColor(displayName.replaceAll("[^a-zA-Z0-9]", " "));
    }
}
