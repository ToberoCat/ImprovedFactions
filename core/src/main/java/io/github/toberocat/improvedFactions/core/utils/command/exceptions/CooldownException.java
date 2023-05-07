package io.github.toberocat.improvedFactions.core.utils.command.exceptions;

import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import io.github.toberocat.improvedFactions.core.utils.CooldownManager;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Created: 08/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class CooldownException extends CommandException {
    public CooldownException(@NotNull CooldownManager manager, @NotNull UUID target) {
        super("exceptions.cooldown", new PlaceholderBuilder()
                .placeholder("left", manager.getLeftTime(target))
                .getPlaceholders());
    }
}
