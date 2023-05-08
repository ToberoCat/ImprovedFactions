package io.github.toberocat.improvedFactions.core.oldcommand.component.exceptions;

import fr.dov3.plugins.server.util.CooldownManager;
import io.github.toberocat.improvedFactions.core.translator.PlaceholderBuilder;
import org.graalvm.compiler.nodes.PiNode;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

/**
 * Created: 08/02/2023
 *
 * @author Tobias Madlberger (Tobias)
 */
public class CooldownException extends CommandExceptions {
    public CooldownException(@NotNull CooldownManager manager, @NotNull UUID target) {
        super("exceptions.cooldown", new PlaceholderBuilder()
                .placeholder("target", target)
                .placeholder("left", manager)
                .getPlaceholders());
    }
}
