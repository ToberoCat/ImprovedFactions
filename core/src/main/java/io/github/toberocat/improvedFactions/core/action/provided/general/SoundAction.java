package io.github.toberocat.improvedFactions.core.action.provided.general;

import io.github.toberocat.improvedFactions.core.action.Action;
import io.github.toberocat.improvedFactions.core.handler.SoundHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

public class SoundAction extends Action {

    @Override
    public @NotNull String label() {
        return "sound";
    }

    @Override
    public void run(@NotNull FactionPlayer player, @NotNull String[] args) {
        if (args.length == 0) {
            return;
        }

        /* Volume */

        float volume = 1;

        if (args.length >= 2) {
            try {
                volume = Float.parseFloat(args[1]);
            } catch (NumberFormatException ex) {
                return;
            }
        }

        /* Pitch */

        float pitch = 1;

        if (args.length >= 3) {
            try {
                pitch = Float.parseFloat(args[2]);
            } catch (NumberFormatException ex) {
                return;
            }
        }

        /* Play */

        SoundHandler.api().playSound(player, args[0], volume, pitch);
    }
}
