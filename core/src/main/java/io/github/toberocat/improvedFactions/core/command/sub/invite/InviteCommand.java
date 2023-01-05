package io.github.toberocat.improvedFactions.core.command.sub.invite;

import io.github.toberocat.improvedFactions.core.command.BaseCommand;
import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerIsAlreadyInFactionException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.relation.CantInviteYourselfException;
import io.github.toberocat.improvedFactions.core.exceptions.invite.PlayerAlreadyMemberException;
import io.github.toberocat.improvedFactions.core.exceptions.invite.PlayerHasBeenInvitedException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.rank.GuestRank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.Rank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.RankContainers;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionMemberRank;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionRank;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.invite.InviteHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.player.OfflineFactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InviteCommand extends Command<InviteCommand.InvitePacket, InviteCommand.InvitePacket> {

    public static final String LABEL = "invite";

    @Override
    public @NotNull String label() {
        return LABEL;
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    protected @NotNull CommandSettings createSettings() {
        return new CommandSettings(node)
                .setRequiresFaction(true)
                .setAllowInConsole(true)
                .setRequiredSpigotPermission(permission());
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        if (args.length <= 1) return ImprovedFactions.api().listPlayers().getOnlinePlayerNames().toList();
        if (args.length <= 2) return ImprovedFactions.api().listPlayers().getPlayerNames().toList();
        if (args.length <= 3) return FactionHandler.getAllFactions().toList();
        return RankContainers.DEFAULT_FACTION_RANKS_CONTAINERS;
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        if (args.length <= 1) return ImprovedFactions.api().listPlayers().getPlayerNames().toList();
        return RankContainers.DEFAULT_FACTION_RANKS_CONTAINERS;
    }

    @Override
    public void run(@NotNull InvitePacket packet) {
        try {
            InviteHandler.sendInvite(
                    packet.receiver,
                    packet.sender,
                    packet.faction,
                    packet.rank);
            packet.sender.sendTranslatable(node.andThen(map -> map.get("invited-sender")));
            packet.receiver.sendMessage(node.andThen(map -> map.get("you-have-been-invited")),
                    new Placeholder("sender", packet.sender().getName()),
                    new Placeholder("faction", packet.faction.getRegistry()),
                    new Placeholder("rank", packet.rank.getRegistry()));
        } catch (PlayerHasBeenInvitedException e) {
            packet.sender.sendTranslatable(node.andThen(map -> map.get("sender-already-been-invited")));
        } catch (CantInviteYourselfException e) {
            packet.sender.sendTranslatable(node.andThen(map -> map.get("cant-invite-yourself")));
        } catch (PlayerAlreadyMemberException e) {
            packet.sender.sendTranslatable(node.andThen(map -> map.get("already-member")));
        } catch (PlayerIsAlreadyInFactionException e) {
            packet.sender.sendTranslatable(node.andThen(map -> map.get("already-in-faction")));
        }
    }

    @Override
    public void runConsole(@NotNull InvitePacket packet) {
        try {
            InviteHandler.sendInvite(
                    packet.receiver,
                    packet.sender,
                    packet.faction,
                    packet.rank);
            //ToDo: Send clickable accept message
        } catch (PlayerHasBeenInvitedException e) {
            Logger.api().logInfo("Player has already been invited");

        } catch (CantInviteYourselfException e) {
            Logger.api().logInfo("Cant invite yourself");
        } catch (PlayerAlreadyMemberException e) {
            Logger.api().logInfo("Player already member");
        } catch (PlayerIsAlreadyInFactionException e) {
            Logger.api().logInfo("Player has already a faction");
        }
    }

    @Override
    public @Nullable InviteCommand.InvitePacket createFromArgs(@NotNull FactionPlayer<?> executor,
                                                               @NotNull String[] args) {
        if (args.length <= 0) {
            executor.sendTranslatable(node.andThen(map -> map.get("not-enough-args")));
            return null;
        }

        OfflineFactionPlayer<?> player = ImprovedFactions.api().getOfflinePlayer(args[0]);
        if (player == null) {
            executor.sendTranslatable(node.andThen(map -> map.get("sender-not-found")));
            return null;
        }

        FactionRank factionRank = (FactionRank) Rank.fromString(FactionMemberRank.REGISTRY);
        if (args.length > 1) {
            Rank rank = Rank.fromString(args[1]);
            if (rank.getRegistry().equals(GuestRank.REGISTRY) ||
                    !(rank instanceof FactionRank fRank)) {
                executor.sendTranslatable(node.andThen(map -> map.get("rank-not-found")));
                return null;
            }
            factionRank = fRank;
        }

        try {
            Faction<?> faction = executor.getFaction();
            return new InvitePacket(executor, player, faction, factionRank);
        } catch (PlayerHasNoFactionException e) {
            executor.sendTranslatable(node.andThen(map -> map.get("sender-has-no-faction")));
        } catch (FactionNotInStorage e) {
            executor.sendTranslatable(node.andThen(map -> map.get("faction-not-in-storage")));
        }

        return null;
    }

    @Override
    public @Nullable InviteCommand.InvitePacket createFromArgs(@NotNull String[] args) {
        if (args.length != 4) {
            Logger.api().logInfo("You need to give a sender, a receiver, a faction and a rank");
            return null;
        }

        FactionPlayer<?> sender = ImprovedFactions.api().getPlayer(args[0]);
        if (sender == null) {
            Logger.api().logInfo("Sender isn't found");
            return null;
        }

        OfflineFactionPlayer<?> player = ImprovedFactions.api().getOfflinePlayer(args[1]);
        if (player == null) {
            Logger.api().logInfo("Receiver isn't found");
            return null;
        }

        Faction<?> faction;
        try {
            faction = FactionHandler.getFaction(args[2]);
        } catch (FactionNotInStorage e) {
            Logger.api().logInfo("Faction wasn't found");
            return null;
        }

        Rank rank = Rank.fromString(args[3]);
        if (rank.getRegistry().equals(GuestRank.REGISTRY) ||
                !(rank instanceof FactionRank factionRank)) {
            Logger.api().logInfo("Rank not found");
            return null;
        }

        return new InvitePacket(sender, player, faction, factionRank);

    }

    protected record InvitePacket(@NotNull FactionPlayer<?> sender,
                                  @NotNull OfflineFactionPlayer<?> receiver,
                                  @NotNull Faction<?> faction,
                                  @NotNull FactionRank rank)
            implements Command.CommandPacket, Command.ConsoleCommandPacket {
    }

}
