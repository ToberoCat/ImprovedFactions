package io.github.toberocat.improvedFactions.core.command;

import io.github.toberocat.improvedFactions.core.command.faction.CreateFactionCommand;
import io.github.toberocat.improvedFactions.core.utils.command.Command;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class RootCommand {
    public static void addCommands(@NotNull Command command) {
        List.of(
                new CreateFactionCommand()
        ).forEach(command::addChild);
    }
}
