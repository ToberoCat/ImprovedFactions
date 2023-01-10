package io.github.toberocat.improvedFactions.core.command.sub.invite;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerIsAlreadyInFactionException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerIsBannedException;
import io.github.toberocat.improvedFactions.core.exceptions.invite.JoinWithRankInvalidException;
import io.github.toberocat.improvedFactions.core.exceptions.invite.PlayerHasntBeenInvitedException;
import io.github.toberocat.improvedFactions.core.exceptions.player.PlayerNotFoundException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.invite.InviteHandler;
import io.github.toberocat.improvedFactions.core.invite.PersistentInvites;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.Placeholder;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class AcceptInviteCommand extends Command<AcceptInviteCommand.AcceptPacket,
        Command.ConsoleCommandPacket> {

    public static final String LABEL = "acceptinvite";

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
                .setAllowInConsole(false)
                .setRequiresNoFaction(true)
                .setRequiredSpigotPermission(permission());
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        PersistentInvites.ReceiverMap map = InviteHandler.getInvites(player);
        if (map == null) return Collections.emptyList();

        return map.keySet().stream().toList();
    }

    @Override
    public void run(@NotNull AcceptPacket packet) {
        try {
            InviteHandler.acceptInvite(packet.receiver, packet.faction);
        } catch (PlayerHasntBeenInvitedException e) {
            packet.receiver.sendMessage(node.andThen(map -> map.get("not-invited")),
                    new Placeholder("faction", packet.faction.getDisplay()));
        } catch (PlayerNotFoundException e) {
            packet.receiver.sendMessage(node.andThen(map -> map.get("inviter-not-found")));
        } catch (JoinWithRankInvalidException e) {
            packet.receiver.sendMessage(node.andThen(map -> map.get("invalid-rank")));
        } catch (FactionIsFrozenException e) {
            packet.receiver.sendMessage(node.andThen(map -> map.get("faction-frozen")));
        } catch (PlayerIsAlreadyInFactionException e) {
            packet.receiver.sendMessage(node.andThen(map -> map.get("already-in-faction")));
        } catch (PlayerIsBannedException e) {
            packet.receiver.sendMessage(node.andThen(map -> map.get("sender-banned")));
        }
    }

    @Override
    public void runConsole(@NotNull Command.ConsoleCommandPacket packet) {

    }

    @Override
    public @Nullable AcceptInviteCommand.AcceptPacket createFromArgs(@NotNull FactionPlayer<?> executor,
                                                                     @NotNull String[] args) {
        if (args.length != 1) {
            executor.sendMessage(node.andThen(map -> map.get("not-enough-args")));
            return null;
        }

        PersistentInvites.ReceiverMap invites = InviteHandler.getInvites(executor);
        if (invites == null) {
            executor.sendMessage(node.andThen(map -> map.get("not-invited")),
                    new Placeholder("faction", args[0]));
            return null;
        }

        if (!invites.containsKey(args[0])) {
            executor.sendMessage(node.andThen(map -> map.get("not-invited")),
                    new Placeholder("faction", args[0]));
            return null;
        }

        Faction<?> faction;
        try {
            faction = FactionHandler.getFaction(args[0]);
        } catch (FactionNotInStorage e) {
            executor.sendMessage(node.andThen(map -> map.get("cant-find-faction")),
                    new Placeholder("faction", args[0]));
            return null;
        }

        return new AcceptPacket(faction, executor);
    }

    @Override
    public @Nullable AcceptInviteCommand.AcceptPacket createFromArgs(@NotNull String[] args) {
        return null;
    }


    protected record AcceptPacket(@NotNull Faction<?> faction,
                                  @NotNull FactionPlayer<?> receiver)
            implements Command.CommandPacket, Command.ConsoleCommandPacket {

    }
}
