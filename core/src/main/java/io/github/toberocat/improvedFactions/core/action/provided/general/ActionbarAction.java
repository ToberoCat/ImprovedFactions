package io.github.toberocat.improvedFactions.core.action.provided.general;

import io.github.toberocat.improvedFactions.core.action.Action;
import io.github.toberocat.improvedFactions.core.handler.message.MessageHandler;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import org.jetbrains.annotations.NotNull;

public class ActionbarAction extends Action {

    @Override
    public @NotNull String label() {
        return "actionbar";
    }

    @Override
    public void run(@NotNull FactionPlayer<?> player, @NotNull String provided) {
        player.sendActionBar(MessageHandler.api().format(player, provided));
    }
}
