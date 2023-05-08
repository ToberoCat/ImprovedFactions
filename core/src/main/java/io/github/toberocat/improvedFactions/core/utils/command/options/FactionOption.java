package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import org.jetbrains.annotations.NotNull;

public class FactionOption implements PlayerOption {
    private final boolean needFaction;

    public FactionOption(boolean needFaction) {
        this.needFaction = needFaction;
    }

    @Override
    public void canExecutePlayer(@NotNull FactionPlayer sender, @NotNull String[] args) throws CommandException {
        if ((sender.getFactionRegistry() == null) == needFaction)
            throw new CommandException("exceptions." + (needFaction ? "need-faction" : "cant-be-in-faction"),
                    () -> new PlaceholderBuilder()
                            .placeholder("player", sender)
                            .getPlaceholders());
    }

    @Override
    public boolean show(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof FactionPlayer player))
            return false;
        return (player.getFactionRegistry() == null) != needFaction;
    }
}
