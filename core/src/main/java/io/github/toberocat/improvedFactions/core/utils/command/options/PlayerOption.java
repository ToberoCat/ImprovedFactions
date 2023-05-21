package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandForPlayersException;
import org.jetbrains.annotations.NotNull;

public interface PlayerOption extends Option {
    @Override
    default @NotNull String[] execute(@NotNull CommandSender sender, @NotNull String[] args) throws CommandException {
        if (!(sender instanceof FactionPlayer player))
            throw new CommandForPlayersException();
        return executePlayer(player, args);
    }

    @NotNull String[] executePlayer(@NotNull FactionPlayer sender,
                                  @NotNull String[] args) throws CommandException;
}
