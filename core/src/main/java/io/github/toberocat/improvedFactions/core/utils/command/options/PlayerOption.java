package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;

public interface PlayerOption extends Option {
    @Override
    default void canExecute(@NotNull CommandSender sender, @NotNull String[] args) throws CommandException {
        if (!(sender instanceof FactionPlayer player))
            throw new CommandException("exceptions.requires-player", HashMap::new);
        canExecutePlayer(player, args);
    }

    void canExecutePlayer(@NotNull FactionPlayer sender, @NotNull String[] args) throws CommandException;
}
