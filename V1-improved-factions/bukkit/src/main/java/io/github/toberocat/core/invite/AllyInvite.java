package io.github.toberocat.core.invite;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.utility.Utility;
import io.github.toberocat.core.utility.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.core.utility.exceptions.faction.FactionOwnerIsOfflineException;
import io.github.toberocat.core.utility.language.Parser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class AllyInvite extends Invite {

    private final Faction<?> inviter;
    private final Faction<?> invited;

    public AllyInvite(@NotNull Player inviter,
                      @NotNull Faction<?> inviterFaction,
                      @NotNull Faction<?> invitedFaction,
                      long duration) throws FactionOwnerIsOfflineException {
        super(inviter.getUniqueId(), invitedFaction.getOwner(), duration);
        if (!Utility.isOnline(invitedFaction.getOwner()))
            throw new FactionOwnerIsOfflineException(invitedFaction);

        this.inviter = inviterFaction;
        this.invited = invitedFaction;
    }

    public AllyInvite(@NotNull Player inviter,
                      @NotNull Faction<?> inviterFaction,
                      @NotNull Faction<?> invitedFaction) {
        super(inviter.getUniqueId(), invitedFaction.getOwner(), DEFAULT_DURATION);

        this.inviter = inviterFaction;
        this.invited = invitedFaction;
    }

    @Override
    public void ranOutOfTime() {
        Player player = Bukkit.getPlayer(receiver);
        if (player != null) Parser.run("invite.ally.out-of-time")
                .parse("{inviter}", invited.getRegistry())
                .parse("{invited}", invited.getRegistry())
                .send(player);

        cancelTask();
    }

    @Override
    public void cancelInvite(@NotNull Player player) {
        Parser.run("invite.ally.cancel")
                .parse("{inviter}", invited.getRegistry())
                .parse("{invited}", invited.getRegistry())
                .send(player);

        cancelTask();
    }

    @Override
    public void accept(@NotNull Player player) {
        try {
            inviter.addAlly(invited);
        } catch (FactionIsFrozenException e) {
            Parser.run("invite.ally.frozen-inviter")
                    .parse("{inviter}", invited.getRegistry())
                    .parse("{invited}", invited.getRegistry())
                    .send(player);
            return;
        }

        try {
            invited.addAlly(inviter);
        } catch (FactionIsFrozenException e) {
            Parser.run("invite.ally.frozen-invited")
                    .parse("{inviter}", invited.getRegistry())
                    .parse("{invited}", invited.getRegistry())
                    .send(player);
            return;
        }

        Parser.run("invite.ally.joined")
                .parse("{inviter}", invited.getRegistry())
                .parse("{invited}", invited.getRegistry())
                .parse("{inviter}", Bukkit.getOfflinePlayer(inviteId).getName())
                .send(player);

        Player inviter = Bukkit.getPlayer(inviteId);
        if (inviter == null) return;

        Parser.run("invite.ally.accepted")
                .parse("{inviter}", invited.getRegistry())
                .parse("{invited}", invited.getRegistry())
                .parse("{inviter}", Bukkit.getOfflinePlayer(inviteId).getName())
                .send(inviter);

        cancelTask();
    }
}
