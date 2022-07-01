package io.github.toberocat.core.utility.action.provided;

import io.github.toberocat.core.utility.action.Action;
import io.github.toberocat.core.utility.language.Language;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class LangMessageAction extends Action {
    @Override
    public @NotNull String label() {
        return "lang-message";
    }

    @Override
    public void run(@NotNull Player player, @NotNull String provided) {
        Language.sendMessage(provided, player);
    }
}
