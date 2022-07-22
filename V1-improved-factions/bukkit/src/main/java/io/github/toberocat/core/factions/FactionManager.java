package io.github.toberocat.core.factions;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.debug.Debugger;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.async.AsyncTask;
import io.github.toberocat.core.utility.claim.ClaimManager;
import io.github.toberocat.core.utility.data.PersistentDataUtility;
import io.github.toberocat.core.utility.data.access.FileAccess;
import io.github.toberocat.core.utility.events.faction.FactionLoadEvent;
import io.github.toberocat.core.utility.events.faction.member.FactionMemberOfflineEvent;
import io.github.toberocat.core.utility.events.faction.member.FactionMemberOnlineEvent;
import io.github.toberocat.core.utility.exceptions.faction.FactionHandlerNotFound;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotFoundException;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.logging.Level;
import java.util.stream.Stream;

/**
 * This class is responsible for handling the faction loading & unloading for RAM savage
 */
public class FactionManager implements Listener {

    /**
     * This will unload a faction if it is not in use anymore
     */
    public static void unload(String registry) {
        Faction<?> faction = getFactionByRegistry(registry);
        if (faction == null) return;

        int online = 0;
        for (UUID uuid : faction.getFactionMemberManager().getMembers()) {
            if (Bukkit.getOfflinePlayer(uuid).isOnline()) online++;
            if (online >= 2) return;
        }

        FileAccess.write("Factions", faction.getRegistryName(), faction);
        Faction.getLoadedFactions().remove(faction.getRegistryName());
        return;
    }

    /**
     * Get if a (online) player is in a faction.
     * This will only check the Persisten data of this player for performance
     * If you want to iterate throw all factions, you must write is yourself
     *
     * @param player The player you want to check
     * @return A boolean, if the player is in a faction
     */
    public static boolean isInFaction(@NotNull OfflinePlayer player) {
        if (player.isOnline()) {
            Player on = player.getPlayer();
            String faction = PersistentDataUtility.read(PersistentDataUtility.PLAYER_FACTION_REGISTRY,
                    PersistentDataType.STRING, on.getPersistentDataContainer();
            return faction != null;
        } else {

        }

    }

    public static @Nullable String getPlayerFactionRegistry(@NotNull OfflinePlayer player) {
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

    public static Color getRegistryColor(@NotNull String registry) throws FactionNotFoundException {
        if (ClaimManager.isManageableZone(registry)) return Color.fromRGB(ClaimManager.getRegistryColor(registry));
        Faction faction = getFactionByRegistry(registry);
        if (faction == null) throw new FactionNotFoundException(registry);
        return Color.fromRGB(faction.getColor());
    }

    /**
     * This will return the faction you were searching for. Will also load the faction if not loaded
     */
    public static @NotNull Faction getFactionByRegistry(@NotNull String registry) throws FactionNotInStorage, FactionHandlerNotFound {
        if (FactionHandler.getLoadedFactions().containsKey(registry)) return Faction.getLoadedFactions().get(registry);

        Faction faction = FactionHandler.loadFromStorage(registry);
        Debugger.log("Loaded &e" + faction.getRegistry());
        return faction;
    }

    /**
     * Get if the plugin finds a file with the registry name
     * Use to check if a faction exists, but isn't loaded
     *
     * @param registry The registry name the faction should be known of
     * @return True if a file with the registryName exists in ImprovedFactions/Data/Factions
     */
    public static boolean doesFactionExist(String registry) {
        return Arrays.asList(FileAccess.listRawFolder("Factions")).contains(registry + ".json");
    }

    public static List<String> getAllFactions() {
        return Arrays.stream(FileAccess.listRawFolder("Factions")).map(registry -> registry.split("\\.")[0]).toList();
    }

    public static Stream<String> getAllFactionsStream() {
        return Arrays.stream(FileAccess.listRawFolder("Factions")).map(x -> x.split("\\.")[0]);
    }

    public static boolean isFactionLoaded(String registryName) {
        return Faction.getLoadedFactions().containsKey(registryName);
    }

    public static String factionDisplayToRegistry(String displayName) {
        return ChatColor.stripColor(displayName.replaceAll("[^a-zA-Z0-9]", " "));
    }

    @EventHandler
    private void join(@NotNull PlayerJoinEvent event) {
        getPlayerFaction(event.getPlayer());
    }

    @EventHandler
    private void leave(@NotNull PlayerQuitEvent event) {
        unloadPlayer(event.getPlayer());
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
        Faction<?> faction = getPlayerFaction(player);
        if (faction == null) return; // Player wasn't in a faction
        unload(faction.getRegistryName()).then((unloaded) -> {
            if (unloaded) return;

            AsyncTask.runSync(() -> Bukkit.getPluginManager()
                    .callEvent(new FactionMemberOfflineEvent(faction, player)));
        });
    }
}
