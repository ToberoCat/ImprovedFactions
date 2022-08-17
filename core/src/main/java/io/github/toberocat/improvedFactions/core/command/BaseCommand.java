package io.github.toberocat.improvedFactions.core.command;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.sub.CreateFactionCommand;
import io.github.toberocat.improvedFactions.core.command.sub.DeleteFactionCommand;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class BaseCommand extends Command<Command.CommandPacket> {

    public BaseCommand() {
        add(new CreateFactionCommand());
        add(new DeleteFactionCommand());
    }

    @Override
    public @NotNull String label() {
        return "faction";
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull String[] args) {
        return Collections.emptyList();
    }

    @Override
    public void run(@NotNull CommandPacket packet) {

    }

    @Override
    public @Nullable Command.CommandPacket createFromArgs(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        return new CommandPacket() {
        };
    }
}
