package io.github.toberocat.core.utility.action.provided;

import io.github.toberocat.core.utility.action.Action;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TitleAction extends Action {

    @Override
    public @NotNull String label() {
        return "title";
    }

    @Override
    public void run(@NotNull Player player, @NotNull String[] args) {
        int length = args.length;
        if (args.length < 1) return;

        String title = args[0].replace("_", " ");

        String subtitle = null;

        if (length >= 2) subtitle = args[1];

        int in = 20;

        if (length >= 3) {
            try {
                in = Integer.parseInt(args[2]);
            } catch (NumberFormatException ignored) {}
        }

        int stay = 5 * 20;

        if (length >= 4) {
            try {
                stay = Integer.parseInt(args[3]);
            } catch (NumberFormatException ignored) {}
        }

        int out = 20;

        if (length >= 3) {
            try {
                out = Integer.parseInt(args[4]);
            } catch (NumberFormatException ignored) {}
        }

        player.sendTitle(title, subtitle, in, stay, out);
    }
}
