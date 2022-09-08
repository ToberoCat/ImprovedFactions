package io.github.toberocat.improvedFactions.core.command;

import io.github.toberocat.improvedFactions.core.command.component.Command;
import io.github.toberocat.improvedFactions.core.command.component.CommandSettings;
import io.github.toberocat.improvedFactions.core.command.sub.*;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class BaseCommand extends Command<Command.CommandPacket, Command.ConsoleCommandPacket> {

    public BaseCommand() {
        add(new CreateFactionCommand());
        add(new DeleteFactionCommand());
        add(new ListFactionCommand());
        add(new ClaimCommand());
        add(new UnclaimCommand());
        add(new JoinFactionCommand());
        add(new LeaveFactionCommand());
        add(new InviteCommand());
    }

    @Override
    public @NotNull String label() {
        return "faction";
    }

    @Override
    public @NotNull CommandSettings createSettings() {
        return new CommandSettings(translatable -> translatable.getMessages()
                .getCommand()
                .get("command-settings"));
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
