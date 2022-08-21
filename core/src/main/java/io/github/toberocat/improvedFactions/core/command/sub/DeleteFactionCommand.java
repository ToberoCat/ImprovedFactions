package io.github.toberocat.improvedFactions.core.command.sub;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.rank.members.FactionOwnerRank;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class DeleteFactionCommand extends Command<DeleteFactionCommand.DeleteFactionPacket> {

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
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player,
                                                   @NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return FactionHandler.getAllFactions().toList();
    }

    @Override
    public void run(@NotNull DeleteFactionPacket packet) {
        FactionPlayer<?> executor = packet.executor;
        if (executor == null) packet.faction.setFrozen(false); // Admin command allows everything

        try {
            packet.faction.deleteFaction();
        } catch (FactionIsFrozenException e) {
            if (executor != null) executor.sendTranslatable(translatable ->
                    translatable.getMessages()
                            .getCommand()
                            .get(label())
                            .get("faction-frozen"));
            else Logger.api().logInfo("Faction is frozen and can't get deleted");
        }
    }

    @Override
    public @Nullable DeleteFactionCommand.DeleteFactionPacket createFromArgs(@NotNull FactionPlayer<?> executor,
                                                                             @NotNull String[] args) {
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
    public @Nullable DeleteFactionCommand.DeleteFactionPacket createFromArgs(@NotNull String[] args) {
        String registry = args[0];
        try {
            Faction<?> faction = FactionHandler.getFaction(registry);
            return new DeleteFactionPacket(faction, null);
        } catch (FactionNotInStorage e) {
            Logger.api().logInfo("Couldn't find faction " + args[0]);
            return null;
        }
    }

    public record DeleteFactionPacket(@NotNull Faction<?> faction, @Nullable FactionPlayer<?> executor)
            implements CommandPacket {
    }
}
