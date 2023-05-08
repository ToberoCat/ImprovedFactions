package io.github.toberocat.improvedFactions.core.utils.command;

import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandForPlayersException;
import io.github.toberocat.improvedFactions.core.utils.command.options.Option;
import io.github.toberocat.improvedFactions.core.utils.command.options.Options;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public abstract class PlayerSubCommand extends SubCommand {
    public PlayerSubCommand(@NotNull String label) {
        super(label);
    }

    public PlayerSubCommand(@NotNull String label, @NotNull Options options) {
        super(label, options);
    }

    public PlayerSubCommand(@NotNull String permission, @NotNull String label, @NotNull Options options) {
        super(permission, label, options);
    }

    public PlayerSubCommand(@NotNull String permission, @NotNull String label, Option[] onCommandOptions, Option[] onTabOptions) {
        super(permission, label, onCommandOptions, onTabOptions);
    }

    @Override
    protected boolean handleCommand(@NotNull CommandSender sender, @NotNull String[] args) throws CommandException {
        if (!(sender instanceof FactionPlayer player))
            throw new CommandForPlayersException();
        return handle(player, args);
    }

    @Override
    protected @Nullable List<String> getTabList(@NotNull CommandSender sender, @NotNull String[] args) throws CommandException {
        if (!(sender instanceof FactionPlayer player))
            throw new CommandForPlayersException();
        return getTab(player, args);
    }

    protected abstract boolean handle(@NotNull FactionPlayer player, @NotNull String[] args)
            throws CommandException;
    protected abstract @Nullable List<String> getTab(@NotNull FactionPlayer player, @NotNull String[] args)
            throws CommandException;
}
