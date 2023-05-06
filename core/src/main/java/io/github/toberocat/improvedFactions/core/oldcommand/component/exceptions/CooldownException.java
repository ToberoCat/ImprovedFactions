package io.github.toberocat.improvedFactions.core.oldcommand.component.exceptions;

import fr.dov3.plugins.server.util.CooldownManager;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Created: 08/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class CooldownException extends CommandExceptions {
    public CooldownException(@NotNull CooldownManager manager, @NotNull UUID target) {
        super("You have to wait §6" + manager.getLeftTime(target) + "§c until you can use this command again");
    }
}
