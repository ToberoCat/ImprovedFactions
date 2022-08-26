package io.github.toberocat.improvedFactions.core.command.sub;

import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.command.component.ConfirmCommand;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionOwnerRank;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.layout.Translatable;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;
import java.util.function.Function;

public class DeleteFactionCommand extends ConfirmCommand
        <DeleteFactionCommand.DeleteFactionPacket, DeleteFactionCommand.DeleteFactionConsolePacket> {

    @Override
    public @NotNull String label() {
        return "delete";
    }

    @Override
    public CommandSettings settings() {
        return new CommandSettings()
                .setRequiredSpigotPermission(permission())
                .setRequiresFaction(true)
                .setAllowInConsole(true)
                .setRequiresRank(FactionOwnerRank.REGISTRY);
    }

    @Override
    public void run(@NotNull DeleteFactionPacket packet) {
        FactionPlayer<?> executor = packet.executor;

        try {
            packet.faction.deleteFaction();
            executor.sendTranslatable(translatable -> translatable
                    .getMessages()
                    .getCommand()
                    .get(label())
                    .get("deleted-faction"));
        } catch (FactionIsFrozenException e) {
            executor.sendTranslatable(translatable ->
                    translatable.getMessages()
                            .getCommand()
                            .get(label())
                            .get("faction-frozen"));
        }
    }

    @Override
    public void runConsole(@NotNull DeleteFactionConsolePacket packet) {
        Faction<?> faction = packet.faction;
        faction.setFrozen(false);

        try {
            packet.faction.deleteFaction();
            Logger.api().logInfo("Faction %s got deleted", packet.faction.getRegistry());
        } catch (FactionIsFrozenException e) {
            Logger.api().logInfo("Couldn't delete faction, because it's frozen");
        }
    }

    @Override
    protected @NotNull List<String> tabCompleteConfirmed(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    protected @NotNull List<String> tabCompleteConfirmed(@NotNull String[] args) {
        return FactionHandler.getAllFactions().toList();
    }

    @Override
    protected @Nullable DeleteFactionCommand.DeleteFactionPacket createFromArgsConfirmed(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        try {
            return new DeleteFactionPacket(executor.getFaction(), executor);
        } catch (PlayerHasNoFactionException e) {
            executor.sendTranslatable(translatable -> translatable
                    .getMessages()
                    .getCommand()
                    .get(label())
                    .get("player-has-no-faction"));
        } catch (FactionNotInStorage factionNotInStorage) {
            executor.sendTranslatable(translatable -> translatable
                    .getMessages()
                    .getCommand()
                    .get(label())
                    .get("faction-not-in-storage"));
        }
        return null;
    }

    @Override
    protected @Nullable DeleteFactionCommand.DeleteFactionConsolePacket createFromArgsConfirmed(@NotNull String[] args) {
        String registry = args[0];
        try {
            Faction<?> faction = FactionHandler.getFaction(registry);
            return new DeleteFactionConsolePacket(faction);
        } catch (FactionNotInStorage e) {
            Logger.api().logInfo("Couldn't find faction " + args[0]);
            return null;
        }
    }

    @Override
    protected Function<Translatable, String> notConfirmedTranslatable() {
        return translatable -> translatable
                .getMessages()
                .getCommand()
                .get(label())
                .get("not-confirmed");
    }

    protected record DeleteFactionPacket(@NotNull Faction<?> faction, @NotNull FactionPlayer<?> executor)
            implements CommandPacket {
    }

    protected record DeleteFactionConsolePacket(@NotNull Faction<?> faction)
            implements ConsoleCommandPacket {
    }
}
