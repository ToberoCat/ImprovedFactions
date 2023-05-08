package io.github.toberocat.improvedFactions.core.utils.command.options;

import io.github.toberocat.improvedFactions.core.player.CommandSender;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CommandException;
import io.github.toberocat.improvedFactions.core.utils.command.exceptions.CooldownException;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.CooldownManager;
import org.jetbrains.annotations.NotNull;

public class CooldownOption implements PlayerOption {
    private final @NotNull CooldownManager cooldownManager;

    public CooldownOption(@NotNull CooldownManager cooldownManager) {
        this.cooldownManager = cooldownManager;
    }

    @Override
    public void canExecutePlayer(@NotNull FactionPlayer sender, @NotNull String[] args) throws CooldownException {
        cooldownManager.runCooldown(sender.getUniqueId());
    }
}
