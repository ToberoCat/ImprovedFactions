package io.github.toberocat.improvedfactions.utils.options;

import io.github.toberocat.improvedFactions.core.exceptions.faction.FactionNotInStorage;
import io.github.toberocat.improvedFactions.core.exceptions.faction.PlayerHasNoFactionException;
import io.github.toberocat.improvedFactions.core.permission.Permission;
import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import org.jetbrains.annotations.NotNull;

public class FactionPermissionOption implements PlayerOption {
    private final Permission permission;

    public FactionPermissionOption(@NotNull Permission permission) {
        this.permission = permission;
    }

    @Override
    public @NotNull String[] executePlayer(@NotNull FactionPlayer player,
                              @NotNull String[] args) throws CommandException {
        try {
            if (!checkPermission(player))
                throw new CommandException("exceptions.permissions-missing", () -> new PlaceholderBuilder()
                        .placeholder("player", player)
                        .placeholder("permission", permission.label())
                        .getPlaceholders());
        } catch (FactionNotInStorage | PlayerHasNoFactionException e) {
            throw new CommandException(e);
        }
        return args;
    }

    @Override
    public boolean show(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof FactionPlayer player))
            return false;
        try {
            return checkPermission(player);
        } catch (FactionNotInStorage | PlayerHasNoFactionException e) {
            return false;
        }
    }

    private boolean checkPermission(@NotNull FactionPlayer player)
            throws FactionNotInStorage, PlayerHasNoFactionException {
        return player.getFaction().hasPermission(permission, player.getRank());
    }
}
