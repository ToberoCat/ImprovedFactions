package io.github.toberocat.core.utility.action.provided;

import io.github.toberocat.core.utility.action.Action;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class SoundAction extends Action {

    @Override
    public @NotNull String label() {
        return "sound";
    }

    @Override
    public void run(@NotNull Player player, @NotNull String[] args) {
        if (args.length == 0) {
            return;
        }

        /* Sound */

        String soundArg = args[0];
        Sound sound = null;

        for (Sound a : Sound.values()) {
            String b = a.toString();

            if (b.endsWith(soundArg)) {
                sound = a;
                break;
            }
        }

        if (sound == null) {
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

        player.playSound(player.getLocation(), sound, volume, pitch);
    }
}
