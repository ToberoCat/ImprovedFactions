package io.github.toberocat.core.utility.action.provided;

import io.github.toberocat.core.utility.action.Action;
import io.github.toberocat.core.utility.language.Language;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class MessageAction extends Action {

    @Override
    public @NotNull String label() {
        return "message";
    }

    @Override
    public void run(@NotNull CommandSender commandSender, @NotNull String provided) {
        commandSender.sendMessage(Language.format(provided));
    }
}
