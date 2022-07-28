package io.github.toberocat.core.invite;

import io.github.toberocat.core.factions.Faction;
import io.github.toberocat.core.factions.components.rank.members.FactionRank;
import io.github.toberocat.core.utility.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.core.utility.language.Language;
import io.github.toberocat.core.utility.language.Parser;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class FactionInvite extends Invite {

    private final Faction<?> faction;
    private final FactionRank joinAs;

    public FactionInvite(@NotNull UUID inviteId,
                         @NotNull UUID receiver,
                         @NotNull Faction<?> faction,
                         @NotNull FactionRank joinAs,
                         long duration) {
        super(inviteId, receiver, duration);
        this.faction = faction;
        this.joinAs = joinAs;
    }

    @Override
    public void ranOutOfTime() {
        Player player = Bukkit.getPlayer(receiver);
        if (player != null) Parser.run("invite.faction.out-of-time")
                .parse("{faction}", faction.getRegistry())
                .send(player);

        cancelTask();
    }

    @Override
    public void cancelInvite(@NotNull Player player) {
        Parser.run("invite.faction.cancel")
                .parse("{faction}", faction.getRegistry())
                .send(player);

        cancelTask();
    }

    @Override
    public void accept(@NotNull Player player) {
        try {
            faction.joinPlayer(player, joinAs);
            Parser.run("invite.faction.joined")
                    .parse("{faction}", faction.getRegistry())
                    .parse("{inviter}", Bukkit.getOfflinePlayer(inviteId).getName())
                    .parse("{rank}", joinAs.getDisplayName())
                    .send(player);
        } catch (FactionIsFrozenException e) {
            Language.sendMessage("invite.faction.frozen", player);
        }

        cancelTask();
    }
}
