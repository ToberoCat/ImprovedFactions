package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import org.jetbrains.annotations.NotNull;

public class MaxArgLengthOption implements Option {
    private final int maxLengthOfArg;
    private final int index;

    public MaxArgLengthOption(int index, int maxLengthOfArg) {
        this.maxLengthOfArg = maxLengthOfArg;
        this.index = index;
    }

    @Override
    public void canExecute(@NotNull CommandSender sender, @NotNull String[] args) throws CommandException {
        if (index >= args.length)
            return;
        if (args[index].length() > maxLengthOfArg)
            throw new CommandException("§cYour argument exceeds the maximum length. Only §6" +
                    maxLengthOfArg + "§c characters allowed");
    }
}
