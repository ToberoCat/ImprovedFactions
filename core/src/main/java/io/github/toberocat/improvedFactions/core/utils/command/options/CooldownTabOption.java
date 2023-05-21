package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.CooldownManager;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CooldownException;
import org.jetbrains.annotations.NotNull;

public class CooldownTabOption implements PlayerOption {

    private final @NotNull CooldownManager cooldownManager;
    private final boolean hideWhenInCooldown;

    public CooldownTabOption(@NotNull CooldownManager cooldownManager,
                             boolean hideWhenInCooldown) {
        this.cooldownManager = cooldownManager;
        this.hideWhenInCooldown = hideWhenInCooldown;
    }

    @Override
    public @NotNull String[] executePlayer(@NotNull FactionPlayer sender, @NotNull String[] args) throws CommandException {
        if (cooldownManager.hasCooldown(sender.getUniqueId()))
            throw new CooldownException(cooldownManager, sender.getUniqueId());
        return args;
    }

    @Override
    public boolean show(@NotNull CommandSender sender, @NotNull String[] args) {
        if (!(sender instanceof FactionPlayer player))
            return false;
        return !hideWhenInCooldown || !cooldownManager.hasCooldown(player.getUniqueId());
    }
}