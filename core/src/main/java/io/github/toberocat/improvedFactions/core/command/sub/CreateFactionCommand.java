package io.github.toberocat.improvedFactions.core.command.sub;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionAlreadyExistsException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.IllegalFactionNamingException;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class CreateFactionCommand extends Command<CreateFactionCommand.CreateFactionPacket> {
    @Override
    public @NotNull String label() {
        return "create";
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        return List.of("name");
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        if (args.length <= 1) return ImprovedFactions.api()
                .listPlayers()
                .getOnlinePlayerNames()
                .toList();

        if (args.length <= 2) return List.of("name");

        return Collections.emptyList();
    }

    @Override
    public void run(@NotNull CreateFactionPacket packet) {
        FactionPlayer<?> owner = packet.owner;

        try {
            FactionHandler.createFaction(packet.display, owner);

            owner.sendTranslatable(translatable -> translatable
                    .getMessages()
                    .getCommand()
                    .get(label())
                    .get("created-faction"));
        } catch (IllegalFactionNamingException e) {
            owner.sendTranslatable(translatable -> translatable
                    .getMessages()
                    .getCommand()
                    .get(label())
                    .get("illegal-naming"));
        } catch (FactionAlreadyExistsException e) {
            owner.sendTranslatable(translatable -> translatable
                    .getMessages()
                    .getCommand()
                    .get(label())
                    .get("faction-already-exists"));
        }
    }

    @Override
    public @Nullable CreateFactionCommand.CreateFactionPacket createFromArgs(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        if (args.length != 1) {
            executor.sendTranslatable(translatable -> translatable
                    .getMessages()
                    .getCommand()
                    .get(label())
                    .get("not-enough-arguments"));
            return null;
        }

        String display = args[0];
        return new CreateFactionPacket(display, executor);
    }

    @Override
    public @Nullable CreateFactionCommand.CreateFactionPacket createFromArgs(@NotNull String[] args) {
        if (args.length != 2) {
            Logger.api().logInfo("You need to specify the player who creates a " +
                    "faction, as well as it's name");
            return null;
        }

        String player = args[0];
        ImprovedFactions<?> api = ImprovedFactions.api();
        if (api.listPlayers()
                .getOnlinePlayerNames()
                .noneMatch(x -> x.equals(player))) {
            Logger.api().logInfo("Couldn't find a online player with this name");
            return null;
        }

        FactionPlayer<?> executor = api.getPlayer(player);
        if (executor == null) {
            Logger.api().logInfo("Couldn't find a online player with this name");
            return null;
        }

        String display = args[1];
        return new CreateFactionPacket(display, executor);
    }

    public record CreateFactionPacket(@NotNull String display,
                                      @NotNull FactionPlayer<?> owner)
            implements CommandPacket {
    }
}
