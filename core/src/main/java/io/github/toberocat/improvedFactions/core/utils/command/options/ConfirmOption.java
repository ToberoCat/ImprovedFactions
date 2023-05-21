package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import org.jetbrains.annotations.NotNull;

public class ConfirmOption implements Option {
    @Override
    public @NotNull String[] execute(@NotNull CommandSender sender, @NotNull String[] args) throws CommandException {
        int lastArg = args.length - 1;
        if (lastArg < 0 || !args[lastArg].equals("confirm"))
            throw new CommandException("exceptions.confirmation-needed");
        return args;
    }
}
