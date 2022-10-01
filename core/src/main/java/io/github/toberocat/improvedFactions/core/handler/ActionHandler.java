/**
 * Created: 01/10/2022
 *
 * @author Tobias Madlberger (Tobias)
 */

package io.github.toberocat.improvedFactions.core.handler;

import io.github.toberocat.improvedFactions.core.exceptions.NoImplementationProvidedException;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.registry.ImplementationHolder;
import org.jetbrains.annotations.NotNull;

public interface ActionHandler {
    static @NotNull ActionHandler api() {
        ActionHandler implementation = ImplementationHolder.actionHandler;
        if (implementation == null) throw new NoImplementationProvidedException("action handler");
        return implementation;
    }

    void renameFaction(@NotNull FactionPlayer<?> player);
}
