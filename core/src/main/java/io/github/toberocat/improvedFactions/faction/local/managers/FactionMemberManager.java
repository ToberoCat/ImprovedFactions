package io.github.toberocat.improvedFactions.faction.local.managers;

import io.github.toberocat.improvedFactions.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.exceptions.faction.PlayerIsAlreadyInFactionException;
import io.github.toberocat.improvedFactions.exceptions.faction.PlayerIsBannedException;
import io.github.toberocat.improvedFactions.faction.Faction;
import io.github.toberocat.improvedFactions.player.OfflineFactionPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.UUID;

/**
 * This is for managing the members of a specific faction
 */

// Todo: Rework this class - It's ugly and kinda useless
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
     * Let a (online) player join a faction if not in any other
     * If the result is not a success, you can get the error message by .getMessage()
     *
     * @param player The player that should join the faction
     *               <p>
     *               Potential Errors:
     *               PLAYER_OFFLINE, PLAYER_IN_FACTION, PLAYER_BANNED, PLAYER_TIMEOUT
     */
    public void join(OfflineFactionPlayer<?> player)
            throws PlayerIsAlreadyInFactionException, PlayerIsBannedException {
        if (player.inFaction()) throw new PlayerIsAlreadyInFactionException(faction, player);
        if (banned.contains(player.getUniqueId())) throw new PlayerIsBannedException(faction, player);

        // ToDo: Write persistent data into a .temp file, allowing offline player data to get saved
        player.getDataContainer()
                .set(PersistentDataContainer.FACTION_KEY, faction.getRegistry());

        members.add(player.getUniqueId());
    }

    /**
     * Let a (online) player leave the faction.
     * <p>
     * If the result is not a success, you can get the error message by .getMessage()
     *
     * @param player The player that should leave
     *               Potential Errors: PLAYER_OFFLINE, PLAYER_NOT_IN_FACTION
     */
    public void leave(OfflineFactionPlayer<?> player) throws PlayerHasNoFactionException {
        if (!player.inFaction()) throw new PlayerHasNoFactionException(player);
        player.getDataContainer().remove(PersistentDataContainer.FACTION_KEY);

        members.remove(player.getUniqueId());
    }

    /**
     * Kick a player froma faction.
     * When you kick someone, he/she doesn't need to be online
     *
     * @param player The uuid of the player you want to kick
     */
    public void kick(@NotNull OfflineFactionPlayer<?> player) {
        members.remove(player.getUniqueId());

        player.getDataContainer().remove(PersistentDataContainer.FACTION_KEY);
    }

    /**
     * This will kick the player & won't let him join this faction again
     * You can ban everyone. The player doesn't need to be online
     *
     * @param player The uuid of the player you want to ban permanently
     */
    public void ban(OfflineFactionPlayer<?> player) {
        banned.add(player.getUniqueId());
        kick(player);
    }

    /**
     * This "forgives" a player who has been banned using the function ban
     */
    public void pardon(OfflineFactionPlayer<?> player) {
        banned.remove(player.getUniqueId());
    }

    public ArrayList<UUID> getMembers() {
        return members;
    }

    public ArrayList<UUID> getBanned() {
        return banned;
    }
}
