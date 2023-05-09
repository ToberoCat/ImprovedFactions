package io.github.toberocat.improvedFactions.core.command.faction;

import io.github.toberocat.improvedFactions.core.faction.components.FactionPermission;
import io.github.toberocat.improvedFactions.core.handler.ImprovedFactions;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.command.PlayerSubCommand;
import io.github.toberocat.improvedFactions.core.utils.command.SubCommand;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import io.github.toberocat.improvedFactions.core.utils.command.options.ArgLengthOption;
import io.github.toberocat.improvedFactions.core.utils.command.options.FactionOption;
import io.github.toberocat.improvedFactions.core.utils.command.options.FactionPermissionOption;
import io.github.toberocat.improvedFactions.core.utils.command.options.Options;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class SettingFactionCommand extends PlayerSubCommand {
    public SettingFactionCommand() {
        super("settings", Options.getFromConfig("settings")
                .opt(new ArgLengthOption(0, 20 * 5))
                .opt(new FactionOption(true))
                .opt(new FactionPermissionOption(FactionPermission.OPEN_SETTINGS_PERMISSION)));
    }

    @Override
    protected boolean handle(@NotNull FactionPlayer player, @NotNull String[] args) throws CommandException {
        ImprovedFactions.api().getGuis().openGui(player, "settings");
        return true;
    }

    @Override
    protected @Nullable List<String> getTab(@NotNull FactionPlayer player, @NotNull String[] args) throws CommandException {
        return null;
    }
}
