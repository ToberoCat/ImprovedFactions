package io.github.toberocat.improvedFactions.core.command.faction;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionIsFrozenException;
import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.command.PlayerSubCommand;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import io.github.toberocat.improvedFactions.core.utils.command.options.ConfirmOption;
import io.github.toberocat.improvedFactions.core.utils.command.options.FactionOption;
import io.github.toberocat.improvedFactions.core.utils.command.options.Options;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DeleteFactionCommand extends PlayerSubCommand {
    public DeleteFactionCommand() {
        super("delete", Options.getFromConfig("delete")
                .opt(new FactionOption(true))
                .cmdOpt(new ConfirmOption()));
    }

    @Override
    protected boolean handle(@NotNull FactionPlayer player, @NotNull String[] args)
            throws CommandException {
        try {
            player.getFaction().deleteFaction();
            player.sendMessage("commands.delete.deleted");
        } catch (FactionIsFrozenException | PlayerHasNoFactionException | FactionNotInStorage e) {
            throw new CommandException(e);
        }
        return false;
    }

    @Override
    protected @Nullable List<String> getTab(@NotNull FactionPlayer player, @NotNull String[] args)
            throws CommandException {
        return null;
    }
}
