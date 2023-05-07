package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import org.jetbrains.annotations.NotNull;

public interface Option {
    void canExecute(@NotNull CommandSender sender, @NotNull String[] args) throws CommandException;

    default boolean show(@NotNull CommandSender sender, @NotNull String[] args) {
        return true;
    }
}
