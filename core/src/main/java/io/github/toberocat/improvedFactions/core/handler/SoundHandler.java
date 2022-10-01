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

public interface SoundHandler {
    static @NotNull SoundHandler api() {
        SoundHandler implementation = ImplementationHolder.soundHandler;
        if (implementation == null) throw new NoImplementationProvidedException("sound handler");
        return implementation;
    }

    void playSound(@NotNull FactionPlayer<?> player, @NotNull String sound, float volume, float pitch);
}
