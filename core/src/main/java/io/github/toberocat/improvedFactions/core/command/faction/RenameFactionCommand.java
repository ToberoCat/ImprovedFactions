package io.github.toberocat.improvedFactions.core.command.faction;

import io.github.toberocat.improvedFactions.core.faction.Faction;
import io.github.toberocat.improvedFactions.core.faction.components.FactionPermission;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.command.PlayerSubCommand;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import io.github.toberocat.improvedFactions.core.utils.command.options.ArgLengthOption;
import io.github.toberocat.improvedFactions.core.utils.command.options.FactionPermissionOption;
import io.github.toberocat.improvedFactions.core.utils.command.options.MaxArgLengthOption;
import io.github.toberocat.improvedFactions.core.utils.command.options.Options;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class RenameFactionCommand extends PlayerSubCommand {
    public RenameFactionCommand() {
        super("rename", Options.getFromConfig("rename")
                .opt(new FactionPermissionOption(FactionPermission.RENAME_FACTION))
                .opt(new ArgLengthOption(1, 20 * 5))
                .opt(new MaxArgLengthOption(0, Faction.MAX_FACTION_DISPLAY_LENGTH, 20 * 5)));
    }

    @Override
    protected boolean handle(@NotNull FactionPlayer player,
                             @NotNull String[] args)
            throws CommandException {
        return false;
    }

    @Override
    protected @Nullable List<String> getTab(@NotNull FactionPlayer player,
                                            @NotNull String[] args)
            throws CommandException {
        return null;
    }
}
