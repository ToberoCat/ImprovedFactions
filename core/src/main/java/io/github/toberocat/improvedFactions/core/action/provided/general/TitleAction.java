package io.github.toberocat.improvedFactions.core.action.provided.general;

import io.github.toberocat.improvedFactions.core.action.Action;
import io.github.toberocat.improvedFactions.core.handler.message.MessageHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

public class TitleAction extends Action {

    @Override
    public @NotNull String label() {
        return "title";
    }

    @Override
    public void run(@NotNull FactionPlayer player, @NotNull String[] args) {
        int length = args.length;
        if (args.length < 1) return;

        String title = MessageHandler.api().format(player, args[0].replace("_", " "));

        String subtitle = null;

        if (length >= 2) {
            subtitle = MessageHandler.api().format(player, args[1]);
        }

        player.sendTitle(title, subtitle == null ? "" : subtitle);
    }
}
