package io.github.toberocat.improvedFactions.core.oldcommand.component;

import io.github.toberocat.improvedFactions.core.oldcommand.component.exceptions.CommandExceptions;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;


public abstract class PlayerSubCommand extends SubCommand {
    public PlayerSubCommand(@NotNull String label) {
        super(label);
    }

    protected abstract boolean runPlayer(@NotNull FactionPlayer<?> player,
                                         @NotNull String[] args)
            throws CommandExceptions;

    protected abstract @Nullable List<String> runPlayerTab(@NotNull FactionPlayer<?> player,
                                                           @NotNull String[] args);

    @Override
    protected boolean run(@NotNull CommandSender sender, @NotNull String[] args)
            throws CommandExceptions {
        if (sender instanceof FactionPlayer<?>)
            return runPlayer((FactionPlayer<?>) sender, args);
        throw new CommandExceptions("This command can only get run by players");
    }

    @Override
    protected @Nullable List<String> runTab(@NotNull CommandSender sender, @NotNull String[] args) {
        if (sender instanceof FactionPlayer<?>) return runPlayerTab((FactionPlayer<?>) sender, args);
        return null;
    }

}
