package io.github.toberocat.core.invite;

import io.github.toberocat.MainIF;
import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.components.rank.members.FactionRank;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public abstract class Invite {

    private final UUID inviteId;
    private final UUID receiver;
    private final Faction faction;
    private final FactionRank joinWithRank;

    public Invite(@NotNull UUID inviteId,
                  @NotNull UUID receiver,
                  @NotNull Faction faction,
                  @NotNull FactionRank joinWithRank,
                  long duration) {
        this.inviteId = inviteId;
        this.receiver = receiver;
        this.faction = faction;
        this.joinWithRank = joinWithRank;

        Bukkit.getScheduler().runTaskLater(MainIF.getIF(), this::cancelInvite, duration);
    }

    public void cancelInvite() {
    }

    public void accept(@NotNull Player player) {
        faction.joinPlayer(player, joinWithRank);
    }

    public UUID getInviteId() {
        return inviteId;
    }

    public UUID getReceiver() {
        return receiver;
    }
}
