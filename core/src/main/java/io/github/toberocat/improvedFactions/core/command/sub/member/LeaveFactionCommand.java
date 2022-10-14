package io.github.toberocat.improvedFactions.core.command.sub.member;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.leave.PlayerIsOwnerException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class LeaveFactionCommand extends Command<LeaveFactionCommand.LeavePacket, LeaveFactionCommand.LeavePacket> {

    public static final String LABEL = "leave";

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
                .setAllowInConsole(true)
                .setRequiredSpigotPermission(permission())
                .setRequiresFaction(true);
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return null;
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        return null;
    }

    @Override
    public void run(@NotNull LeavePacket packet) {
        FactionPlayer<?> player = packet.player;
        try {
            packet.faction.leavePlayer(player);
            player.sendTranslatable(node.andThen(map -> map.get("player-left")));
        } catch (FactionIsFrozenException e) {
            player.sendTranslatable(node.andThen(map -> map.get("faction-frozen")));
        } catch (PlayerIsOwnerException e) {
            player.sendTranslatable(node.andThen(map -> map.get("player-is-owner")));
        } catch (PlayerHasNoFactionException e) {
            player.sendTranslatable(node.andThen(map -> map.get("player-has-no-faction")));
        }
    }

    @Override
    public void runConsole(@NotNull LeavePacket packet) {

    }

    @Override
    public @Nullable LeaveFactionCommand.LeavePacket createFromArgs(@NotNull FactionPlayer<?> executor,
                                                                    @NotNull String[] args) {
        if (args.length != 1) {
            executor.sendTranslatable(node.andThen(map -> map.get("not-enough-args")));
            return null;
        }

        try {
            Faction<?> faction = FactionHandler.getFaction(args[0]);
            return new LeavePacket(executor, faction);
        } catch (FactionNotInStorage e) {
            executor.sendTranslatable(node.andThen(map -> map.get("faction-not-found")));
            return null;
        }
    }

    @Override
    public @Nullable LeaveFactionCommand.LeavePacket createFromArgs(@NotNull String[] args) {
        if (args.length != 1) {
            Logger.api().logInfo("You need to give a player");
            return null;
        }
        String playerName = args[0];
        FactionPlayer<?> executor = ImprovedFactions.api().getPlayer(playerName);
        if (executor == null) {
            Logger.api().logInfo("Player wasn't found");
            return null;
        }

        try {
            Faction<?> faction = executor.getFaction();
            return new LeavePacket(executor, faction);
        } catch (PlayerHasNoFactionException e) {
            Logger.api().logInfo("Player is in no faction");
        } catch (FactionNotInStorage e) {
            Logger.api().logInfo("Player faction isn't found in storage");
        }
        return null;
    }


    protected record LeavePacket(@NotNull FactionPlayer<?> player,
                                 @NotNull Faction<?> faction)
    implements Command.CommandPacket, Command.ConsoleCommandPacket {

    }
}