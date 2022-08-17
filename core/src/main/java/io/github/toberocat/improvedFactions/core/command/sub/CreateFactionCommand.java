package io.github.toberocat.improvedFactions.core.command.sub;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionAlreadyExistsException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.IllegalFactionNamingException;
import io.github.toberocat.improvedFactions.core.faction.handler.FactionHandler;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
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
    public @NotNull List<String> tabComplete(@NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public void run(@NotNull CreateFactionPacket packet) {
        FactionPlayer<?> owner = packet.owner;

        try {
            FactionHandler.createFaction(packet.display, owner);
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
        if (args.length != 2) return null;

        String display = args[1];
        return new CreateFactionPacket(display, executor);
    }

    public record CreateFactionPacket(@NotNull String display,
                                      @NotNull FactionPlayer<?> owner)
            implements CommandPacket {
    }
}
