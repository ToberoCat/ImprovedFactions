package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import org.jetbrains.annotations.NotNull;

public class ArgLengthOption implements Option {
    private final int length;

    public ArgLengthOption(int length) {
        this.length = length;
    }

    @Override
    public void canExecute(@NotNull CommandSender sender, @NotNull String[] args) throws CommandException {
        if (args.length != length)
            throw new CommandException("exceptions.not-enough-arguments", () -> new PlaceholderBuilder()
                    .placeholder("provided", args.length)
                    .placeholder("required", length)
                    .getPlaceholders());
    }
}
