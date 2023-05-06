package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import org.jetbrains.annotations.NotNull;

public class FactionOption implements PlayerOption {
    private boolean needFaction;

    public FactionOption(boolean needFaction) {
        this.needFaction = needFaction;
    }

    @Override
    public void canExecutePlayer(@NotNull FactionPlayer sender, @NotNull String[] args) throws CommandException {
        if ((sender.getFactionRegistry() == null) == needFaction)
            throw new CommandException(needFaction
                    ? "You have to be in a faction to use this command"
                    : "You can't be in a faction for this command");
    }
}
