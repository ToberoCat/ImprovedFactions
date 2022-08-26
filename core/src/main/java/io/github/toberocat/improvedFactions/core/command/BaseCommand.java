package io.github.toberocat.improvedFactions.core.command;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.command.sub.CreateFactionCommand;
import io.github.toberocat.improvedFactions.core.command.sub.DeleteFactionCommand;
import io.github.toberocat.improvedFactions.core.command.sub.ListFactionCommand;
import io.github.toberocat.improvedFactions.core.sender.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BaseCommand extends Command<Command.CommandPacket, Command.ConsoleCommandPacket> {

    public BaseCommand() {
        add(new CreateFactionCommand());
        add(new DeleteFactionCommand());
        add(new ListFactionCommand());
    }

    @Override
    public @NotNull String label() {
        return "faction";
    }

    @Override
    public CommandSettings settings() {
        return new CommandSettings();
    }

    @Override
    public @NotNull List<String> tabCompletePlayer(@NotNull FactionPlayer<?> player, @NotNull String[] args) {
        return new ArrayList<>(commands.keySet());
    }

    @Override
    public @NotNull List<String> tabCompleteConsole(@NotNull String[] args) {
        return new ArrayList<>(commands.keySet());
    }

    @Override
    public void run(@NotNull CommandPacket packet) {

    }

    @Override
    public void runConsole(@NotNull ConsoleCommandPacket packet) {

    }

    @Override
    public @Nullable Command.CommandPacket createFromArgs(@NotNull FactionPlayer<?> executor, @NotNull String[] args) {
        return new CommandPacket() {
        };
    }

    @Override
    public @Nullable Command.ConsoleCommandPacket createFromArgs(@NotNull String[] args) {
        return new ConsoleCommandPacket() {
        };
    }
}
