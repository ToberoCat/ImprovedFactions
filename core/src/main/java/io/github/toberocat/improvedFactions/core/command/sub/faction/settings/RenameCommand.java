package io.github.toberocat.improvedFactions.core.command.sub.faction.settings;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionCantBeRenamedToThisLiteralException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.faction.components.FactionPermission;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RenameCommand
        extends Command<RenameCommand.RenamePacket, RenameCommand.RenameConsolePacket> {

    private static final List<String> nameTabComplete = List.of("<name>");

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public @NotNull String label() {
        return "rename";
    }

    @Override
    protected @NotNull CommandSettings createSettings() {
        return new CommandSettings(node)
                .setRequiredSpigotPermission(permission())
                .setRequiredFactionPermission(FactionPermission.RENAME_FACTION)
                .setAllowInConsole(true);
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        if (args.length <= 1) return FactionHandler.getAllFactions().toList();
        return nameTabComplete;
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player,
                                                   @NotNull String[] args) {
        return nameTabComplete;
    }

    @Override
    public void run(@NotNull RenamePacket packet) {
        try {
            Faction<?> faction = packet.player.getFaction();
            faction.renameFaction(packet.newName);
            packet.player.sendTranslatable(node.andThen(map -> "renamed"));
        } catch (PlayerHasNoFactionException e) {
            packet.player.sendTranslatable(node.andThen(map -> "sender-has-no-faction"));
        } catch (FactionNotInStorage e) {
            packet.player.sendTranslatable(node.andThen(map -> "faction-not-in-storage"));
        } catch (FactionCantBeRenamedToThisLiteralException e) {
            packet.player.sendTranslatable(node.andThen(map -> "invalid-faction-name"));
        } catch (FactionIsFrozenException e) {
            packet.player.sendTranslatable(node.andThen(map -> "faction-is-frozen"));
        }
    }

    @Override
    public void runConsole(@NotNull RenameConsolePacket packet) {
        boolean frozen = packet.faction.isFrozen();
        packet.faction.setFrozen(false);
        try {
            packet.faction.renameFaction(packet.newName);
        } catch (FactionCantBeRenamedToThisLiteralException e) {
            Logger.api().logInfo("Faction can't be renamed to this name");
        } catch (FactionIsFrozenException e) {
            Logger.api().logInfo("Faction is frozen");
        } finally {
            packet.faction.setFrozen(frozen);
        }
    }

    @Override
    public @Nullable RenameCommand.RenamePacket createFromArgs(@NotNull FactionPlayer<?> executor,
                                                               @NotNull String[] args) {
        if (args.length <= 1) {
            executor.sendTranslatable(node.andThen(map -> map.get("not-enough-args")));
            return null;
        }
        return new RenamePacket(executor, args[0]);
    }

    @Override
    public @Nullable RenameCommand.RenameConsolePacket createFromArgs(@NotNull String[] args) {
        if (args.length <= 2) {
            Logger.api().logInfo("Not enough args. Requires a faction and a new name");
            return null;
        }

        Faction<?> faction;
        try {
            faction = FactionHandler.getFaction(args[0]);
        } catch (FactionNotInStorage e) {
            Logger.api().logInfo("Faction not found");
            return null;
        }

        return new RenameConsolePacket(faction, args[1]);
    }


    protected static record RenamePacket(@NotNull FactionPlayer<?> player, @NotNull String newName)
            implements Command.CommandPacket {

    }

    protected static record RenameConsolePacket(@NotNull Faction<?> faction, @NotNull String newName)
            implements Command.ConsoleCommandPacket {

    }
}
