package io.github.toberocat.improvedFactions.core.action.provided.general;

import io.github.toberocat.improvedFactions.core.action.Action;
import io.github.toberocat.improvedFactions.core.handler.message.MessageHandler;
import io.github.toberocat.improvedFactions.core.player.ConsoleCommandSender;
import io.github.toberocat.improvedFactions.core.player.FactionPlayer;
import io.github.toberocat.improvedFactions.core.utils.Logger;
import org.jetbrains.annotations.NotNull;

public class MessageAction extends Action {

    @Override
    public @NotNull String label() {
        return "message";
    }

    @Override
    public void run(@NotNull ConsoleCommandSender consoleCommandSender, @NotNull String provided) {
        Logger.api().logInfo(provided);
    }

    @Override
    public void run(@NotNull FactionPlayer<?> player, @NotNull String provided) {
        player.sendMessage(MessageHandler.api().format(player, provided));
    }
}
