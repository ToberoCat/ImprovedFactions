package io.github.toberocat.improvedFactions.core.command.faction;

import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.utils.command.SubCommand;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import io.github.toberocat.improvedFactions.core.utils.command.options.ArgLengthOption;
import io.github.toberocat.improvedFactions.core.utils.command.options.FactionOption;
import io.github.toberocat.improvedFactions.core.utils.command.options.Options;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SettingFactionCommand extends SubCommand {
    public SettingFactionCommand() {
        super("settings", Options.getFromConfig("settings")
                .opt(new ArgLengthOption(0, 20 * 5))
                .opt(new FactionOption(true)));
    }

    @Override
    protected boolean handleCommand(@NotNull CommandSender sender, @NotNull String[] args)
            throws CommandException {
        return false;
    }

    @Override
    protected @Nullable List<String> getTabList(@NotNull CommandSender sender, @NotNull String[] args)
            throws CommandException {
        return null;
    }
}
