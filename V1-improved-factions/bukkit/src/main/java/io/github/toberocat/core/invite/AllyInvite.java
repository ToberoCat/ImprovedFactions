package io.github.toberocat.core.invite;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.handler.FactionHandler;
import io.github.toberocat.core.utility.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.core.utility.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.core.utility.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.core.utility.language.Parser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AllyInvite extends Invite {

    private final Faction<?> inviter;
    private final Faction<?> invited;

    public AllyInvite(@NotNull UUID inviteId,
                      @NotNull UUID receiver,
                      long duration) throws FactionNotInStorage, PlayerHasNoFactionException {
        super(inviteId, receiver, duration);
        this.inviter = FactionHandler.getFaction(Bukkit.getOfflinePlayer(inviteId));
        this.invited = FactionHandler.getFaction(Bukkit.getOfflinePlayer(inviteId));
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
