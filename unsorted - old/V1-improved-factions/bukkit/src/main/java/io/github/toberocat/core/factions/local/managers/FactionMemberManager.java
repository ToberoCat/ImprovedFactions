package io.github.toberocat.core.factions.local.managers;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.FactionManager;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.listeners.player.PlayerJoinListener;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.data.PersistentDataUtility;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.messages.PlayerMessageBuilder;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

/**
 * This is for managing the members of a specific faction
 */
public class FactionMemberManager {


    private ArrayList<UUID> members;
    private ArrayList<UUID> banned;

    private Faction<?> faction;

    /**
     * Don't use this. It is for jackson (json).
     */
    public FactionMemberManager() {
    }

    public FactionMemberManager(Faction<?> faction) {
        this.members = new ArrayList<>();
        this.banned = new ArrayList<>();
        this.faction = faction;
    }

    /**
     * This event should get called by the @see {@link PlayerJoinListener#Join(PlayerJoinEvent)}
     * and shouldn't get called outside of it
     */
    public static void PlayerJoin(Player player) {
        updatePlayer(player);
    }

    private static void updatePlayer(Player player) {
        if (player == null) return;

        String registry = PersistentDataUtility.read(PersistentDataUtility.PLAYER_FACTION_REGISTRY,
                PersistentDataType.STRING, player.getPersistentDataContainer());
        if (registry == null) return;

        Faction<?> faction = null;
        try {
            faction = FactionHandler.getFaction(registry);
        } catch (FactionNotInStorage e) {
            e.printStackTrace();
        }
        if (faction != null) return;

        PersistentDataUtility.remove(PersistentDataUtility.PLAYER_FACTION_REGISTRY, player.getPersistentDataContainer());

    }


    /**
     * Let a (online) player join a faction if not in any other
     * If the result is not a success, you can get the error message by .getMessage()
     *
     * @param player The player that should join the faction
     * <p>
     * Potential Errors:
     * PLAYER_OFFLINE, PLAYER_IN_FACTION, PLAYER_BANNED, PLAYER_TIMEOUT
     */
    public Result<?> join(Player player) {
        if (!player.isOnline()) return new Result<>(false).setMessages("PLAYER_OFFLINE",
                "It looks like " + player.getName() + " are offline");

        if (FactionHandler.isInFaction(player)) return new Result<>(false).setMessages(
                "PLAYER_IN_FACTION", player.getName() + " is already in a faction");

        if (banned.contains(player.getUniqueId())) return new Result<>(false).setMessages(
                "PLAYER_BANNED", "Looks like " + player.getName()
                        + " is banned from this faction");

        PersistentDataUtility.write(
                PersistentDataUtility.PLAYER_FACTION_REGISTRY,
                PersistentDataType.STRING, faction.getRegistry(),
                player.getPersistentDataContainer());

        members.add(player.getUniqueId());

        return new Result<>(true);
    }

    /**
     * Let a (online) player leave the faction.
     * <p>
     * If the result is not a success, you can get the error message by .getMessage()
     *
     * @param player The player that should leave
     * Potential Errors: PLAYER_OFFLINE, PLAYER_NOT_IN_FACTION
     */
    public Result<?> leave(Player player) {
        if (!player.isOnline()) return new Result<>(false).setMessages("PLAYER_OFFLINE",
                "It looks like " + player.getName() + " are offline");

        if (!FactionHandler.isInFaction(player)) return new Result<>(false).setMessages(
                "PLAYER_NOT_IN_FACTION", player.getName()
                        + " is in no faction. So nothing can be left");

        PersistentDataUtility.remove(PersistentDataUtility.PLAYER_FACTION_REGISTRY,
                player.getPersistentDataContainer());

        updatePlayer(player);

        members.remove(player.getUniqueId());
        return new Result<>(true);
    }

    /**
     * Kick a player froma faction.
     * When you kick someone, he/she doesn't need to be online
     *
     * @param player The uuid of the player you want to kick
     */
    public Result<?> kick(@NotNull OfflinePlayer player) {
        members.remove(player.getUniqueId());
        if (!player.isOnline()) return new Result<>(true);
        Player onP = player.getPlayer();
        if (onP == null) return new Result<>(false);

        PersistentDataUtility.remove(PersistentDataUtility.PLAYER_FACTION_REGISTRY,
                onP.getPersistentDataContainer());
        return new Result<>(true);
    }

    /**
     * This will kick the player & won't let him join this faction again
     * You can ban everyone. The player doesn't need to be online
     *
     * @param player The uuid of the player you want to ban permanently
     */
    public void ban(OfflinePlayer player) {
        banned.add(player.getUniqueId());
        kick(player);
    }

    /**
     * This "forgives" a player who has been banned using the function ban
     *
     * @see #ban(OfflinePlayer)
     */
    public void pardon(OfflinePlayer player) {
        banned.remove(player.getUniqueId());
    }

    public ArrayList<UUID> getMembers() {
        return members;
    }

    public ArrayList<UUID> getBanned() {
        return banned;
    }
}
