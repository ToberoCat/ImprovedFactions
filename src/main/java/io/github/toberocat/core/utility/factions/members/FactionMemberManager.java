package io.github.toberocat.core.utility.factions.members;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.toberocat.MainIF;
import io.github.toberocat.core.listeners.PlayerJoinListener;
import io.github.toberocat.core.utility.factions.FactionUtility;
import io.github.toberocat.core.utility.Result;
import io.github.toberocat.core.utility.async.AsyncCore;
import io.github.toberocat.core.utility.data.PersistentDataUtility;
import io.github.toberocat.core.utility.factions.Faction;
import io.github.toberocat.core.utility.language.LangMessage;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.UUID;

/**
 * This is for managing the members of a specific faction
 */
public class FactionMemberManager {

    private static final String PLAYER_NO_FACTION = "NONE";
    public static final String NONE_TIMEOUT = "NONE";


    private ArrayList<UUID> members;
    private ArrayList<UUID> banned;

    private Faction faction;

    /**
     * Don't use this. It is for jackson (json).
     */
    public FactionMemberManager() {}

    public FactionMemberManager(Faction faction) {
        this.members = new ArrayList<>();
        this.banned = new ArrayList<>();
        this.faction = faction;
    }

    /**
     * This event should get called by the @see {@link PlayerJoinListener#Join(PlayerJoinEvent)}
     * and shouldn't get called outside of it
     * @param event The event from PlayerJoin
     */
    public static void PlayerJoin(PlayerJoinEvent event) {
        AsyncCore.Run(() -> {
            Player player = event.getPlayer();
            Faction faction = FactionUtility.getPlayerFaction(player);

            if (faction == null) return new Result(false);
            if (faction.getFactionMemberManager().members.contains(player.getUniqueId()))
                return new Result(false);

            PersistentDataUtility.write(PersistentDataUtility.PLAYER_FACTION_REGISTRY,
                    PersistentDataType.STRING, PLAYER_NO_FACTION,
                    player.getPersistentDataContainer());
            Language.sendMessage(LangMessage.FACTION_KICKED, player);
            return new Result(true);
        });
    }

    /**
     * Let a (online) player join a faction if not in any other
     * If the result is not a success, you can get the error message by .getMessage() @see {@link Result#getMachineMessage()} ()}
     *
     * Potential Errors:
     * PLAYER_OFFLINE, PLAYER_IN_FACTION, PLAYER_BANNED, PLAYER_TIMEOUT
     *
     * @param player The player that should join the faction
     */
    public Result join(Player player) {
        if (!player.isOnline()) return new Result(false).setMessages("PLAYER_OFFLINE",
                "It looks like "+player.getName()+" are offline");

        if (FactionUtility.isInFaction(player)) return new Result(false).setMessages(
                "PLAYER_IN_FACTION", player.getName() + " is already in a faction");

        if (banned.contains(player.getUniqueId())) return new Result(false).setMessages(
                "PLAYER_BANNED", "Looks like " + player.getName() + " is banned from this faction");

        PersistentDataUtility.write(
                PersistentDataUtility.PLAYER_FACTION_REGISTRY,
                PersistentDataType.STRING, faction.getRegistryName(),
                player.getPersistentDataContainer());

        members.add(player.getUniqueId());
        faction.getPowerManager()
                .IncreaseMax(MainIF.getConfigManager().getValue("power.maxPowerPerPlayer"));

        Language.sendMessage(LangMessage.COMMAND_FACTION_JOIN_SUCCESS, player);
        System.out.println(Arrays.toString(members.toArray(UUID[]::new)));
        return new Result(true);
    }

    /**
     * Let a (online) player leave the faction.
     * If the player could / is offline, please use @see {@link #kick(UUID)}
     *
     * If the result is not a success, you can get the error message by .getMessage() @see {@link Result#getMachineMessage()} ()}
     * Potential Errors: PLAYER_OFFLINE, PLAYER_NOT_IN_FACTION
     *
     * @param player The player that should leave
     */
    public Result leave(Player player) {
        if (!player.isOnline()) return new Result(false).setMessages("PLAYER_OFFLINE", "It looks like "+player.getName()+" are offline");

        if (!FactionUtility.isInFaction(player)) return new Result(false).setMessages(
                "PLAYER_NOT_IN_FACTION", player.getName() + " is in no faction. So nothing can be left");

        PersistentDataUtility.remove(
                PersistentDataUtility.PLAYER_FACTION_REGISTRY, player.getPersistentDataContainer());

        members.remove(player.getUniqueId());
        faction.getPowerManager()
                .IncreaseMax(MainIF.getConfigManager().getValue("power.maxPowerPerPlayer"));
        return new Result(true);
    }

    /**
     * Kick a player froma faction.
     * When you kick someone, he/she doesn't need to be online
     * @param player The uuid of the player you want to kick
     */
    public Result kick(UUID player) {
        members.remove(player);
        return new Result(true);
    }

    /**
     * This will kick the player & won't let him join this faction again
     * You can ban everyone. The player doesn't need to be online
     * @param player The uuid of the player you want to ban permanently
     */
    public Result ban(UUID player) {
        banned.add(player);
        kick(player);
        return new Result(true);
    }

    /**
     * This "forgives" a player who has been banned using the function ban
     * @see #ban(UUID)
     */
    public Result pardon(UUID player) {
        banned.remove(player);
        return new Result(true);
    }

    public ArrayList<UUID> getMembers() {
        return members;
    }

    public ArrayList<UUID> getBanned() {
        return banned;
    }

    public FactionMemberManager setMembers(ArrayList<UUID> members) {
        this.members = members;
        return this;
    }

    public FactionMemberManager setBanned(ArrayList<UUID> banned) {
        this.banned = banned;
        return this;
    }

    @JsonIgnore
    public FactionMemberManager setFaction(Faction faction) {
        this.faction = faction;
        return this;
    }

    @JsonIgnore
    public Faction getFaction() {
        return faction;
    }
}
